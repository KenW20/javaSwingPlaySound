package mywin;

import java.awt.*; // Using AWT layouts
import java.awt.event.*; // Using AWT event classes and listener interfaces
import javax.swing.*; // Using Swing components and containers
import javax.swing.border.BevelBorder;

public class FirstSwingExample
{
	private IPreferenceSound soundPreference;
	private ISoundPlayer soundPlayer;
	private long nextInterval;
	private JLabel statusLabel;
	
	public static void main(String[] args)
	{
		FirstSwingExample example = new FirstSwingExample();

		JFrame f = new JFrame();// creating instance of JFrame
		example.buildContent(f);
		example.startTimer();
	}

	public FirstSwingExample() {
		soundPreference = new PreferenceSound();
		soundPlayer = new SoundPlayer(soundPreference);
	}

	private void startTimer()
	{
		long startTime = System.currentTimeMillis();
		long wakeInterval = 10000;
		nextInterval = wakeInterval;

		ActionListener taskPerformer = new ActionListener() {
			public void actionPerformed(ActionEvent evt)
			{
				long elapsedTime = System.currentTimeMillis() - startTime;
				String message = String.format("elapsedTime in millis %,d", elapsedTime);
				System.out.println(message);
				updateStatus(message);

				if (elapsedTime > nextInterval)
				{
					soundPlayer.playAlarm();
					nextInterval += wakeInterval;
				}
			}
		};
		Timer timer = new Timer(500, taskPerformer);
		timer.setRepeats(true);
		timer.start();
	}

	private void updateStatus(String text)
	{
		statusLabel.setText(text);
	}
	
	private JLabel createStatusLabel()
	{
		JLabel statusLabel = new JLabel("status");
		statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
		return statusLabel;
	}
	
	private void buildContent(JFrame f)
	{
		f.setLayout(new BorderLayout());
		
		JPanel statusPanel = new JPanel();
		statusPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
		statusPanel.setPreferredSize(new Dimension(f.getWidth(), 24));
		statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.X_AXIS));
		
		statusLabel = createStatusLabel();
		statusPanel.add(statusLabel);
		
		f.add(statusPanel, BorderLayout.SOUTH);
		
		JPanel mainPanel = new JPanel();	
		mainPanel.setLayout(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;

		JLabel counterLabel = new JLabel("Counter");
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 0.25;
		mainPanel.add(counterLabel, c);

		JTextField counterTextField = new JTextField("0");
		counterTextField.setEditable(true);
		counterTextField.setHorizontalAlignment(JTextField.RIGHT);
		c.gridx = 1;
		c.gridy = 0;
		c.weightx = 0.75;
		mainPanel.add(counterTextField, c);

		JButton button = new JButton("Play Now");// creating instance of JButton
		button.addActionListener(buttonAction(counterTextField));
		c.gridx = 1;
		c.gridy = 1;
		c.weightx = 0.5;
		c.insets = new Insets(3, 0, 3, 13);
		mainPanel.add(button, c);

		PreferencePanel preferencePanel = new PreferencePanel(soundPreference);
		c.gridx = 0;
		c.gridy = 3;
		c.weightx = 1;
		c.gridwidth = 2;
		c.insets = new Insets(0, 0, 0, 0);
		mainPanel.add(preferencePanel, c);

		f.add(mainPanel, BorderLayout.CENTER);		

		f.setSize(400, 300);// width and height
		f.setLocation(-1000, 10);
		f.setVisible(true);// making the frame visible
		f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

	}

	private void buildContentWithNoLayout(JFrame f)
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

		PreferencePanel preferencePanel = new PreferencePanel(soundPreference);
		preferencePanel.setBounds(0, 160, 390, 42);// x axis, y axis, width, height
		f.add(preferencePanel);
	}

	private ActionListener buttonAction(JTextField counterTextField)
	{
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt)
			{
				System.out.println(String.format("%s\n", counterTextField.getText()));
				soundPlayer.playAlarm();
				soundPlayer.playChime();
			}
		};
	}

}
