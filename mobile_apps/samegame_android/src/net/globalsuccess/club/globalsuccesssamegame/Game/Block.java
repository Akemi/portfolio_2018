package net.globalsuccess.club.globalsuccesssamegame.Game;

import java.util.Random;

import net.globalsuccess.club.globalsuccesssamegame.global.GlobalFun;
import net.globalsuccess.club.globalsuccesssamegame.global.GlobalVars;


import android.graphics.Bitmap;

public class Block {
	
	private int Colour;										//colour of block
	private Bitmap bitmap;									//image of Block
	private Random rnd = new Random();						//random
	private GlobalVars glob = GlobalVars.getInstance();
	
	public Block() {
		//set random colour
		Colour = rnd.nextInt(glob.getNumOfColours())+1;
		if(Colour == GlobalVars.BLOCK_COLOUR1) this.setBitmap(GlobalFun.getResizedBitmap(glob.getBlock1Image(), glob.getBlockDim(), glob.getBlockDim()));
		if(Colour == GlobalVars.BLOCK_COLOUR2) this.setBitmap(GlobalFun.getResizedBitmap(glob.getBlock2Image(), glob.getBlockDim(), glob.getBlockDim()));
		if(Colour == GlobalVars.BLOCK_COLOUR3) this.setBitmap(GlobalFun.getResizedBitmap(glob.getBlock3Image(), glob.getBlockDim(), glob.getBlockDim()));
		if(Colour == GlobalVars.BLOCK_COLOUR4) this.setBitmap(GlobalFun.getResizedBitmap(glob.getBlock4Image(), glob.getBlockDim(), glob.getBlockDim()));
		if(Colour == GlobalVars.BLOCK_COLOUR5) this.setBitmap(GlobalFun.getResizedBitmap(glob.getBlock5Image(), glob.getBlockDim(), glob.getBlockDim()));
		
	}

	public Bitmap getBitmap() {
		return bitmap;
	}

	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}

	public int getColour() {
		return Colour;
	}

	public void setColour(int colour) {
		Colour = colour;
	}
}
