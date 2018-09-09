package de.thm.asteroidshooter.Game;

import java.util.ArrayList;
import java.util.List;

import de.thm.asteroidshooter.GameScreen;
import de.thm.asteroidshooter.R;
import de.thm.asteroidshooter.Global.GlobalFun;
import de.thm.asteroidshooter.Global.GlobalVars;
import de.thm.asteroidshooter.Joystick.DualJoystickView;
import de.thm.asteroidshooter.Joystick.JoystickMovedListener;
import de.thm.asteroidshooter.Objects.Asteroid;
import de.thm.asteroidshooter.Objects.Bullet;
import de.thm.asteroidshooter.Objects.PowerUP;
import de.thm.asteroidshooter.Objects.Ship;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;


public class GameView extends SurfaceView implements SurfaceHolder.Callback {
	//debug, view and variable object
	private GameThread gameThread;							//game thread instanz
	private DualJoystickView dualJoystick;					//dualjoystick view for listener
	private GlobalVars glob = GlobalVars.getInstance();		//singelton pattern variables
	private String fps;										//fps for debug
	
	//objects
	private static Ship ship;								//my ship
	private List<Asteroid> asteroidList;					//all asteroids
	private List<Bullet> bulletList;						//all bullets the ship shoots
	private List<PowerUP> powerUPList;						//all spawning powerups
	//timer, time between two checks
	private long AsteroidBeginTime;							//asteroid timer
	private long BulletBeginTime;							//bullet timer
	private long AsteroidAddTime;							//asteroid add timer
	private long PowerUpBeginTime;							//poerup timer
	private long PointsBeginTime;							//points timer
	private long AsteroidSpeedTime;							//asteroid speed time
	
	//game variables
	private boolean bulletshoot = false;					//did i shoot?
	private int points;										//my points
	private boolean bomb = false;							//do i have a bomb?
	private boolean bombSet = false;						//bomb is set
	
	private Context con;

