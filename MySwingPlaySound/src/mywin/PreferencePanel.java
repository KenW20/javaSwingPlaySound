package mywin;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class PreferencePanel extends JPanel
{
	private IPreferenceSound soundPreference;

	public PreferencePanel(IPreferenceSound soundPreference) {
		this.soundPreference = soundPreference;

		buildContent(this);
	}

	private void buildContent(JPanel p)
	{
		p.setLayout(new BorderLayout(3, 3));

		JTextField soundPathTextField = new JTextField(soundPreference.getSoundPath());
		soundPathTextField.setEditable(true);
		soundPathTextField.setPreferredSize(new Dimension(305, 35));
		soundPathTextField.setHorizontalAlignment(JTextField.LEFT);
		p.add(soundPathTextField, BorderLayout.CENTER);

		JButton changeSoundButton = new JButton("Change Sound");// creating instance of JButton
		changeSoundButton.setPreferredSize(new Dimension(50, 35));
		changeSoundButton.addActionListener(changeSoundbuttonAction(soundPathTextField));
		p.add(changeSoundButton, BorderLayout.EAST);

		p.setBackground(Color.YELLOW);
		p.setVisible(true);
	}

	private ActionListener changeSoundbuttonAction(JTextField soundPathTextField)
	{
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt)
			{
				String soundPath = soundPreference.getSoundPath();

				JFileChooser j = new JFileChooser(soundPath);

				int r = j.showOpenDialog(null);

				if (r == JFileChooser.APPROVE_OPTION)
				{
					String newPath = j.getSelectedFile().getAbsolutePath();

					soundPathTextField.setText(newPath);

					soundPreference.setSoundPath(newPath);
				} else
				{
					System.out.println("the user cancelled the operation");
				}
			}
		};
	}

}
