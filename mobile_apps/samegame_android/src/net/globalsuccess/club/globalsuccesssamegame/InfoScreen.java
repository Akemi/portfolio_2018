package net.globalsuccess.club.globalsuccesssamegame;

import java.util.Random;

import net.globalsuccess.club.globalsuccesssamegame.global.GlobalVars;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class InfoScreen extends Activity {

	private GlobalVars glob = GlobalVars.getInstance();		//Singleton pattern variables
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_info_screen);
		
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
		
		String items [] = getResources().getStringArray(R.array.advert_screen_sentences);
		
		TextView advertText = (TextView) findViewById(R.id.advertText);
		advertText.setTypeface(glob.getTypeFace());

		Random rnd = new Random();
		advertText.setText(items[rnd.nextInt(items.length)]);
	}
	
	public void onClickFb (View view) {
		//jump to GameScreen
		String url = "http://facebook.com/GSCFrankfurt";
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setData(Uri.parse(url));
		startActivity(i);
	}
	
	public void onClickTwi (View view) {
		//jump to GameScreen
		String url = "https://www.twitter.com/GSCFrankfurt";
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setData(Uri.parse(url));
		startActivity(i);
	}
	
	/*public void onClickYt (View view) {
		//jump to GameScreen
		String url = "http://www.example.com";
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setData(Uri.parse(url));
		startActivity(i);
	}*/
	
	public void onClickGsc (View view) {
		//jump to GameScreen
		String url = "https://www.globalsuccess-club.net";
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setData(Uri.parse(url));
		startActivity(i);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		
		return true;
	}

}