	public GameView(Context context, DualJoystickView dJ) {
		super(context);
		
		con = context;
		
		//get screen size
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		
		//set all the initial variables for the singleton pattern
		//VERY IMPORTANT, SEE DESCRIPTION IN GlobVars SINGLETON PATTER
		glob.setShip(BitmapFactory.decodeResource(getResources(), R.drawable.ship2));
		glob.setAsteroid1(BitmapFactory.decodeResource(getResources(), R.drawable.asteroid1));
		glob.setAsteroid2(BitmapFactory.decodeResource(getResources(), R.drawable.asteroid2));
		glob.setAsteroid3(BitmapFactory.decodeResource(getResources(), R.drawable.asteroid3));
		glob.setBullet(BitmapFactory.decodeResource(getResources(), R.drawable.bullet));
		glob.setHp1(BitmapFactory.decodeResource(getResources(), R.drawable.green1));
		glob.setHp2(BitmapFactory.decodeResource(getResources(), R.drawable.green2));
		glob.setHp3(BitmapFactory.decodeResource(getResources(), R.drawable.green3));
		glob.setShield1(BitmapFactory.decodeResource(getResources(), R.drawable.blue1));
		glob.setShield2(BitmapFactory.decodeResource(getResources(), R.drawable.blue2));
		glob.setShield3(BitmapFactory.decodeResource(getResources(), R.drawable.blue3));
		glob.setPowerUP1(BitmapFactory.decodeResource(getResources(), R.drawable.powerup1));
		glob.setPowerUP2(BitmapFactory.decodeResource(getResources(), R.drawable.powerup2));
		glob.setBombui(BitmapFactory.decodeResource(getResources(), R.drawable.bombui));
		glob.setPixelScaleFac((double)size.x/(double)1024);
		glob.setBackground(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.bg), size.x + glob.getBgOffset(), size.y + glob.getBgOffset(), true));
		glob.setScreenDimX(size.x);
		glob.setScreenDimY(size.y);
		glob.setBorderLeft(0 - 2*glob.getObjDim());
		glob.setBorderRight(size.x + 2*glob.getObjDim());
		glob.setBorderTop(0 - 2*glob.getObjDim());
		glob.setBorderBottom(size.y + 2*glob.getObjDim());
		glob.setAsteroidNum(10);
		
		//adding the callback (this) to the surface holder to intercept events
		getHolder().addCallback(this);

		//adds listener to joystick
		dualJoystick = dJ;
		dualJoystick.setOnJostickMovedListener(leftJoystickListener, rightJoystickListener);

		//create the game loop thread
		gameThread = new GameThread(getHolder(), this);
		
		//make the GameView focusable so it can handle events
		setFocusable(true);	
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
	
	}

	//when the surface of the games is created
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if(gameThread == null){
			gameThread = new GameThread(getHolder(), this);
			
			//set initial times for checking when to add what
			setInitTimes();

			//start game loop
			gameThread.setRunning(true);
			gameThread.start();
			if(glob.isMp() && (!glob.isOpponentReady() || !glob.isYourselfReady())) gameThread.setPause(true);
			else gameThread.setPause(false);
		}
		else {
			//sets games objects and variables
			ship = new Ship();
			points = 0;
			asteroidList = new ArrayList<Asteroid>();
			bulletList = new ArrayList<Bullet>();
			powerUPList = new ArrayList<PowerUP>();
			
			//adds initial asteroids
			for(int i=0; i < glob.getAsteroidNum(); i++){
				asteroidList.add(new Asteroid());
			}
			
			//set initial times for checking when to ad what
			setInitTimes();

			//start game loop
			gameThread.setRunning(true);
			if(!gameThread.isAlive())gameThread.start();
			if(glob.isMp()) gameThread.setPause(true);
			else  gameThread.setPause(false);
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		//shut down thread and wait till finished
		endThread();
	}
	
	public void endThread(){	
		gameThread.setRunning(false);
		boolean retry = true;
		while (retry) {
			try {
				gameThread.join();
				gameThread = null;
				retry = false;
			} catch (InterruptedException e) {
			}
		}
	}

	//render method, called by thread
	public void render(Canvas canvas) {
		//draws background
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setFilterBitmap(true);
		paint.setDither(true);

		//draw background with some offset, inverse of ship properly scaled
		int offsetX = - (int)(((double)ship.getX()/(double)glob.getScreenDimX()) * (double)glob.getBgOffset());
		int offsetY = - (int)(((double)ship.getY()/(double)glob.getScreenDimY()) * (double)glob.getBgOffset());
		canvas.drawBitmap(glob.getBackground(), offsetX , offsetY , paint);
		
		//draws bullets
		for(int i = 0; i < bulletList.size(); i++) bulletList.get(i).draw(canvas);
		
		//draws powerup
		for(int i = 0; i < powerUPList.size(); i++) powerUPList.get(i).draw(canvas);
		
		//draws asteroids
		for(int i = 0; i < asteroidList.size(); i++) asteroidList.get(i).draw(canvas);
		
		//draws ship
		ship.draw(canvas);
		
		//draws UI, points and bomb
		drawUI(canvas);
		
		//show fps when debug is true
		if(GlobalVars.DEBUG == true){
			displayFps(canvas, fps);
		}
	}
	
	//draws the ui, bomb icon middle and points counter
	private void drawUI(Canvas canvas) {
		Paint paint = new Paint();
		paint.setARGB(255, 255, 255, 255);
		paint.setAlpha(GlobalVars.UI_ALPHA);
		paint.setAntiAlias(true);
		paint.setFilterBitmap(true);
		paint.setDither(true);
		paint.setTextSize(glob.getUIFontSize());
		paint.setTextAlign(Align.RIGHT);
		//draws points counter
		canvas.drawText(Integer.toString(points), this.getWidth() - glob.getUIy(), glob.getUIFontSize(), paint);
		
		paint.setTextAlign(Align.CENTER);
		//draws bomb
		if(bomb == true) canvas.drawBitmap(GlobalFun.getResizedBitmap(glob.getBombui(), glob.getUIheightBomb(), glob.getUiwidthBomb()), this.getWidth()/2 , 0, paint);
	}
	
	//three seconds counter before the game begins
	public void drawCounter(Canvas canvas, int counter) {
		Paint paint = new Paint();
		paint.setARGB(255, 255, 255, 255);
		paint.setAlpha(GlobalVars.UI_ALPHA);
		paint.setAntiAlias(true);
		paint.setFilterBitmap(true);
		paint.setDither(true);
		paint.setTextSize(glob.getCountFontSize());
		paint.setTextAlign(Align.CENTER);
		canvas.drawText(Integer.toString(counter), glob.getScreenDimX()/2, glob.getScreenDimY()/2 + glob.getCountFontSize()/4, paint);
	}
	
	//updates game object states
	public void update() {
		//bomb is set
		if(bombSet == true){
			//if mp add asteroids sent from opponent
			if(glob.isMp()){
				if(asteroidList.size() < glob.getAsteroidNum() + GlobalVars.OPPONENT_SENT_ASTEROIDS){
					for(int i = 0; i < (glob.getAsteroidNum() + GlobalVars.OPPONENT_SENT_ASTEROIDS) - asteroidList.size(); i++){
						asteroidList.add(new Asteroid());
						//Log.v("aster", "aster sent");
					}
				}
			}
			//else it's SP and bomb true destroy asteroids
			else{
				if(bomb == true){
					points = points + asteroidList.size()*GlobalVars.ASTEROID_POINTS;	//sets points
					asteroidList.clear();												//kills all asteroids
					bomb = false;														//set bomb varibales back
				}
			}
			bombSet  = false;
		}
		else{
			bombSet = false;
		}
		
		//updates bullet position, if update false remove
		for(int i = 0; i < bulletList.size(); i++){
			if(bulletList.get(i).update() == false){
				bulletList.remove(i);
				i = 0;
			}
		}
		
		//updates powerUP position if available, if update false (border) remove
		for(int i = 0; i < powerUPList.size(); i++){
			if(powerUPList.get(i).update() == false){
				powerUPList.remove(i);
			}
		}
		
		//updates ship position
		ship.update();
		
		//updates asteroid position
		for(int i = 0; i < asteroidList.size(); i++){
			asteroidList.get(i).update();
		}
		
		//collision detection between bullets and asteroids
		for(int i = 0; i < bulletList.size(); i++){
			int hit = 0;
			for(int j = 0; j < asteroidList.size(); j++){
				if(bulletList.get(i).collision(asteroidList.get(j)) == true){ 	//asteroid destroyed
					asteroidList.remove(j);
					hit++;
					points = points + GlobalVars.ASTEROID_POINTS;				//add points
				}
			}
			//remove bullet after loop
			if(hit > 0){
				bulletList.remove(i);
				i = 0;
			}
		}
		
		//collision detection between PowerUPs and Ship
		for(int i = 0; i < powerUPList.size(); i++){
			int status = powerUPList.get(i).collision(ship);
			if(status == PowerUP.BOMB){				//if it's a bomb
				bomb = true;
				powerUPList.remove(i);
			}
			else if(status == PowerUP.SHIELD){		//if it's a shield
				ship.increaseShield();
				powerUPList.remove(i);
			}
		}
		
		//------------------------------------------------------------------------------------------------
		//								game ends here
		//------------------------------------------------------------------------------------------------
		//collision detection between ship and asteroids, only while ship is fine
		for(int i = 0; i < asteroidList.size() && ship.getHP() > 0; i++){
			int status = ship.collision(asteroidList.get(i));
			if(status == Ship.SHIP_COL){			//when collision
				asteroidList.remove(i);
			}
			else if(status == Ship.SHIP_DEST){		//when ship destroyed				
				gameEnd();
				break;
			}
		}
		//------------------------------------------------------------------------------------------------
		//------------------------------------------------------------------------------------------------
		
		//increases Speed of asteroids interval
		if(System.currentTimeMillis() - AsteroidSpeedTime > GlobalVars.ASTEROID_INCREASE_SPEED_TIME){
			AsteroidSpeedTime = System.currentTimeMillis();
			glob.increaseAsteroidSpeed();
		}
		
		//adds new PowerUP every interval POWERUP_ADD_TIME
		if(System.currentTimeMillis() - PowerUpBeginTime > GlobalVars.POWERUP_ADD_TIME){
			PowerUpBeginTime = System.currentTimeMillis();
			powerUPList.add(new PowerUP());
		}
		
		//increase global asteroid count every interval ASTEROID_INCREASE_TIME
		if(System.currentTimeMillis() - AsteroidBeginTime > GlobalVars.ASTEROID_INCREASE_TIME){
			AsteroidBeginTime = System.currentTimeMillis();
			glob.setAsteroidNum(glob.getAsteroidNum() + GlobalVars.ASTEROID_PER_INTERVAL);
		}
		
		//adds new asteroid every interval ASTEROID_ADD_TIME if max count is not reached 
		if(System.currentTimeMillis() - AsteroidAddTime > GlobalVars.ASTEROID_ADD_TIME){
			AsteroidAddTime = System.currentTimeMillis();
			if(asteroidList.size() < glob.getAsteroidNum()){
				asteroidList.add(new Asteroid());
				Log.v("aster", "added");
			}
		}
		
		//adds points every interval POINTS_ADD_TIME
		if(System.currentTimeMillis() - PointsBeginTime > GlobalVars.POINTS_ADD_TIME){
			PointsBeginTime = System.currentTimeMillis();
			points = points + GlobalVars.NORMAL_POINTS;
		}
		
		//add new bullets every interval BULLET_ADD_TIME
		if(System.currentTimeMillis() - BulletBeginTime > GlobalVars.BULLET_ADD_TIME && bulletshoot == true){
			BulletBeginTime = System.currentTimeMillis();
			bulletList.add(new Bullet(ship));
		}
		
	}
	
	//game End function
	private void gameEnd(){
		gameThread.setRunning(false);
		if(glob.isMp()){
			((GameScreen)con).changeActivity(0);
		}
		else{
			((GameScreen)con).changeActivity(points);
		}
	}
	
	//game Start function, needs to reset inital times
	public void gameStart(){
		setInitTimes();
		gameThread.setPause(false);
	}
	
	//game Pause function
	public void gamePause(){
		gameThread.setPause(true);
	}
	
	//set the bomb
	public void setBomb(){
		bombSet = true;	
	}
	
	//check if bomb is set
	public boolean isBombSet(){
		if(bomb == true){
			bomb = false;
			return true;
		}
		return false;
	}
	
	//sets inital times
	private void setInitTimes(){
		AsteroidBeginTime = System.currentTimeMillis();
		BulletBeginTime = System.currentTimeMillis();
		AsteroidAddTime = System.currentTimeMillis();
		PowerUpBeginTime = System.currentTimeMillis();
		PointsBeginTime = System.currentTimeMillis();
		AsteroidSpeedTime = System.currentTimeMillis();
	}
	
	//------------------------------------------------------------------------------------------------
	//------------------------------------------------------------------------------------------------
	//								joystick listener
	//------------------------------------------------------------------------------------------------
	//------------------------------------------------------------------------------------------------
	
	//left joystick
	private JoystickMovedListener leftJoystickListener = new JoystickMovedListener() {
		//sets speed for ship
		@Override
		public void OnMoved(int pan, int tilt) {
			ship.setSpeed(pan, tilt);
		}

		@Override
		public void OnReleased() {}
		public void OnReturnedToCenter() {};
	};
	
	//right joystick
	private JoystickMovedListener rightJoystickListener = new JoystickMovedListener() {
		//sets angel of ship, also sets bullet shoot varibale
		@Override
		public void OnMoved(int pan, int tilt) {
			ship.setAngle(pan, tilt);
			bulletshoot = true;
		}
		//ship doesn't shoot anymore
		@Override
		public void OnReleased() {
			bulletshoot = false;
		}
		
		public void OnReturnedToCenter() {
			bulletshoot = false;
		};
	};
	
	//------------------------------------------------------------------------------------------------
	//------------------------------------------------------------------------------------------------
	//								less important debug functions
	//------------------------------------------------------------------------------------------------
	//------------------------------------------------------------------------------------------------
	
	//displays fps
	private void displayFps(Canvas canvas, String fps) {
		if (canvas != null && fps != null) {
			Paint paint = new Paint();
			paint.setARGB(255, 255, 255, 255);
			canvas.drawText(fps, this.getWidth()/2, 20, paint);
		}
	}
	
	//sets fps from thread
	public void setAvgFps(String avgFps) {
		this.fps = avgFps;
	}

}
