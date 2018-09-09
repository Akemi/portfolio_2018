package net.globalsuccess.club.globalsuccesssamegame.global;

import android.graphics.Bitmap;
import android.graphics.Typeface;


public class GlobalVars {
	private static GlobalVars instance;

	//static variables
	public static int BLOCK_COLOUR1 = 1;					//block colour 1
	public static int BLOCK_COLOUR2 = 2;					//block colour 2
	public static int BLOCK_COLOUR3 = 3;					//block colour 3
	public static int BLOCK_COLOUR4 = 4;					//block colour 4
	public static int BLOCK_COLOUR5 = 5;					//block colour 5
	public static int DIF_EASY = 2;							//difficulty easy
	public static int DIF_NORMAL = 3;						//difficulty easy
	public static int DIF_HARD = 4;							//difficulty easy
	public static int DIRECTION_ALL = 0;					//check direction all
	public static int DIRECTION_RIGHT = 1;					//check direction right
	public static int DIRECTION_DOWN = 2;					//check direction down
	public static int DIRECTION_LEFT = 3;					//check direction left
	public static int DIRECTION_UP = 4;						//check direction up
	public static int RES_X_BLOCKS = 9;						//number of blocks x resolution
	public static int RES_Y_BLOCKS = 5;						//number of blocks y resolution
	public static int BONUS_POINTS_CLEAR = 10000;			//bonus points for level clear
	
	//static variables in pixel for 
	private static int SCORE_BAR_HEIGHT = 30;				//height of score bar
	private static int SCORE_BAR_FONT_SIZE = 24;			//font size in scorebar
	
	//scaled variables for destination resolution
	private double pixelScaleFac;							//scale factor for target device resolution
	private int scoreBarHeight;								//scaled bar height for scorebar
	private int scoreBarFontSize;							//scaled font size for scorebar

	
	//screen dimensions and off-screen borders
	private int ScreenDimX;									//screen dim x
	private int ScreenDimY;									//screen dim y
	private int ScreenOffsetX;								//offset of screen x direction
	private int ScreenOffsetY;								//offset of screen y direction
	private int BlockDim;									//dimension if blocks
	private Bitmap background;								//background of game
	
	//game settings
	private int NumOfColours;								//Number of Colours
	private int Difficulty;									//difficulty of the game
	
	//images
	private Bitmap block1Image;								//block images
	private Bitmap block2Image;
	private Bitmap block3Image;
	private Bitmap block4Image;
	private Bitmap block5Image;
		
	//font
	private Typeface typeFace;								//font
		
		
	
	public void setPixelScaleFac(double pixelScaleFac) {
		this.pixelScaleFac = pixelScaleFac;
		
		setScoreBarHeight((int)(pixelScaleFac * SCORE_BAR_HEIGHT));
		setScoreBarFontSize((int)(pixelScaleFac *SCORE_BAR_FONT_SIZE));
	}
	
	
	
	
	
	//singleton pattern
	public static synchronized GlobalVars getInstance(){
		if(instance == null){
			instance = new GlobalVars();
		}
		return instance;
	}

	
	//functional methods
	public int getRealOffsetY(){
		
		return ScreenOffsetY + scoreBarHeight;
	}



	//getter and setter methods
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

	public int getNumOfColours() {
		return NumOfColours;
	}
	public void setNumOfColours(int numOfColours) {
		NumOfColours = numOfColours;
	}

	public int getDifficulty() {
		return Difficulty;
	}
	public void setDifficulty(int difficulty) {
		Difficulty = difficulty;
	}

	public int getBlockDim() {
		return BlockDim;
	}
	public void setBlockDim(int blockDim) {
		BlockDim = blockDim;
	}

	public Bitmap getBlock1Image() {
		return block1Image;
	}

	public void setBlock1Image(Bitmap block1Image) {
		this.block1Image = block1Image;
	}

	public Bitmap getBlock2Image() {
		return block2Image;
	}
	public void setBlock2Image(Bitmap block2Image) {
		this.block2Image = block2Image;
	}

	public Bitmap getBlock3Image() {
		return block3Image;
	}
	public void setBlock3Image(Bitmap block3Image) {
		this.block3Image = block3Image;
	}

	public Bitmap getBlock4Image() {
		return block4Image;
	}
	public void setBlock4Image(Bitmap block4Image) {
		this.block4Image = block4Image;
	}

	public Bitmap getBlock5Image() {
		return block5Image;
	}
	public void setBlock5Image(Bitmap block5Image) {
		this.block5Image = block5Image;
	}

	public int getScreenOffsetX() {
		return ScreenOffsetX;
	}
	public void setScreenOffsetX(int screenOffsetX) {
		ScreenOffsetX = screenOffsetX;
	}

	public int getScreenOffsetY() {
		return ScreenOffsetY;
	}
	public void setScreenOffsetY(int screenOffsetY) {
		ScreenOffsetY = screenOffsetY;
	}

	public int getScoreBarHeight() {
		return scoreBarHeight;
	}
	public void setScoreBarHeight(int scoreBarHeight) {
		this.scoreBarHeight = scoreBarHeight;
	}





	public int getScoreBarFontSize() {
		return scoreBarFontSize;
	}





	public void setScoreBarFontSize(int scoreBarFontSize) {
		this.scoreBarFontSize = scoreBarFontSize;
	}





	public Bitmap getBackground() {
		return background;
	}





	public void setBackground(Bitmap background) {
		this.background = background;
	}





	public Typeface getTypeFace() {
		return typeFace;
	}





	public void setTypeFace(Typeface typeFace) {
		this.typeFace = typeFace;
	}
}
