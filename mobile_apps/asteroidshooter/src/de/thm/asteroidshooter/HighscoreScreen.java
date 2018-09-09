package de.thm.asteroidshooter;

import java.util.ArrayList;
import java.util.Collections;
import de.thm.asteroidshooter.Global.GlobalVars;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class HighscoreScreen extends Activity {

	private String scoreList;											//shared preferences score list
	private String scoreListSave="";									//new score list needs saving
	private int myScore;												//my own score
	private TextView myScoreText;										//textview for score text
	private ListView scoreListView;										//listview for all scores
	public ArrayList<Integer> list = new ArrayList<Integer>();			//array list to sort
	public ArrayList<String> listForView = new ArrayList<String>();		//array list as string for listview
    public ArrayAdapter<String> adapter;								//array adapter
    private GlobalVars glob = GlobalVars.getInstance();					//Singelton Pattern global varibales
    private Button backButton;											//back to mainscreen button
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_highscore_screen);
		
		Intent intent = getIntent();
		int myScore = intent.getIntExtra("score", 0);
        //Log.v("test", Integer.toString(myScore));
		
		//back to main screen
		backButton = (Button) findViewById(R.id.backButton);
		backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	changeToHomeScreen();
            }
        });
		
        //score text
        myScoreText = (TextView) findViewById(R.id.yourScoreText);
        scoreListView = (ListView) findViewById(R.id.scoreListView);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listForView);
        scoreListView.setAdapter(adapter);
        
        myScoreText.setText("Your Score: " + Integer.toString(myScore));

        SharedPreferences settings = getSharedPreferences("de.thm.asteroidshooter", 0);

        scoreList = settings.getString("scoreList", "");
        //scoreList = "";
        Log.v("test", scoreList);
        
        //new list with old score and new
        if(scoreList != ""){
        	String[] parts = scoreList.split(":");
        	
        	for(int i=0; i < parts.length; i++){
        		list.add(Integer.parseInt(parts[i]));
        	}
        	list.add(myScore);
        }
        else{
        	list.add(myScore);
        }
        
        //sort new score list
        Collections.sort(list, Collections.reverseOrder());
        
        //save new score list
        for (int s : list){
        	scoreListSave += s + ":";
        }
        
        //add to list view
        for(int i=0; i<list.size(); i++){
        	listForView.add(i+1 + ". " + list.get(i));
        }
        
        //save changes
        Log.v("test", scoreListSave);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("scoreList", scoreListSave);
        editor.commit(); 
	}
	
	private void changeToHomeScreen(){
		Intent myIntent = new Intent(HighscoreScreen.this, MainScreen.class);
    	myIntent.putExtra("multiplayer", false);
    	HighscoreScreen.this.startActivity(myIntent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_highscore_screen, menu);
		return true;
	}
	
	//when closing game go back to previous activity
	protected void onPause()  { 
		 super.onPause();
		 glob.getPlayer().pause();
	}
	
	public void onResume() {
		super.onResume();
		glob.getPlayer().start();
	}
	
	@Override
	public void onBackPressed() {
		changeToHomeScreen();
	}

}
