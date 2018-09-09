package net.globalsuccess.club.globalsuccesssamegame;

import net.globalsuccess.club.globalsuccesssamegame.R;
import net.globalsuccess.club.globalsuccesssamegame.Game.GameView;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.widget.RelativeLayout;

public class GameScreen extends Activity {
	
	private GameView gameview;								//GameView instance
	private RelativeLayout gameLayout;						//Layout containing the GamesView
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_screen);
		
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
		
		gameLayout = (RelativeLayout) findViewById(R.id.sameGameView);
		
		gameview = new GameView(this);
        gameLayout.addView(gameview);
        
	}
	
	public void changeActivity(int score){
		Intent myIntent = new Intent(this, AdvertScreen.class);
		myIntent.putExtra("score", score);
		startActivity(myIntent);
		
		//startActivity(new Intent(this, ScoreScreen.class));
	}
	

}
