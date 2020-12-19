package mywin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class SoundPlayer implements ISoundPlayer
{
	private IPreferenceSound soundPreference;

	public SoundPlayer(IPreferenceSound soundPreference) {
		this.soundPreference = soundPreference;
	}
	
	@Override
	public void playChime()
	{
		Thread newThread = new Thread(() -> {
			playSoundFromResource(FirstSwingExample.class.getResourceAsStream("/mywin/ClockChimes.wav"), 8000);
		});
		newThread.start();
	}

	@Override
	public void playAlarm()
	{
		Thread newThread = new Thread(() -> {
			String sourcePath = soundPreference.getSoundPath();
			playSoundFromFile(sourcePath);
		});
		newThread.start();
	}

	private void playSoundFromFile(String fileName)
	{
		try
		{
			if (fileName == null)
			{
				System.out.println("No sound file to play");
				return;
			}
			File audioFile = new File(fileName);
			AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
			AudioFormat format = audioStream.getFormat();
			DataLine.Info info = new DataLine.Info(Clip.class, format);
			Clip audioClip = (Clip) AudioSystem.getLine(info);
			audioClip.open(audioStream);
			audioClip.start();
			
			long audioFileLength = audioFile.length();
			int frameSize = format.getFrameSize();
			float frameRate = format.getFrameRate();
			float durationInSeconds = (audioFileLength / (frameSize * frameRate));		
			long sleepInMillis = (long)durationInSeconds * 1000;		
			System.out.println(String.format("%.2f seconds (%d ms)", durationInSeconds, sleepInMillis));
			
			Thread.sleep(sleepInMillis);
			
			audioClip.flush();
			audioClip.close();
			audioStream.close();
		} catch (UnsupportedAudioFileException | IOException | LineUnavailableException | InterruptedException e)
		{
			e.printStackTrace();
		} finally
		{
			System.out.println("Played");
		}
	}

	private void playSoundFromResource(InputStream inputStream, long sleepMillis)
	{
		try
		{
			AudioInputStream audioStream = AudioSystem.getAudioInputStream(inputStream);
			AudioFormat format = audioStream.getFormat();
			DataLine.Info info = new DataLine.Info(Clip.class, format);
			Clip audioClip = (Clip) AudioSystem.getLine(info);
			audioClip.open(audioStream);
			audioClip.start();
			Thread.sleep(sleepMillis);
			audioClip.flush();
			audioClip.close();
			audioStream.close();
		} catch (UnsupportedAudioFileException | IOException | LineUnavailableException | InterruptedException e)
		{
			e.printStackTrace();
		} finally
		{
			System.out.println("Played");
		}
	}

}
