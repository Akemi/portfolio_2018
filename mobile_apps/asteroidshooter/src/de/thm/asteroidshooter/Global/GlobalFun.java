package de.thm.asteroidshooter.Global;

import android.graphics.Bitmap;
import android.graphics.Matrix;

public final class GlobalFun {
	
	//resize bitmap
	public static Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
        return resizedBitmap;
    }
	
	//rotate bitmap
	public static Bitmap getRotatedBitmap(Bitmap bm, int an) {
	    int width = bm.getWidth();
	    int height = bm.getHeight();
	    Matrix matrix = new Matrix();
	    matrix.postRotate(an, width/2, height/2);
	    Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
	    return resizedBitmap;
	}
}
