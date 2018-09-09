package net.globalsuccess.club.globalsuccesssamegame;

import java.util.ArrayList;
import java.util.Collections;

import net.globalsuccess.club.globalsuccesssamegame.R;
import net.globalsuccess.club.globalsuccesssamegame.global.GlobalVars;
import android.os.Build;
import android.os.Bundle;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class ScoreScreen extends Activity {
	private String scoreList;											//shared preferences score list
	private String scoreListSave="";									//new score list needs saving
	private ArrayList<Integer> list = new ArrayList<Integer>();			//array list to sort
	private ArrayList<String> listForView = new ArrayList<String>();	//array list as string for listview
	private ListView scoreListView;										//listview for all scores
	private CustomAdapter adapter;								//array adapter
	private GlobalVars glob = GlobalVars.getInstance();					//Singleton pattern variables
	private SharedPreferences settings;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_score_screen);
		
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
		
		//get the score from intent
		Intent intent = getIntent();
		int myScore = intent.getIntExtra("score", 0);
        
		//set actionbar title
		int currentapiVersion = android.os.Build.VERSION.SDK_INT;
		if (currentapiVersion >= android.os.Build.VERSION_CODES.HONEYCOMB){
			ActionBar actionBar = getActionBar();
			
			String items [] = getResources().getStringArray(R.array.setting_difficulty_entries);
			String item = items[glob.getDifficulty()-2].toString();
			
			actionBar.setTitle(this.getString(R.string.title_activity_score_screen) + " " + glob.getNumOfColours() + 
					" " + this.getString(R.string.score_screen_color) + " " + item);
		}
		
		//set the my score text
        TextView myScoreText = (TextView) findViewById(R.id.yourScore);
        myScoreText.setText(this.getString(R.string.score_screen_score) + " " + myScore);
        myScoreText.setTypeface(glob.getTypeFace());
        
        
        //reads the list from shared preferences, modifies it, saves it
        scoreListView = (ListView) findViewById(R.id.scoreList);
        adapter = new CustomAdapter(this, R.layout.list_item, listForView);
        scoreListView.setAdapter(adapter);
        
        //load the right list
        //three colours
        if(glob.getNumOfColours() == 3){
        	if(glob.getDifficulty() == GlobalVars.DIF_EASY){
              	 settings = getSharedPreferences("net.globalsuccess.club.globalsuccesssamegame.scorelistEasyThree", 0);
       	    }
       	    if(glob.getDifficulty() == GlobalVars.DIF_NORMAL){
       	   	 	 settings = getSharedPreferences("net.globalsuccess.club.globalsuccesssamegame.scorelistNormalThree", 0);
       	    }
       	    if(glob.getDifficulty() == GlobalVars.DIF_HARD){
       	    	 settings = getSharedPreferences("net.globalsuccess.club.globalsuccesssamegame.scorelistHardThree", 0);
       	    }
        }
        
        //four colours
        if(glob.getNumOfColours() == 4){
        	if(glob.getDifficulty() == GlobalVars.DIF_EASY){
              	 settings = getSharedPreferences("net.globalsuccess.club.globalsuccesssamegame.scorelistEasyFour", 0);
       	    }
       	    if(glob.getDifficulty() == GlobalVars.DIF_NORMAL){
       	   	 	 settings = getSharedPreferences("net.globalsuccess.club.globalsuccesssamegame.scorelistNormalFour", 0);
       	    }
       	    if(glob.getDifficulty() == GlobalVars.DIF_HARD){
       	    	 settings = getSharedPreferences("net.globalsuccess.club.globalsuccesssamegame.scorelistHardFour", 0);
       	    }
        }
	    
	    //five colours
        if(glob.getNumOfColours() == 5){
        	if(glob.getDifficulty() == GlobalVars.DIF_EASY){
              	 settings = getSharedPreferences("net.globalsuccess.club.globalsuccesssamegame.scorelistEasyFive", 0);
       	    }
       	    if(glob.getDifficulty() == GlobalVars.DIF_NORMAL){
       	   	 	 settings = getSharedPreferences("net.globalsuccess.club.globalsuccesssamegame.scorelistNormalFive", 0);
       	    }
       	    if(glob.getDifficulty() == GlobalVars.DIF_HARD){
       	    	 settings = getSharedPreferences("net.globalsuccess.club.globalsuccesssamegame.scorelistHardFive", 0);
       	    }
        }
	    
        
        //get the list as string
        scoreList = settings.getString("scoreList", "");
        
        //new list with old score and new
        if(scoreList != ""){
        	String[] parts = scoreList.split(":");
        	
        	for(int i=0; i < parts.length; i++) list.add(Integer.parseInt(parts[i]));
        	list.add(myScore);
        }
        else list.add(myScore);
        
        //sort new score list
        Collections.sort(list, Collections.reverseOrder());
        
        //save new score list
        for (int s : list) scoreListSave += s + ":";
        
        //add to list view
        for(int i=0; i<list.size(); i++) listForView.add(i+1 + ". " + list.get(i));
        
        //save changes
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("scoreList", scoreListSave);
        editor.commit(); 
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		
		return true;
	}
	
	@Override
	public void onBackPressed() {
		startActivity(new Intent(this, HomeScreen.class));
	}
	
	public class CustomAdapter extends ArrayAdapter<String>{
		
		private final Typeface mTypeface;
		
	    public CustomAdapter(Context context, int textViewResourceId, ArrayList<java.lang.String> listForView) {
	        super(context, textViewResourceId, listForView);

	        mTypeface = glob.getTypeFace();
	    }
	    
	    @Override 
	    public View getView(int position, View convertView, ViewGroup parent) {
	        View view = super.getView(position, convertView, parent);
	        TextView textview = (TextView) view;
	        textview.setTypeface(mTypeface);
	        return textview;
	    }
	}

}
