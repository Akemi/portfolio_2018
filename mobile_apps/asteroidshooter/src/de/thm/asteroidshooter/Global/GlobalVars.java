package de.thm.asteroidshooter.Global;

import android.graphics.Bitmap;
import android.media.MediaPlayer;

public class GlobalVars{
	private static GlobalVars instance;

	//static variables in pixel for standard horizontal resolution 1024
	private static int OBJECT_DIM = 40;						//size of all objects, ship, asteroids, poweups
	private static int BULLET_DIM = 10;						//size of bullets
	private static int UI_HEIGHT = 23;						//hight of UI
	private static int UI_WIDTH_HP = 234;					//width of HP ui
	private static int UI_WIDTH_SHIELD = 289;				//width of shield ui
	private static int UI_HEIGHT_BOMB = 55;					//high of bomb ui
	private static int UI_WIDTH_BOMB = 75;					//width of bobm ui
	private static int UI_X = 25;							//x position of ui
	private static int UI_Y = 20;							//y position of ui
	private static int UI_FONT_SIZE = 60;					//font size of ui
	private static int COUNT_FONT_SIZE = 170;				//font size of counter
	private static int MAX_ASTEROID_SPEED = 10;				//max asteroid speed
	private static int MIN_ASTEROID_SPEED = 2;				//min asteroid speed
	private static int INCREASE_ASTEROID_SPEED = 2;			//speed asteroid increases over time
	private static int MAX_POWERUP_SPEED = 7;				//max speed of powerups
	private static int MAX_BULLET_SPEED = 13;				//max speed of bullets
	private static int LEFT_JOYSTICK_PRECISSION = 10;		//precission of left joystick
	private static int BG_OFFSET = 30;						//background offset for perpective view
	
	//scaled variables for destination resolution
	private double pixelScaleFac;							//scale factor for target device resolution
	private int ObjDim;										//OBJECT_DIM scaled to right resolution
	private int BulletDim;									//BULLET_DIM scaled to right resolution
	private int UIheight;									//UI_HEIGHT scaled to right resolution
	private int UIwidthHP;									//UI_WIDTH_HP scaled to right resolution
	private int UIwidthSHIELD;								//UI_WIDTH_SHIELD scaled to right resolution
	private int UIheightBomb;								//UI_HEIGHT_BOMB scaled to right resolution
	private int UiwidthBomb;								//UI_WIDTH_BOMB scaled to right resolution
	private int UIx;										//UI_X scaled to right resolution
	private int UIy;										//UI_Y scaled to right resolution
	private int UIFontSize;									//UI_FONT_SIZE scaled to right resolution
	private int CountFontSize;								//COUNT_FONT_SIZE scaled to right resolution
	private int MaxAsteroidSpeed;							//MAX_ASTEROID_SPEED scaled to right resolution
	private int MinAsteroidSpeed;							//MAX_ASTEROID_SPEED scaled to right resolution
	private int IncreaseAsteroidSpeed;						//INCREASE_ASTEROID_SPEED scaled to right resolution
	private int MaxPowerUPSpeed;							//MAX_POWERUP_SPEED scaled to right resolution
	private int MaxBulletSpeed;								//MAX_BULLET_SPEED scaled to right resolution
	private int LeftJoyStickPrecission;						//LEFT_JOYSTICK_PRECISSION scaled to right resolution
	private int BgOffset;									//BG_OFFSET scaled to right resolution
	
	//static variables independent from resolution
	public static int MAX_ASTEROID_DEGREE = 90;				//max degree asteroids fly from spawn point
	public static int MAX_POWERUP_DEGREE = 45;				//max dagree powerups fly from spawn point
	public static int MAX_ASTEROID_ROTATION = 5;			//may rotation speed of asteroids
	public static int ASTEROID_INCREASE_TIME = 30000;		//time asteroids Max count increase
	public static int ASTEROID_INCREASE_SPEED_TIME = 40000;	//time asteroids get faster
	public static int COUNTER_TIME = 1000;					//counter interval time
	public static int BULLET_ADD_TIME = 500;				//time bullets are added when shooting
	public static int ASTEROID_ADD_TIME = 400;				//time asteroids will be added when not max count
	public static int POWERUP_ADD_TIME = 20000;				//time when power ups will be added
	public static int POINTS_ADD_TIME = 1000;				//time point counter increases
	public static int SHIP_CHANGE_SPEED_TIME = 100;			//time ship changes its speed
	public static int UI_ALPHA = 170;						//opacity of the ui 255 max
	public static int ASTEROID_POINTS = 10;					//how many point i get for destroyed asteroid
	public static int NORMAL_POINTS = 1;					//point is get over time
	public static int ASTEROID_PER_INTERVAL = 2;			//how many asteroids can be added at once
	public static int RIGHT_JOYSTICK_PRECISSION = 30;		//precission of th eright joystick

