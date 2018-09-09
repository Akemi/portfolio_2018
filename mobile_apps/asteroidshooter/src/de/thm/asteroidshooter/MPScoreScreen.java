package de.thm.asteroidshooter;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class MPScoreScreen extends Activity {

	private TextView MPScore;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mpscore_screen);
		
		//MPScore
		Intent intent = getIntent();
		int myScore = intent.getIntExtra("MPscore", 0);
        //Log.v("test", Integer.toString(myScore));
		
		if(myScore == 0){
			MPScore = (TextView) findViewById(R.id.MPScore);  
			MPScore.setText("You Lose.");
			MPScore.setTextColor(getResources().getColor(R.color.text_red));
		}
		else{
			MPScore = (TextView) findViewById(R.id.MPScore);  
			MPScore.setText("You Win!");
			MPScore.setTextColor(getResources().getColor(R.color.text_green));
		}
        
		

	}
	
	private void changeToHomeScreen(){
		Intent myIntent = new Intent(MPScoreScreen.this, MainScreen.class);
    	myIntent.putExtra("multiplayer", false);
    	MPScoreScreen.this.startActivity(myIntent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_mpscore_screen, menu);
		return true;
	}
	
	@Override
	public void onBackPressed() {
		changeToHomeScreen();
	}

}
