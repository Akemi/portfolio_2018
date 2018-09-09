package de.thm.asteroidshooter.Objects;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import de.thm.asteroidshooter.Global.GlobalFun;
import de.thm.asteroidshooter.Global.GlobalVars;

public class Bullet {
	private Bitmap bitmap;									//image of bullet
	private int x;											//x pos
	private int y;											//y pos
	private int xSpeed = 1;									//x speed
	private int ySpeed = 1;									//y speed
	private int angle;										//angle
	private GlobalVars glob = GlobalVars.getInstance();		//singleton pattern
	
	public Bullet(Ship ship) {
		this.bitmap = GlobalFun.getResizedBitmap(glob.getBullet(), glob.getBulletDim(), glob.getBulletDim());
		//spawn point middle of ship, sets angle
		x = ship.getX() + glob.getObjDim()/2 - glob.getBulletDim()/2;
		y = ship.getY() + glob.getObjDim()/2 - glob.getBulletDim()/2;
		angle = ship.getAngle();
		
		//length of vector
		double vlength = Math.sqrt(ship.getAngleX()*ship.getAngleX() + ship.getAngleY()*ship.getAngleY());
		
		//get speed, normalise vector, multiply with speed
		xSpeed = (int)Math.ceil(((double)ship.getAngleX() / vlength) * glob.getMaxBulletSpeed());
		ySpeed = (int)Math.ceil(((double)ship.getAngleY() / vlength) * glob.getMaxBulletSpeed());
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

	//updates position, if at border return false
	public boolean update() {
		x += xSpeed;
		y += ySpeed;
		
		if(y < glob.getBorderTop() || y > glob.getBorderBottom() || x < glob.getBorderLeft() || x > glob.getBorderRight()){
			return false;
		}
		return true;
	}
	
	//collision detection with atseroid (pythagoras)
	public boolean collision(Asteroid aster){
		double xDistance = (double)((x+glob.getBulletDim()/2) - (aster.getX()+glob.getObjDim()/2));
		double yDistance = (double)((y+glob.getBulletDim()/2) - (aster.getY()+glob.getObjDim()/2));
		
		if(Math.sqrt(xDistance*xDistance + yDistance*yDistance) < (glob.getObjDim()/2 + glob.getBulletDim()/2)){
			return true;
		}
		return false;
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
