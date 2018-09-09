package de.thm.asteroidshooter.Objects;

import de.thm.asteroidshooter.Global.GlobalFun;
import de.thm.asteroidshooter.Global.GlobalVars;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

public class Ship {

	private Bitmap bitmap;									//image of ship
	private int x;											//x position
	private int y;											//y position
	private int xSpeed = 0;									//x speed
	private int ySpeed = 0;									//y speed
	private int xSpeedVec = 0;								//vector for x speed
	private int ySpeedVec = 0;								//vector for y speed
	private int angle = 0;									//angel of ship
	private int angleX;										//angel x vec
	private int angleY;										//angel y vec
	private GlobalVars glob = GlobalVars.getInstance();		//singelton pattern
	private int HP = 3;										//hp of ship
	private int shield = 3;									//shield of ship
	private Bitmap hpImage;									//image of hp
	private Bitmap shieldImage;								//image of shield
	private long speedChangeTime = 0;						//when did i last change the speed

	public static int SHIP_COL = 1;							//trigger for collision
	public static int SHIP_DEST = 2;						//trigger for destroyed
	public static int SHIP_MISS = 0;						//trigger for missed
	
	public Ship() {
		//position of ship middle of screen, sets image of ship
		x = (glob.getScreenDimX() - glob.getObjDim())/2;
		y = (glob.getScreenDimY() - glob.getObjDim())/2;
		this.bitmap = GlobalFun.getResizedBitmap(glob.getShip(), glob.getObjDim(), glob.getObjDim());
		
		//sets HP and shield images for UI
		setHP();
	}
	
	//draws the UI, HP and shield
	public void drawUI(Canvas canvas){	
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setFilterBitmap(true);
		paint.setDither(true);
		paint.setAlpha(GlobalVars.UI_ALPHA);
		canvas.drawBitmap(shieldImage, glob.getUIx() , glob.getUIy() , paint);
		canvas.drawBitmap(hpImage, glob.getUIx() , glob.getUIy()+glob.getUIheight() + 5 , paint);
	}
	
	//the right image for HP and shield
	private void setHP(){
		if(HP == 1) 		hpImage = GlobalFun.getResizedBitmap(glob.getHp1(), glob.getUIheight(), glob.getUIwidthHP());
		else if(HP == 2) 	hpImage = GlobalFun.getResizedBitmap(glob.getHp2(), glob.getUIheight(), glob.getUIwidthHP());
		else if(HP == 3) 	hpImage = GlobalFun.getResizedBitmap(glob.getHp3(), glob.getUIheight(), glob.getUIwidthHP());
		else if(HP == 0)	hpImage = Bitmap.createBitmap(glob.getUIwidthHP(), glob.getUIheight(), Bitmap.Config.ARGB_8888);
		
		if(shield == 1)			shieldImage = GlobalFun.getResizedBitmap(glob.getShield1(), glob.getUIheight(), glob.getUIwidthSHIELD());
		else if(shield == 2)	shieldImage = GlobalFun.getResizedBitmap(glob.getShield2(), glob.getUIheight(), glob.getUIwidthSHIELD());
		else if(shield == 3)	shieldImage = GlobalFun.getResizedBitmap(glob.getShield3(), glob.getUIheight(), glob.getUIwidthSHIELD());
		else if(shield == 0)	shieldImage = Bitmap.createBitmap(glob.getUIwidthSHIELD(), glob.getUIheight(), Bitmap.Config.ARGB_8888);
	}
	
	//increase shield when power up was picked up
	public void increaseShield(){
		if(shield < 3){
			shield++;
			setHP();
		}
	}
	
	//set angle of ship
	public void setAngle(int x, int y){
		angleX = x;
		angleY = y;
		
		//converts cartesian to polars (degree)
		if(!(x == 0 && y == 0)){
			double radius = Math.sqrt( (double)x * (double)x + (double)y * (double)y );
			double angleInRadians = 0;
			
			if(y > 0) angleInRadians = Math.toDegrees(Math.acos(x / radius));
			else angleInRadians = Math.toDegrees(Math.acos(-x / radius)) + 180;

			//turn around degree 90 degree
			angleInRadians = angleInRadians + 90;
			
			if(angleInRadians >= 360) angleInRadians = ((angleInRadians/360)-1)*360;
			angle = (int) Math.round(angleInRadians);
		}
	}