	//debug UI elements
	public static boolean DEBUG = false;					//enable debug mode
	
	//MP or SP
	private boolean mp;										//variable if MP
	public static String TRIGGER_BOMB = "1";				//MP trigger for bomb
	public static String TRIGGER_READY = "0";				//MP trigger for ready
	public static String TRIGGER_DEAD = "2";				//MP trigger for dead
	public static int OPPONENT_SENT_ASTEROIDS = 10;			//number of asteroids which can be send
	private boolean opponentReady = false;					//variable if opponent is ready
	private boolean yourselfReady = false;					//variable if self is ready

	//variable for asteroid
	private int asteroidNum = 10;							//number of starting asteroids
	
	//screen dimensions and off-screen borders
	private int ScreenDimX;									//screen dim x
	private int ScreenDimY;									//screen dim y
	private int borderLeft;									//left game border, out of screen
	private int borderRight;								//right game border, out of screen
	private int borderTop;									//top game border, out of screen
	private int borderBottom;								//bottom game border, out of screen

	//all the images
	private Bitmap shield1;									//image for shield at 1
	private Bitmap shield2;									//image for shield at 2
	private Bitmap shield3;									//image for shield at 3
	private Bitmap hp1;										//image for hp at 1
	private Bitmap hp2;										//image for hp at 2
	private Bitmap hp3;										//image for hp at 3
	private Bitmap Ship;									//image for ship
	private Bitmap Background;								//image for background
	private Bitmap Asteroid1;								//image for asteroid 1
	private Bitmap Asteroid2;								//image for asteroid 2
	private Bitmap Asteroid3;								//image for asteroid 3
	private Bitmap Bullet;									//image for bullet
	private Bitmap PowerUP1;								//image for powerup 1
	private Bitmap PowerUP2;								//image for powerup 2
	private Bitmap bombui;									//image for bombui top center
	
	//background music
	private MediaPlayer player = null;						//music player for background music

	
	//scaling set method, scales everything relevant relative to 1024 horizontal pixels
	public void setPixelScaleFac(double pixelScaleFac) {
		this.pixelScaleFac = pixelScaleFac;
		
		ObjDim = (int)(pixelScaleFac * OBJECT_DIM);
		BulletDim = (int)(pixelScaleFac * BULLET_DIM);
		UIheight = (int)(pixelScaleFac * UI_HEIGHT);
		UIwidthHP = (int)(pixelScaleFac * UI_WIDTH_HP);
		UIwidthSHIELD = (int)(pixelScaleFac * UI_WIDTH_SHIELD);
		UIheightBomb = (int)(pixelScaleFac * UI_HEIGHT_BOMB);
		UiwidthBomb = (int)(pixelScaleFac * UI_WIDTH_BOMB);
		UIx = (int)(pixelScaleFac * UI_X);
		UIy = (int)(pixelScaleFac * UI_Y);
		UIFontSize = (int)(pixelScaleFac * UI_FONT_SIZE);
		CountFontSize = (int)(pixelScaleFac * COUNT_FONT_SIZE);
		MaxAsteroidSpeed = (int)(pixelScaleFac * MAX_ASTEROID_SPEED);
		MinAsteroidSpeed = (int)(pixelScaleFac * MIN_ASTEROID_SPEED);
		IncreaseAsteroidSpeed = (int)(pixelScaleFac * INCREASE_ASTEROID_SPEED);
		MaxPowerUPSpeed = (int)(pixelScaleFac * MAX_POWERUP_SPEED);
		MaxBulletSpeed = (int)(pixelScaleFac * MAX_BULLET_SPEED);
		LeftJoyStickPrecission = (int)(pixelScaleFac * LEFT_JOYSTICK_PRECISSION);
		BgOffset = (int)(pixelScaleFac * BG_OFFSET);
	}
	
