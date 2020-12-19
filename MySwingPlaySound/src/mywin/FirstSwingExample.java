package mywin;

import java.awt.*; // Using AWT layouts
import java.awt.event.*; // Using AWT event classes and listener interfaces
import java.io.*;
import java.util.prefs.Preferences;

import javax.sound.sampled.*;
import javax.swing.*; // Using Swing components and containers

public class FirstSwingExample
{
	static long nextInterval;

	public static void main(String[] args)
	{
		JFrame f = new JFrame();// creating instance of JFrame
		buildContent(f);
		startTimer();
	}

	private static void startTimer()
	{
		long startTime = System.currentTimeMillis();
		long wakeInterval = 10000;
		nextInterval = wakeInterval;

		ActionListener taskPerformer = new ActionListener() {
			public void actionPerformed(ActionEvent evt)
			{
				long elapsedTime = System.currentTimeMillis() - startTime;
				System.out.println(String.format("elapsedTime in millis %,d", elapsedTime));

				if (elapsedTime > nextInterval)
				{
					playAlarm();
					nextInterval += wakeInterval;
				}
			}
		};
		Timer timer = new Timer(500, taskPerformer);
		timer.setRepeats(true);
		timer.start();
	}

	private static void buildContent(JFrame f)
	{
		JLabel counterLabel = new JLabel("Counter");
		counterLabel.setBounds(30, 50, 100, 40);// x, y, width, height
		f.add(counterLabel);

		JTextField counterTextField = new JTextField("0");
		counterTextField.setEditable(true);
		counterTextField.setBounds(130, 50, 100, 40);// x, y, width, height
		counterTextField.setHorizontalAlignment(JTextField.RIGHT);
		f.add(counterTextField);

		JButton button = new JButton("Play Now");// creating instance of JButton
		button.setBounds(130, 100, 100, 40);// x axis, y axis, width, height
		button.addActionListener(buttonAction(counterTextField));
		f.add(button);// adding button in JFrame

		f.setSize(400, 300);// width and height
		f.setLocation(-1000, 10);
		f.setLayout(null);// using no layout managers
		f.setVisible(true);// making the frame visible
		f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		String sountPath = getPreference("Sound_Path", null);

		JTextField soundPathTextField = new JTextField(sountPath);
		soundPathTextField.setEditable(true);
		soundPathTextField.setBounds(30, 155, 200, 35);// x, y, width, height
		soundPathTextField.setHorizontalAlignment(JTextField.LEFT);
		f.add(soundPathTextField);

		JButton changeSoundButton = new JButton("Change Sound");// creating instance of JButton
		changeSoundButton.setBounds(230, 155, 130, 35);// x axis, y axis, width, height
		changeSoundButton.addActionListener(changeSoundbuttonAction(soundPathTextField));
		f.add(changeSoundButton);// adding button in JFrame
	}

	private static String getPreference(final String PREF_NAME, String defaultValue)
	{
		// Retrieve the user preference node for the package com.mycompany
		Preferences prefs = Preferences.userNodeForPackage(mywin.FirstSwingExample.class);

		// Get the value of the preference;
		// default value is returned if the preference does not exist
		String propertyValue = prefs.get(PREF_NAME, defaultValue);
		return propertyValue;
	}

	private static void setPreference(final String PREF_NAME, String propValue)
	{
		// Retrieve the user preference node for the package com.mycompany
		Preferences prefs = Preferences.userNodeForPackage(mywin.FirstSwingExample.class);

		// Set the value of the preference
		prefs.put(PREF_NAME, propValue);
	}

	private static ActionListener changeSoundbuttonAction(JTextField soundPathTextField)
	{
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt)
			{
				String soundPath = getPreference("Sound_Path", null);
				
				JFileChooser j = new JFileChooser(soundPath);

				int r = j.showOpenDialog(null);

				if (r == JFileChooser.APPROVE_OPTION)
				{
					String newPath = j.getSelectedFile().getAbsolutePath();
					
					soundPathTextField.setText(newPath);
					
					setPreference("Sound_Path", newPath);					
				}
				else
					System.out.println("the user cancelled the operation");
			}
		};
	}

	private static ActionListener buttonAction(JTextField counterTextField)
	{
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt)
			{
				System.out.println(String.format("%s\n", counterTextField.getText()));
				playAlarm();
				playChime();
			}
		};
	}

	private static void playChime()
	{
		Thread newThread = new Thread(() -> {
			playSoundFromResource(FirstSwingExample.class.getResourceAsStream("/mywin/ClockChimes.wav"), 8000);
		});
		newThread.start();
	}

	private static void playAlarm()
	{
		Thread newThread = new Thread(() -> {
			playSoundFromFile("D:\\Users\\Kenneth\\Downloads\\Alarm.wav", 1000);
		});
		newThread.start();
	}

	private static void playSoundFromFile(String fileName, long sleepMillis)
	{
		try
		{
			File audioFile = new File(fileName);
			AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
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

	private static void playSoundFromResource(InputStream inputStream, long sleepMillis)
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
