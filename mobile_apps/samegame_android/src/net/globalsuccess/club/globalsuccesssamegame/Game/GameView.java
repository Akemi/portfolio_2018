package net.globalsuccess.club.globalsuccesssamegame.Game;

import net.globalsuccess.club.globalsuccesssamegame.GameScreen;
import net.globalsuccess.club.globalsuccesssamegame.R;
import net.globalsuccess.club.globalsuccesssamegame.global.GlobalFun;
import net.globalsuccess.club.globalsuccesssamegame.global.GlobalVars;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.os.Vibrator;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;



public class GameView extends SurfaceView implements SurfaceHolder.Callback {

	private GlobalVars glob = GlobalVars.getInstance();		//Singleton pattern variables
	private SurfaceHolder surfaceHolder;					//surface holder for canvas
	private Block Blocks[][] = null;						//Block array
	private int touchCorX = -1;								//touched point x coordinate
	private int touchCorY = -1;								//touched point y coordinate
	private Context ctx;									//context of acticity
	int blockcount;											//current number of blocks
	int score = 0;											//current score
	

	public GameView(Context context) {
		super(context);
		surfaceHolder = getHolder();
		getHolder().addCallback(this);						//add to Callback method, all surface methods
		ctx = context;										//context of activity
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		
	}

	//when the surface of the games is created
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
	
		//set initial variables for the singleton pattern
		//VERY IMPORTANT, SEE DESCRIPTION IN GlobVars SINGLETON PATTER
		if(getWidth() > getHeight()){										//check for right scaling
			glob.setPixelScaleFac((double)getWidth()/(double)800);
		}
		else {glob.setPixelScaleFac((double)getHeight()/(double)800);}
		
		glob.setScreenDimX(getWidth());										//canvas size x
		glob.setScreenDimY(getHeight());									//canvas size y
		
		if(glob.getScreenDimX()/(GlobalVars.RES_X_BLOCKS*glob.getDifficulty()) <= (glob.getScreenDimY()-glob.getScoreBarHeight())/(GlobalVars.RES_X_BLOCKS*glob.getDifficulty())){				//dimension of blocks, depends on difficulty and screen size
			glob.setBlockDim((int)Math.floor(glob.getScreenDimX()/(GlobalVars.RES_X_BLOCKS*glob.getDifficulty())));
		}
		else{glob.setBlockDim((int)Math.floor((glob.getScreenDimY()-glob.getScoreBarHeight())/(GlobalVars.RES_Y_BLOCKS*glob.getDifficulty())));}
		
		//set the offset for centered display
		glob.setScreenOffsetX((glob.getScreenDimX() - glob.getBlockDim()*glob.getDifficulty()*GlobalVars.RES_X_BLOCKS)/2);
		glob.setScreenOffsetY((glob.getScreenDimY() - glob.getScoreBarHeight() - glob.getBlockDim()*glob.getDifficulty()*GlobalVars.RES_Y_BLOCKS));
		
