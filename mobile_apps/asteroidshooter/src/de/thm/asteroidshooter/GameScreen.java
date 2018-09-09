package de.thm.asteroidshooter;

import java.util.ArrayList;
import java.util.List;
import de.thm.asteroidshooter.Connection.MOWSchnittstellenInterface;
import de.thm.asteroidshooter.Game.GameShakeListener;
import de.thm.asteroidshooter.Game.GameView;
import de.thm.asteroidshooter.Global.GlobalVars;
import de.thm.asteroidshooter.Joystick.DualJoystickView;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class GameScreen extends Activity {

	private DualJoystickView dualJoystick;					//Dual Joystick View
	private RelativeLayout gameLayout;						//Game Layout for adding the game view
	private RelativeLayout MPOverlay;						//Overlay for Multiplayer
	private Button MPOverlayButton;							//Ready Button for MP
	private TextView MPOverlayText;							//Status of Opponent
	private GlobalVars glob = GlobalVars.getInstance();		//Singelton Pattern global varibales
	private SensorManager sensorManager;					//Sensor Manager for Bomb
    private GameShakeListener gameShakeListener;			//Listener for Bomb
    private GameView gv = null;								//GameView instance 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_screen);
		
		//Asteroid Shooter Multiplayer Overlay Objekte
		MPOverlay = (RelativeLayout) findViewById(R.id.asteroidShooterMultiplayerOverlay);
		MPOverlayButton = (Button) findViewById(R.id.asteroidShooterMultiplayerOverlayReadyButton);
		MPOverlayText = (TextView) findViewById(R.id.asteroidShooterMultiplayerOverlayText);
		
		//Initialise game
		gameScreenInit();
		
		//intent if mp or sp
		Intent intent = getIntent();
		boolean mp = intent.getBooleanExtra("multiplayer", false);
		
		//MP init
		chosenServiceIndex = -1;
		availableServices=new ArrayList<PackageInfo>();
		List<PackageInfo> services=getPackageManager().getInstalledPackages(PackageManager.GET_SERVICES);

		//if MP set overlay visible
		if(mp == true){
			glob.setMp(true);
			MPOverlay.setVisibility(View.VISIBLE);
			
			for(PackageInfo pi : services){
				if(pi.packageName.startsWith(MOWSchnittstellenInterface.SERVICE_PACKAGE)){
					availableServices.add(pi);
				}
			}
			
			//work around fuer service chooser
			new Handler().postDelayed(new Runnable() { 
				public void run(){
					new ClassChooserDialog().show(getFragmentManager(), TAG);
                } 
			}, 1000);
			
		}
		//if SP start game automatically
		else{
			glob.setMp(false);
			gv.gameStart();
		}
		
		//multiplayer ready on click method
		MPOverlayButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	MPOverlayButton.setText("You are Ready.");
            	MPOverlayButton.setBackgroundColor(getResources().getColor(R.color.text_green));
            	MPOverlayButton.setEnabled(false);
            	
            	//send ready message
            	sendMessage(GlobalVars.TRIGGER_READY);
            	
            	//set self as ready
            	glob.setYourselfReady(true);
            	
            	//check is game is startable
            	gameReadyToStart();
            }
        });
	}
	
	private void gameReadyToStart(){
		//check is MP game is ready
		if(glob.isOpponentReady() && glob.isYourselfReady()){
			//MP overlay invisible
			MPOverlay.setVisibility(View.GONE);
			
			//start game
			gv.gameStart();
			Log.v("start", "strat");
		}
	}
	
	//activity init
	private void gameScreenInit(){
		//get joystick and game view
		dualJoystick = (DualJoystickView)findViewById(R.id.asteroidShooterDualjoystickView);
		gameLayout = (RelativeLayout) findViewById(R.id.asteroidShooterGameView);
		
		dualJoystick.setVisibility(View.VISIBLE);
		
		//create my game view and add it to the layout
		if(gv == null)gv = new GameView(this, dualJoystick);
        gameLayout.addView(gv);

        //create and set bomb listener
        gameShakeListener = new GameShakeListener();
	    sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
	    sensorManager.registerListener(gameShakeListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_UI);
	    
	    //set method for bomb
	    gameShakeListener.setOnShakeListener(new GameShakeListener.OnShakeListener() {
	    	public void onShake() {
	    		//if MP and bomb send bomb message to opponent, else set bomb in SP
	    		if(glob.isMp() && gv.isBombSet()) sendMessage(GlobalVars.TRIGGER_BOMB);
	    		else gv.setBomb();
	    	}
	    });
	}
	
	//change activity to highscore screen
	public void changeActivity(int score){
		if(glob.isMp()){
			if(score == 0){
				sendMessage(GlobalVars.TRIGGER_DEAD);
			}
			
			//DONT FORGET TO REMOVE THE COMMENT MARKS, END GAME TRIGGER
			/*Message msg = Message.obtain(null, MOWSchnittstellenInterface.MESSAGE_5_CLOSE);
			try{
				messengerService.send(msg);
			}
			catch(RemoteException e){
				Log.e(TAG, e.getClass().getName() + ": " + e.getMessage());
			}*/
			
			Intent myIntent = new Intent(GameScreen.this, MPScoreScreen.class);
			myIntent.putExtra("MPscore", score);
			GameScreen.this.startActivity(myIntent);
		}
		else{
			Intent myIntent = new Intent(GameScreen.this, HighscoreScreen.class);
			myIntent.putExtra("score", score);
			GameScreen.this.startActivity(myIntent);
		}
		
		
	}
	
	//------------------------------------------------------------------------------------------------
	//------------------------------------------------------------------------------------------------
	//								Multiplayer
	//------------------------------------------------------------------------------------------------
	//------------------------------------------------------------------------------------------------
	
	private final String TAG=GameScreen.class.getName();
	
	private ServiceConnection serviceConnection = new MyServiceConnection();
	private boolean boundToService=false;
	private final Messenger messengerActivity = new Messenger(new MessageHandler());
	private Messenger messengerService;
	private List<PackageInfo> availableServices;
	private int chosenServiceIndex;
	private Bundle bundleMessage2;
	private Bundle bundleMessage4;
	
	
	 private class ClassChooserDialog extends DialogFragment implements DialogInterface.OnClickListener{
			@Override
			public Dialog onCreateDialog(Bundle savedInstanceState){
				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				builder.setTitle("Choose your service");
				CharSequence[] texts = new CharSequence[availableServices.size()];
				for(int i = 0; i < availableServices.size(); i++){
					PackageInfo service=availableServices.get(i);
					texts[i]=service.applicationInfo.loadLabel(getPackageManager()) + " (" + service.packageName + ")";
				}
				builder.setItems(texts, this);
				return builder.create();
			}

			public void onClick(DialogInterface dialog, int which){
				chosenServiceIndex = which;
				doBind();
			}
			
		}
		
	    private void doBind(){
			Intent i=new Intent();
			PackageInfo chosenService = availableServices.get(chosenServiceIndex);
			
			i.setClassName(chosenService.packageName, chosenService.packageName + "." + chosenService.applicationInfo.loadLabel(getPackageManager()));
			Log.d(TAG, chosenService.packageName + "." + chosenService.applicationInfo.loadLabel(getPackageManager()));
			bindService(i, serviceConnection, Context.BIND_AUTO_CREATE);
		}
		
		private void doUnbind()
		{
			if(boundToService)
			{
				unbindService(serviceConnection);
				
				// tell service i am offline
				Message msg = Message.obtain(null, MOWSchnittstellenInterface.MESSAGE_5_CLOSE);
				msg.replyTo = messengerActivity;
				try{
					messengerService.send(msg);
				}
				catch(RemoteException e){
					Log.e(TAG, e.getClass().getName() + ": " + e.getMessage());
				}
				boundToService=false;
			}
		}
		
		private class MyServiceConnection implements ServiceConnection
		{
			public void onServiceConnected(ComponentName name, IBinder service)
			{
				messengerService = new Messenger(service);
				
				// make myself public to the service
				Message msg = Message.obtain(null, MOWSchnittstellenInterface.MESSAGE_I_REGISTER);
				msg.replyTo = messengerActivity;
				try{
					messengerService.send(msg);
				}
				catch(RemoteException e){
					Log.e(TAG, e.getClass().getName() + ": " + e.getMessage());
				}
				boundToService=true;
			}

			public void onServiceDisconnected(ComponentName name)
			{
				messengerService=null;
				boundToService=false;
			}
		}
	
	private class MessageHandler extends Handler{	
    	
    	@Override
    	public void handleMessage(Message msg){
    		switch(msg.what){
    			//start connection procedure
    			case MOWSchnittstellenInterface.MESSAGE_2_CONNECTION:
    								
    				bundleMessage2 = msg.getData();
    				bundleMessage2.setClassLoader(getClassLoader());
    				
    				int playerCount = bundleMessage2.getInt(MOWSchnittstellenInterface.CONNECTION_PLAYERCOUNT);
    				String playerNames = bundleMessage2.getString(MOWSchnittstellenInterface.CONNECTION_PLAYERNAMES);
    				int myId = bundleMessage2.getInt(MOWSchnittstellenInterface.CONNECTION_OWN_ID);
    				String activity = bundleMessage2.getString(MOWSchnittstellenInterface.CONNECTION_ACTIVITY);
    				
    				Log.v("test", playerCount + " - " + playerNames + " - " + myId + " - " + activity );
    				
    				Intent i = new Intent(); 
 
    				PackageInfo chosenService = availableServices.get(chosenServiceIndex);
    		        i.setClassName(chosenService.packageName, activity);
    		        startActivity(i);
    		        break;
    			//received message handling
    			case MOWSchnittstellenInterface.MESSAGE_4_GET:
    				
    				Bundle bundleMessage4 = msg.getData();

    				int id4 = bundleMessage4.getInt(MOWSchnittstellenInterface.GET_ADDRESS);
    				String typ = bundleMessage4.getString(MOWSchnittstellenInterface.GET_TYPE);
    				byte[] data = bundleMessage4.getByteArray(MOWSchnittstellenInterface.GET_DATA);
    				String dataString = new String(data);
    				
    				//game ready trigger
    				if(dataString.equals(GlobalVars.TRIGGER_READY) && typ.equals("gameTrigger")){
    					
    					MPOverlayText.setText("Opponent is ready.");
    					MPOverlayText.setTextColor(getResources().getColor(R.color.text_green));
    					glob.setOpponentReady(true);
    					gameReadyToStart();
    				}
    				//bomb trigger
    				else if(dataString.equals(GlobalVars.TRIGGER_BOMB) && typ.equals("gameTrigger")){
    					gv.setBomb();
    				}
    				//death trigger
    				else if(dataString.equals(GlobalVars.TRIGGER_DEAD) && typ.equals("gameTrigger")){
    					changeActivity(1);
    				}
    				
    				break;
    			case MOWSchnittstellenInterface.MESSAGE_6_CLOSED:
    				
    				/*Bundle bundle6=msg.getData();

    				int id6 = bundle6.getInt(MOWSchnittstellenInterface.GET_ADDRESS);*/
    				break;
    			default:
    				super.handleMessage(msg);
    		}
    	}
    }
	
	//send nessages
	public void sendMessage(String trigger){
		Message msg = Message.obtain(null, MOWSchnittstellenInterface.MESSAGE_3_SEND);
		Bundle bundleSend = new Bundle();
		//only broadcast for now
		bundleSend.putInt(MOWSchnittstellenInterface.SEND_ADDRESS, -1);
		bundleSend.putString(MOWSchnittstellenInterface.SEND_TYPE, "gameTrigger");
		bundleSend.putByteArray(MOWSchnittstellenInterface.SEND_DATA, trigger.getBytes());
		msg.setData(bundleSend);
		msg.replyTo = messengerActivity;
		try {
			messengerService.send(msg);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	//------------------------------------------------------------------------------------------------
	//------------------------------------------------------------------------------------------------
	
	
	//when closing game go back to previous activity + pause player
	protected void onPause()  { 
		 super.onPause();
		 glob.getPlayer().pause();
		 
	}
	
	//start music player when resumed
	public void onResume() {
		super.onResume();
		glob.getPlayer().start();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_game_screen, menu);
		return true;
	}
	
	@Override
	public void onBackPressed() {
		//super.onBackPressed();
		//gv.endThread();
		//finish();
		if(glob.isMp()) {
			finish();
			sendMessage(GlobalVars.TRIGGER_DEAD);
		}
	}
	
	@Override
	protected void onStop(){
		super.onStop();		
	}
	
	@Override
	protected void onStart(){
		super.onStart();
		
	}
	
/*	@Override
    protected void onDestroy() {
		//Log.v("kjhkj", "kjgjk");
    }*/

}
