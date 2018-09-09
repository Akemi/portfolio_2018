package de.thm.asteroidshooter;

import de.thm.asteroidshooter.Global.GlobalVars;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;


public class MainScreen extends Activity {

	private Button sp_button;
	private Button mp_button;
	private MediaPlayer player;
	private GlobalVars glob = GlobalVars.getInstance();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_screen);
		
		glob.setOpponentReady(false);
		glob.setYourselfReady(false);
		
		sp_button = (Button) findViewById(R.id.sp_button);
		mp_button = (Button) findViewById(R.id.mp_button);
		
		sp_button.getBackground().setAlpha(150);
		mp_button.getBackground().setAlpha(150);
		
		//play background music
        if(glob.getPlayer() == null){
        	player = MediaPlayer.create(MainScreen.this, R.raw.bgmusic); 
            player.setLooping(true);
            player.setVolume(100,100); 
            player.start();
            glob.setPlayer(player);
       }
		
        //singleplayer button, sets multiplayer intent false
        sp_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	Intent myIntent = new Intent(MainScreen.this, GameScreen.class);
            	myIntent.putExtra("multiplayer", false);
            	MainScreen.this.startActivity(myIntent);
            }
        });
		
      //multiplayer button, sets multiplayer intent true
		mp_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	Intent myIntent = new Intent(MainScreen.this, GameScreen.class);
            	myIntent.putExtra("multiplayer", true);
            	MainScreen.this.startActivity(myIntent);
            }
        });
		
	}
	
	protected void onPause()  { 
		 super.onPause();
		 glob.getPlayer().pause();
	}
	
	public void onResume() {
		super.onResume();
		glob.getPlayer().start();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main_screen, menu);
		return true;
	}

}