		//sets background
		glob.setBackground(GlobalFun.getResizedBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.background), getHeight(), getWidth()));
		
		
		if(Blocks == null){
			//create block array with right dimension
			Blocks = new Block[GlobalVars.RES_X_BLOCKS*glob.getDifficulty()][GlobalVars.RES_Y_BLOCKS*glob.getDifficulty()];
			
			//fill block array
			for(int i=0; i < Blocks[0].length; i++){
				for(int j=0; j < Blocks.length; j++){
					Blocks[j][i] = new Block();	
				}
			}
			
			blockcount = Blocks[0].length*Blocks.length;
		}
		
		//render on canvas
		render();
}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		
	}
	
	//touch event
	@Override
    public boolean onTouchEvent(MotionEvent ev) {
		final int action = ev.getAction();
        
		switch (action & MotionEvent.ACTION_MASK) {
	        //finger touches  display
			case MotionEvent.ACTION_DOWN: {
	            final float x = ev.getX();
	            final float y = ev.getY();
	            
	            touchCorX = (int)Math.floor((x-glob.getScreenOffsetX())/glob.getBlockDim());
	            touchCorY = (int)Math.floor((y-glob.getRealOffsetY())/glob.getBlockDim());
	            
	            Log.v("Touched at:", touchCorX + " - " + touchCorY);
	            break;
	        }
			//finger removed from display
	        case MotionEvent.ACTION_UP: {
	        	final float x = ev.getX();
	            final float y = ev.getY();
	            
	            //touch in bounds, block existent
	            if(touchCorX >= 0 && touchCorX < GlobalVars.RES_X_BLOCKS*glob.getDifficulty() && touchCorY >= 0 && touchCorY < GlobalVars.RES_Y_BLOCKS*glob.getDifficulty() && Blocks[touchCorX][touchCorY] != null){
	            	//finger same place before and after
	            	if(touchCorX == (int)Math.floor((x-glob.getScreenOffsetX())/glob.getBlockDim()) && 
	            			touchCorY == (int)Math.floor((y-glob.getRealOffsetY())/glob.getBlockDim())){
	            		//check the block, if hit vibrate
	            		if(checkBlock(touchCorX, touchCorY, Blocks[touchCorX][touchCorY].getColour(), GlobalVars.DIRECTION_ALL)) vibrate();
	            		
	            		//counts destroyed blocks and calculates points
	            		Log.v("Points earned", String.valueOf(setNewScore()));
	            		
	            		//fill empty spaces
	            		shrinkBlocksDownwards();
	            		shrinkBlocksSidewards();
	            		
	            		//render screen
	            		render();
	            		
	            		//are there still moves left
	            		//end of the game
	            		Log.v("Moves Left", String.valueOf(checkMovesLeft()));
	            		
	            		if(checkMovesLeft() == false){
	            			Blocks = null;
	            			((GameScreen)ctx).changeActivity(score);
	            		}
	            	}
	            }
	            
	        	Log.v("Released at:", (int)Math.floor((x-glob.getScreenOffsetX())/glob.getBlockDim()) + " - " + (int)Math.floor((y-glob.getRealOffsetY())/glob.getBlockDim()));
	            break;
	        }
        }

		return true;
	}


	//render method
	public void render() {
		Canvas canvas = surfaceHolder.lockCanvas();
		
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setFilterBitmap(true);
		paint.setDither(true);
		
		//draw background
		canvas.drawBitmap(glob.getBackground(), 0, 0, paint);
		//canvas.drawColor(Color.GRAY);
		
		//draw the blocks
		for(int i=0; i < Blocks[0].length; i++){
			for(int j=0; j < Blocks.length; j++){
				if(Blocks[j][i] != null) canvas.drawBitmap(Blocks[j][i].getBitmap(), j*glob.getBlockDim()+glob.getScreenOffsetX() , i*glob.getBlockDim()+glob.getRealOffsetY(), paint);
			}
		}
		
		drawUI(canvas);
		
	    surfaceHolder.unlockCanvasAndPost(canvas);
	}
	
	private void drawUI(Canvas canvas) {
		Paint paint = new Paint();
		paint.setARGB(255, 94, 94, 94);
		paint.setAntiAlias(true);
		paint.setFilterBitmap(true);
		paint.setDither(true);
		paint.setTextSize(glob.getScoreBarFontSize());
		paint.setTextAlign(Align.RIGHT);
		paint.setTypeface(glob.getTypeFace());
		//draws points counter
		canvas.drawText(Integer.toString(score), glob.getScreenDimX() - glob.getScreenOffsetX() - glob.getScoreBarFontSize(), glob.getScoreBarFontSize(), paint);
		
	}
	
	//check the surrounding blocks
	public boolean checkBlock(int x, int y, int colour, int direction){
		boolean hit = false;		//flag when colours are the same, hit flag
		
		//check for direction, check if in level bounds, check if block still exists, check if new block has same colour as old 
		//check to the right
		if(direction != GlobalVars.DIRECTION_LEFT && x+1 < GlobalVars.RES_X_BLOCKS*glob.getDifficulty() && Blocks[x+1][y] != null && Blocks[x+1][y].getColour() == colour){
			checkBlock(x+1, y, Blocks[x+1][y].getColour(), GlobalVars.DIRECTION_RIGHT);
			hit = true;
		}
		//check to the bottom
		if(direction != GlobalVars.DIRECTION_UP && y+1 < GlobalVars.RES_Y_BLOCKS*glob.getDifficulty() && Blocks[x][y+1] != null && Blocks[x][y+1].getColour() == colour){
			checkBlock(x, y+1, Blocks[x][y+1].getColour(), GlobalVars.DIRECTION_DOWN);
			hit = true;
		}
		//check to the left
		if(direction != GlobalVars.DIRECTION_RIGHT && x-1 >= 0 && Blocks[x-1][y] != null && Blocks[x-1][y].getColour() == colour){
			checkBlock(x-1, y, Blocks[x-1][y].getColour(), GlobalVars.DIRECTION_LEFT);
			hit = true;
		}
		//check to the top
		if(direction != GlobalVars.DIRECTION_DOWN && y-1 >= 0 && Blocks[x][y-1] != null && Blocks[x][y-1].getColour() == colour){
			Blocks[x][y] = null;
			checkBlock(x, y-1, Blocks[x][y-1].getColour(), GlobalVars.DIRECTION_UP);
			hit = true;
		}
		//if something was hit
		if(direction != GlobalVars.DIRECTION_ALL || hit == true){
			Blocks[x][y] = null;
		}
		
		return hit;
	}
	
	//fills blank spaces with remaining blocks downwards
	public void shrinkBlocksDownwards(){
		for(int i=0; i < Blocks.length; i++){				//x direction from left
			for(int j=Blocks[0].length-1; j >= 0 ; j--){	//y direction from bottom
				int count = 0;
				while(Blocks[i][j] == null && count <= j){	//move blocks while null and not more moves then blocks
					for(int k=j; k > 0; k--){
						//swap block and the one above
						Block cache = Blocks[i][k];
						Blocks[i][k] = Blocks[i][k-1];
						Blocks[i][k-1] = cache;
					}
					count++;
				}
			}
		}
	}
	
	//fills blank spaces with remaining blocks sidewards
	public void shrinkBlocksSidewards(){
		for(int i=0; i < Blocks.length-1; i++){					//x direction from left
			int count = 0;
			while(Blocks[i][GlobalVars.RES_Y_BLOCKS*glob.getDifficulty()-1] == null && count < Blocks.length){	//move blocks while null and not more moves then blocks
				for(int k=i; k < Blocks.length-1; k++){			//x direction from left, beginning from first loop
					for(int j=0; j < Blocks[0].length; j++){	//y direction from top
						//swap block column and the next one
						Block cache = Blocks[k][j];
						Blocks[k][j] = Blocks[k+1][j];
						Blocks[k+1][j] = cache;
					}
				}
				count++;
			}
		}
	}
	
	//check if any moves are left
	public boolean checkMovesLeft(){
		//check array, besides last row and column
		for(int i=0; i < Blocks.length-1; i++){
			for(int j=0; j < Blocks[0].length-1; j++){
				//right block
				if(Blocks[i][j] != null && Blocks[i+1][j] != null){
					if(Blocks[i][j].getColour() == Blocks[i+1][j].getColour()){
						return true;
					}
				}
				//bottom block
				if(Blocks[i][j] != null && Blocks[i][j+1] != null){
					if(Blocks[i][j].getColour() == Blocks[i][j+1].getColour()){
						return true;
					}
				}
			}
		}
		//check last row
		for(int i=0; i < Blocks.length-1; i++){
			if(Blocks[i][Blocks[0].length-1] != null && Blocks[i+1][Blocks[0].length-1] != null ){
				if(Blocks[i][Blocks[0].length-1].getColour() == Blocks[i+1][Blocks[0].length-1].getColour()){
					return true;
				}
			}
		}
		//check last column
		for(int j=0; j < Blocks[0].length-1; j++){
			if(Blocks[Blocks.length-1][j] != null && Blocks[Blocks.length-1][j+1] != null ){
				if(Blocks[Blocks.length-1][j].getColour() == Blocks[Blocks.length-1][j+1].getColour()){
					return true;
				}
			}
		}
		
		return false;
	}
	
	//vibrates 200ms
	public void vibrate(){
		Vibrator v = (Vibrator) ctx.getSystemService(Context.VIBRATOR_SERVICE);
		v.vibrate(100);
	}
	
	//counts the destroyed blocks, for point calculation
	public int setNewScore(){
		int oldBlockcount = blockcount;
		blockcount = 0;
		
		//counts new number of blocks
		for(int i=0; i < Blocks.length; i++){
			for(int j=0; j < Blocks[0].length; j++){
				if(Blocks[i][j] != null){
					blockcount++;
				}
			}
		}
		//only calculate when blocks are destroyed
		if(oldBlockcount - blockcount != 0) score = score + (int)Math.pow(oldBlockcount - blockcount - 1, 2);
		
		//check for bonus points when all blocks are destroyed
		boolean blocksLeft = false;
		for(int i=0; i < Blocks.length; i++){
			for(int j=0; j < Blocks[0].length; j++){
				if(Blocks[i][j] != null){
					blocksLeft = true;
				}
			}
		}
		if(blocksLeft == false) score = score + GlobalVars.BONUS_POINTS_CLEAR;
		
		return (int)Math.pow(oldBlockcount - blockcount - 1, 2);
	}
	
	
	

}