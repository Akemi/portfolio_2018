package net.globalsuccess.club.globalsuccesssamegame;

import net.globalsuccess.club.globalsuccesssamegame.R;
import net.globalsuccess.club.globalsuccesssamegame.global.GlobalVars;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class HomeScreen extends Activity {

	private GlobalVars glob = GlobalVars.getInstance();		//Singleton pattern variables
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home_screen);
		
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
		
		//set initial variables for the singleton pattern
		//VERY IMPORTANT, SEE DESCRIPTION IN GlobVars SINGLETON PATTER
		glob.setBlock1Image(BitmapFactory.decodeResource(getResources(), R.drawable.block1));
		glob.setBlock2Image(BitmapFactory.decodeResource(getResources(), R.drawable.block2));
		glob.setBlock3Image(BitmapFactory.decodeResource(getResources(), R.drawable.block3));
		glob.setBlock4Image(BitmapFactory.decodeResource(getResources(), R.drawable.block4));
		glob.setBlock5Image(BitmapFactory.decodeResource(getResources(), R.drawable.block5));
		
		//set fontface
		glob.setTypeFace(Typeface.createFromAsset(getAssets(),"fonts/avenir-light.ttf"));
		((Button)findViewById(R.id.start_button)).setTypeface(glob.getTypeFace());
		((Button)findViewById(R.id.info_button)).setTypeface(glob.getTypeFace());
		
	}
	
	@Override
	protected void onResume() {
	    super.onResume();
	    
	    //set global variables from settings screen
	    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
	    glob.setNumOfColours(Integer.parseInt(prefs.getString("colour_num", "3")));
	    glob.setDifficulty(Integer.parseInt(prefs.getString("difficulty", "2")));
	    
	}

	public void changeActivity (View view) {
		//jump to GameScreen
		startActivity(new Intent(this, GameScreen.class));
	}
	
	public void changeActivityInfo (View view) {
		//jump to InfoScreen
		startActivity(new Intent(this, InfoScreen.class));
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home_screen, menu);
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item){
	    //trigger for actionbar
	    switch(item.getItemId()) {
	    case R.id.action_settings:
	        startActivity(new Intent(this, SettingScreen.class));
	        break;
	    }
	
	    return true;
	}
	
	
	//helper function to make Toast messages
	public void makeToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


}
