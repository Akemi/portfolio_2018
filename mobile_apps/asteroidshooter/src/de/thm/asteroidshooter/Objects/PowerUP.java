package de.thm.asteroidshooter.Objects;

import java.util.Random;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import de.thm.asteroidshooter.Global.GlobalFun;
import de.thm.asteroidshooter.Global.GlobalVars;

public class PowerUP {
	private Bitmap bitmap;									//image for powerup
	private int x;											//x pos
	private int y;											//y pos
	private int xSpeed;										//x speed
	private int ySpeed;										//y speed
	private Random rnd = new Random();						//random object
	private GlobalVars glob = GlobalVars.getInstance();		//singelton pattern
	private int status;										//what power up is used
	public static int SHIELD = 2;							//trigger shield
	public static int BOMB = 1;								//trigger bomb
	public static int POWERUP_MISS = 0;						//trigger missed
	
	public PowerUP() {
		//random of bomb or shield
		status = rnd.nextInt(2)+1;
		if(status == BOMB){
			this.bitmap = GlobalFun.getResizedBitmap(glob.getPowerUP1(), glob.getObjDim(), glob.getObjDim());
		}
		else if(status == SHIELD){
			this.bitmap = GlobalFun.getResizedBitmap(glob.getPowerUP2(), glob.getObjDim(), glob.getObjDim());
		}
		initPower();
	}
	
	//initialise power up
	private void initPower(){
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
			
			double vx = (double)(glob.getScreenDimX()/2 - x);	//vector between object and middle
			double vy = (double)(glob.getScreenDimY()/2 - y);	//
			double vlength = Math.sqrt(vx*vx + vy*vy);			//length of vector
			
			//get speed, normalise vector, multiply with speed
			rndNum = rnd.nextInt(glob.getMaxPowerUPSpeed()) + 1;
			vx = (vx / vlength) * rndNum;
			vy = (vy / vlength) * rndNum;
			
			//get degree, convert to radians, rotate vector
			rndNum = rnd.nextInt(GlobalVars.MAX_POWERUP_DEGREE) - (GlobalVars.MAX_POWERUP_DEGREE/2);
			rndNum = Math.toRadians(rndNum);
			xSpeed = (int)Math.ceil(vx*Math.cos(rndNum) - vy*Math.sin(rndNum));
			ySpeed = (int)Math.ceil(vx*Math.sin(rndNum) + vy*Math.cos(rndNum));

			//if no speed init again		
			if(xSpeed == 0 && ySpeed == 0){
				initPower();
			}
	}

	//draw method
	public void draw(Canvas canvas) {
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setFilterBitmap(true);
		paint.setDither(true);
		canvas.drawBitmap(bitmap, x , y , paint);
	}

	//update position, border -> return false
	public boolean update() {
		x += xSpeed;
		y += ySpeed;
		
		if(y < glob.getBorderTop() || y > glob.getBorderBottom() || x < glob.getBorderLeft() || x > glob.getBorderRight()){
			return false;
		}
		return true;
	}
	
	//collision detection with ship, return if missed or which power up was picked up
	public int collision(Ship ship){
		double xDistance = (double)((x+glob.getObjDim()/2) - (ship.getX()+glob.getObjDim()/2));
		double yDistance = (double)((y+glob.getObjDim()/2) - (ship.getY()+glob.getObjDim()/2));
		
		if(Math.sqrt(xDistance*xDistance + yDistance*yDistance) < glob.getObjDim()){
			return status;
		}
		return POWERUP_MISS;
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
