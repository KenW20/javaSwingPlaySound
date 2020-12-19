package mywin;

import java.util.prefs.Preferences;

public class PreferenceSound implements IPreferenceSound
{
	private static final String SOUND_PATH_KEY = "Sound_Path";

	@Override
	public String getSoundPath()
	{
		return getPreference(SOUND_PATH_KEY, null);
	}

	@Override
	public void setSoundPath(String soundPath)
	{
		setPreference(SOUND_PATH_KEY, soundPath);
	}
	
	private String getPreference(final String PREF_NAME, String defaultValue)
	{
		// Retrieve the user preference node for the package com.mycompany
		Preferences prefs = Preferences.userNodeForPackage(mywin.FirstSwingExample.class);

		// Get the value of the preference;
		// default value is returned if the preference does not exist
		String propertyValue = prefs.get(PREF_NAME, defaultValue);
		return propertyValue;
	}

	private void setPreference(final String PREF_NAME, String propValue)
	{
		// Retrieve the user preference node for the package com.mycompany
		Preferences prefs = Preferences.userNodeForPackage(mywin.FirstSwingExample.class);

		// Set the value of the preference
		prefs.put(PREF_NAME, propValue);
	}
}
