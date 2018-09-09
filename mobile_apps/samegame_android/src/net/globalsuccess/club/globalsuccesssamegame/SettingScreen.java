package net.globalsuccess.club.globalsuccesssamegame;

import net.globalsuccess.club.globalsuccesssamegame.R;
import android.os.Bundle;
import android.preference.PreferenceActivity;

public class SettingScreen  extends PreferenceActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);

	}

	

}
