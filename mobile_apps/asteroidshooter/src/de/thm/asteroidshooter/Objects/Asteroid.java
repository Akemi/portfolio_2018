package de.thm.asteroidshooter.Objects;

import java.util.Random;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import de.thm.asteroidshooter.Global.GlobalFun;
import de.thm.asteroidshooter.Global.GlobalVars;

public class Asteroid {
	
	private Bitmap bitmap;								//image of asteroid
	private int x;										//x position
	private int y;										//y position
	private int xSpeed = 1;								//x speed
	private int ySpeed = 1;								//y speed
	private int rotation;								//rotation speed
	private int angle = 0;								//angel of current asteroid
	private Random rnd = new Random();					//random
	private GlobalVars glob = GlobalVars.getInstance();	//singelton pattern
	
	public Asteroid() {
		initAsteroid();
	}
	
	public void initAsteroid(){
		//set random asteroid image
		int rndImg = rnd.nextInt(3);
		if(rndImg == 0) this.bitmap = GlobalFun.getResizedBitmap(glob.getAsteroid1(), glob.getObjDim(), glob.getObjDim());
		if(rndImg == 1) this.bitmap = GlobalFun.getResizedBitmap(glob.getAsteroid2(), glob.getObjDim(), glob.getObjDim());
		if(rndImg == 2) this.bitmap = GlobalFun.getResizedBitmap(glob.getAsteroid3(), glob.getObjDim(), glob.getObjDim());
		
		//set rotation
		rotation = rnd.nextInt(2*GlobalVars.MAX_ASTEROID_ROTATION + 1) - GlobalVars.MAX_ASTEROID_ROTATION;
		
		//set spawnpoint
		double rndNum = rnd.nextInt(4);
		//top
		if(rndNum == 0){
			x = rnd.nextInt(glob.getBorderRight()) + glob.getBorderLeft();
			y = glob.getBorderTop();
		}
		//right
		else if(rndNum == 1){
			y = rnd.nextInt(glob.getBorderBottom()) + glob.getBorderTop();
			x = glob.getBorderRight();
		}
		//bottom
		else if(rndNum == 2){
			x = rnd.nextInt(glob.getBorderRight()) + glob.getBorderLeft();
			y = glob.getBorderBottom();
		}
		//left
		else if(rndNum == 3){
			y = rnd.nextInt(glob.getBorderBottom()) + glob.getBorderTop();
			x = glob.getBorderLeft();
		}
		
		double vx = (double)(glob.getScreenDimX()/2 - x);	//vector between asteroid and middle
		double vy = (double)(glob.getScreenDimY()/2 - y);	//
		double vlength = Math.sqrt(vx*vx + vy*vy);			//length of vector
		
		//get speed, normalise vector, multiply with speed
		rndNum = rnd.nextInt(glob.getMaxAsteroidSpeed() - glob.getMinAsteroidSpeed()) + glob.getMinAsteroidSpeed();
		vx = (vx / vlength) * rndNum;
		vy = (vy / vlength) * rndNum;
		
		//get degree, convert to radians, rotate vector
		rndNum = rnd.nextInt(GlobalVars.MAX_ASTEROID_DEGREE) - (GlobalVars.MAX_ASTEROID_DEGREE/2);
		rndNum = Math.toRadians(rndNum);
		xSpeed = (int)Math.ceil(vx*Math.cos(rndNum) - vy*Math.sin(rndNum));
		ySpeed = (int)Math.ceil(vx*Math.sin(rndNum) + vy*Math.cos(rndNum));

		//if no speed init again		
		if(xSpeed == 0 && ySpeed == 0){
			initAsteroid();
		}
	}
	
	//draw method
	public void draw(Canvas canvas) {
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setFilterBitmap(true);
		paint.setDither(true);
		canvas.save();
		canvas.rotate(angle, x + (bitmap.getWidth() / 2), y + (bitmap.getHeight() / 2));
		canvas.drawBitmap(bitmap, x , y , paint);
		canvas.restore();
	}

	//updates movement, if at border initialise asteroid again
	public void update() {
		x += xSpeed;
		y += ySpeed;
		
		angle = angle + rotation;
		
		if(y < glob.getBorderTop() || y > glob.getBorderBottom() || x < glob.getBorderLeft() || x > glob.getBorderRight()){
			initAsteroid();
		}
	}
	
	//------------------------------------------------------------------------------------------------
	//------------------------------------------------------------------------------------------------
	//								getter and setter
	//------------------------------------------------------------------------------------------------
	//------------------------------------------------------------------------------------------------	
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	
	public void setSpeed(int x, int y){
		xSpeed = x;
		ySpeed = y;
	}
	
}