	//increases asteroid speed
	public void increaseAsteroidSpeed(){
		MaxAsteroidSpeed = MaxAsteroidSpeed + IncreaseAsteroidSpeed;
		MinAsteroidSpeed = MinAsteroidSpeed + IncreaseAsteroidSpeed;
	}

	//Restrict the constructor from being instantiated
	private GlobalVars(){}
	
	//singleton pattern
	public static synchronized GlobalVars getInstance(){
		if(instance == null){
			instance = new GlobalVars();
		}
		return instance;
	}

	
	//------------------------------------------------------------------------------------------------
	//------------------------------------------------------------------------------------------------
	//								simple getter and setter
	//------------------------------------------------------------------------------------------------
	//------------------------------------------------------------------------------------------------
	public void setShip(Bitmap shipImg){
		Ship = shipImg;
	}
	public Bitmap getShip(){
		return Ship;
	}

	public int getScreenDimX() {
		return ScreenDimX;
	}

	public void setScreenDimX(int screenDimX) {
		ScreenDimX = screenDimX;
	}

	public int getScreenDimY() {
		return ScreenDimY;
	}

	public void setScreenDimY(int screenDimY) {
		ScreenDimY = screenDimY;
	}

	public Bitmap getBackground() {
		return Background;
	}

	public void setBackground(Bitmap background) {
		Background = background;
	}

	public int getBorderLeft() {
		return borderLeft;
	}

	public void setBorderLeft(int borderLeft) {
		this.borderLeft = borderLeft;
	}

	public int getBorderRight() {
		return borderRight;
	}

	public void setBorderRight(int borderRight) {
		this.borderRight = borderRight;
	}

	public int getBorderTop() {
		return borderTop;
	}

	public void setBorderTop(int borderTop) {
		this.borderTop = borderTop;
	}

	public int getBorderBottom() {
		return borderBottom;
	}

	public void setBorderBottom(int borderBottom) {
		this.borderBottom = borderBottom;
	}

	public int getAsteroidNum() {
		return asteroidNum;
	}

	public void setAsteroidNum(int asteroidNum) {
		this.asteroidNum = asteroidNum;
	}

	public Bitmap getBullet() {
		return Bullet;
	}

	public void setBullet(Bitmap bullet) {
		Bullet = bullet;
	}

	public Bitmap getShield1() {
		return shield1;
	}

	public void setShield1(Bitmap shield1) {
		this.shield1 = shield1;
	}

	public Bitmap getShield2() {
		return shield2;
	}

	public void setShield2(Bitmap shield2) {
		this.shield2 = shield2;
	}

	public Bitmap getShield3() {
		return shield3;
	}

	public void setShield3(Bitmap shield3) {
		this.shield3 = shield3;
	}

	public Bitmap getHp1() {
		return hp1;
	}

	public void setHp1(Bitmap hp1) {
		this.hp1 = hp1;
	}

	public Bitmap getHp2() {
		return hp2;
	}

	public void setHp2(Bitmap hp2) {
		this.hp2 = hp2;
	}

	public Bitmap getHp3() {
		return hp3;
	}

	public void setHp3(Bitmap hp3) {
		this.hp3 = hp3;
	}

	public Bitmap getPowerUP1() {
		return PowerUP1;
	}

	public void setPowerUP1(Bitmap powerUP1) {
		PowerUP1 = powerUP1;
	}

	public Bitmap getPowerUP2() {
		return PowerUP2;
	}

	public void setPowerUP2(Bitmap powerUP2) {
		PowerUP2 = powerUP2;
	}

	public double getPixelScaleFac() {
		return pixelScaleFac;
	}

	public int getObjDim() {
		return ObjDim;
	}

	public void setObjDim(int objDim) {
		ObjDim = objDim;
	}

	public int getBulletDim() {
		return BulletDim;
	}

	public void setBulletDim(int bulletDim) {
		BulletDim = bulletDim;
	}