	//draws the ship on the canvas
	public void draw(Canvas canvas) {
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setFilterBitmap(true);
		paint.setDither(true);
		canvas.save();
		canvas.rotate(angle, x + (bitmap.getWidth() / 2), y + (bitmap.getHeight() / 2));
		canvas.drawBitmap(bitmap, x , y , paint);
		canvas.restore();
		
		//every time ship is drawn, draw also UI
		drawUI(canvas);
	}

	//updates ship position, moves it
	public void update() {
		
		//calculates speed every SHIP_CHANGE_SPEED_TIME interval, let the ship behave like in space
		if(System.currentTimeMillis() - speedChangeTime > GlobalVars.SHIP_CHANGE_SPEED_TIME){
			speedChangeTime = System.currentTimeMillis();
			
			xSpeed = (int)((double)xSpeed + Math.round(2*(double)xSpeedVec/(double)glob.getLeftJoyStickPrecission()));
			ySpeed = (int)((double)ySpeed + Math.round(2*(double)ySpeedVec/(double)glob.getLeftJoyStickPrecission()));
			
			if(xSpeed > glob.getLeftJoyStickPrecission()) xSpeed = glob.getLeftJoyStickPrecission();
			if(ySpeed > glob.getLeftJoyStickPrecission()) ySpeed = glob.getLeftJoyStickPrecission();
			if(xSpeed < -glob.getLeftJoyStickPrecission()) xSpeed = -glob.getLeftJoyStickPrecission();
			if(ySpeed < -glob.getLeftJoyStickPrecission()) ySpeed = -glob.getLeftJoyStickPrecission();
		}
		
		//sets the position
		if(x + xSpeed < 0)x = 0;
		else if (x + xSpeed > glob.getScreenDimX() - bitmap.getWidth()) x = glob.getScreenDimX() - bitmap.getWidth();
		else x += xSpeed; 
		
		if(y + ySpeed < 0) y = 0;
		else if (y + ySpeed > glob.getScreenDimY() - bitmap.getHeight()) y = glob.getScreenDimY() - bitmap.getHeight();
		else y += ySpeed;			
	}
	
	//detects colission with asteroid
	public int collision(Asteroid aster){
		//distance between object and ship
		double xDistance = (double)((x+glob.getObjDim()/2) - (aster.getX()+glob.getObjDim()/2));
		double yDistance = (double)((y+glob.getObjDim()/2) - (aster.getY()+glob.getObjDim()/2));
		
		//pythagoras, if hit
		if(Math.sqrt(xDistance*xDistance + yDistance*yDistance) < glob.getObjDim()){
			if(shield != 0) shield--;	//first decrease shield
			else HP--;					//then decrease HP
			
			setHP();					//updates UI images
			
			if(HP <= 0) return SHIP_DEST;	//if HP 0, ship is destroyed
			
			return SHIP_COL;				//else only collision
		}
		return SHIP_MISS;					//nothing happened at all
	}
	
	//------------------------------------------------------------------------------------------------
	//------------------------------------------------------------------------------------------------
	//								simple getter and setter
	//------------------------------------------------------------------------------------------------
	//------------------------------------------------------------------------------------------------
	public int getX() {return x;}
	public void setX(int x) {this.x = x;
	}
	public int getY() {return y;}
	public void setY(int y) {this.y = y;}
	
	public void setSpeed(int x, int y){
		xSpeedVec = x;
		ySpeedVec = y;
	}
	
	public int getAngleX() {return angleX;}
	public void setAngleX(int angleX) {this.angleX = angleX;}

	public int getAngleY() {return angleY;}
	public void setAngleY(int angelY) {this.angleY = angelY;}

	public int getAngle() {return angle;}
	public void setAngle(int angle) {this.angle = angle;}

	public int getHP() {
		return HP;
	}

	public void setHP(int hP) {
		HP = hP;
	}
}