	public int getUIheight() {
		return UIheight;
	}

	public void setUIheight(int uIheight) {
		UIheight = uIheight;
	}

	public int getUIwidthHP() {
		return UIwidthHP;
	}

	public void setUIwidthHP(int uIwidthHP) {
		UIwidthHP = uIwidthHP;
	}

	public int getUIwidthSHIELD() {
		return UIwidthSHIELD;
	}

	public void setUIwidthSHIELD(int uIwidthSHIELD) {
		UIwidthSHIELD = uIwidthSHIELD;
	}

	public int getUIx() {
		return UIx;
	}

	public void setUIx(int uIx) {
		UIx = uIx;
	}

	public int getUIy() {
		return UIy;
	}

	public void setUIy(int uIy) {
		UIy = uIy;
	}

	public int getUIFontSize() {
		return UIFontSize;
	}

	public void setUIFontSize(int uIFontSize) {
		UIFontSize = uIFontSize;
	}

	public int getMaxAsteroidSpeed() {
		return MaxAsteroidSpeed;
	}

	public void setMaxAsteroidSpeed(int maxAsteroidSpeed) {
		MaxAsteroidSpeed = maxAsteroidSpeed;
	}

	public int getMaxPowerUPSpeed() {
		return MaxPowerUPSpeed;
	}

	public void setMaxPowerUPSpeed(int maxPowerUPSpeed) {
		MaxPowerUPSpeed = maxPowerUPSpeed;
	}

	public int getMaxBulletSpeed() {
		return MaxBulletSpeed;
	}

	public void setMaxBulletSpeed(int maxBulletSpeed) {
		MaxBulletSpeed = maxBulletSpeed;
	}

	public int getLeftJoyStickPrecission() {
		return LeftJoyStickPrecission;
	}

	public void setLeftJoyStickPrecission(int leftJoyStickPrecission) {
		LeftJoyStickPrecission = leftJoyStickPrecission;
	}

	public Bitmap getAsteroid1() {
		return Asteroid1;
	}

	public void setAsteroid1(Bitmap asteroid1) {
		Asteroid1 = asteroid1;
	}

	public Bitmap getAsteroid2() {
		return Asteroid2;
	}

	public void setAsteroid2(Bitmap asteroid2) {
		Asteroid2 = asteroid2;
	}

	public Bitmap getAsteroid3() {
		return Asteroid3;
	}

	public void setAsteroid3(Bitmap asteroid3) {
		Asteroid3 = asteroid3;
	}

	public int getMinAsteroidSpeed() {
		return MinAsteroidSpeed;
	}

	public void setMinAsteroidSpeed(int minAsteroidSpeed) {
		MinAsteroidSpeed = minAsteroidSpeed;
	}

	public Bitmap getBombui() {
		return bombui;
	}

	public void setBombui(Bitmap bombui) {
		this.bombui = bombui;
	}

	public int getUIheightBomb() {
		return UIheightBomb;
	}

	public void setUIheightBomb(int uIheightBomb) {
		UIheightBomb = uIheightBomb;
	}

	public int getUiwidthBomb() {
		return UiwidthBomb;
	}

	public void setUiwidthBomb(int uiwidthBomb) {
		UiwidthBomb = uiwidthBomb;
	}

	public MediaPlayer getPlayer() {
		return player;
	}

	public void setPlayer(MediaPlayer player) {
		this.player = player;
	}

	public int getCountFontSize() {
		return CountFontSize;
	}

	public void setCountFontSize(int countFontSize) {
		CountFontSize = countFontSize;
	}

	public boolean isMp() {
		return mp;
	}

	public void setMp(boolean mp) {
		this.mp = mp;
	}

	public boolean isOpponentReady() {
		return opponentReady;
	}

	public void setOpponentReady(boolean opponentReady) {
		this.opponentReady = opponentReady;
	}

	public boolean isYourselfReady() {
		return yourselfReady;
	}

	public void setYourselfReady(boolean yourselfReady) {
		this.yourselfReady = yourselfReady;
	}

	public int getBgOffset() {
		return BgOffset;
	}

	public void setBgOffset(int bgOffset) {
		BgOffset = bgOffset;
	}


}
