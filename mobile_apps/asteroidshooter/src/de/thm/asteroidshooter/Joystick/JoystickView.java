package de.thm.asteroidshooter.Joystick;

import de.thm.asteroidshooter.Global.GlobalVars;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class JoystickView extends View {
	public static final int INVALID_POINTER_ID = -1;

	String TAG = "JoystickView";
	
	//color for handle and its background
	private Paint bgPaint;
	private Paint handlePaint;
	
	//measurements of joystick
	private int innerPadding;
	private int bgRadius;
	private int handleRadius;
	private int movementRadius;
	private int handleInnerBoundaries;
	
	//listener
	private JoystickMovedListener moveListener;

	//resolution of system
	private float moveResolution;
	
	//range of handle
	private float movementRange;
	
	//pointer id
	private int pointerId = INVALID_POINTER_ID;
	
	//x and y variables
	private float touchX, touchY;
	private float reportX, reportY;
	private float handleX, handleY;
	private int cX, cY;
	
	//x and y dimension
	private int dimX, dimY;
	
	//to constrain in circle
	private double radial;
	
	//coordinates input, for listener
	private int userX, userY;

	//movement offset in both directions
	private int offsetX;
	private int offsetY;

	
	public JoystickView(Context context) {
		super(context);
		initJoystickView();
	}

	public JoystickView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initJoystickView();
	}

	public JoystickView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initJoystickView();
	}

	//set standard variables
	private void initJoystickView() {
		setFocusable(true);
		
		//handle background colour
		bgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		bgPaint.setColor(Color.WHITE);
		bgPaint.setAlpha(GlobalVars.UI_ALPHA);
		bgPaint.setStyle(Paint.Style.FILL);
		//handle colour
		handlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		handlePaint.setColor(Color.BLACK);
		handlePaint.setAlpha(GlobalVars.UI_ALPHA);
		handlePaint.setStyle(Paint.Style.FILL);
		//padding for handle
		innerPadding = 10;
	}
	
	
	public void setMovementRange(float movementRange) {
		this.movementRange = movementRange;
	}
	
	public void setOnJostickMovedListener(JoystickMovedListener listener) {
		this.moveListener = listener;
	}
	
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		//perfect circle
		int measuredWidth = measure(widthMeasureSpec);
		int measuredHeight = measure(heightMeasureSpec);
		setMeasuredDimension(measuredWidth, measuredHeight);
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		super.onLayout(changed, left, top, right, bottom);

		int d = Math.min(getMeasuredWidth(), getMeasuredHeight());

		dimX = d;
		dimY = d;

		cX = d / 2;
		cY = d / 2;
		
		bgRadius = dimX/2 - innerPadding;
		handleRadius = (int)(d * 0.30);
		handleInnerBoundaries = handleRadius;
		movementRadius = Math.min(cX, cY) - handleInnerBoundaries;
	}

	private int measure(int measureSpec) {
		int result = 0;
		//decode the measurement specifications.
		int specMode = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);
		if (specMode == MeasureSpec.UNSPECIFIED) {
			//return a default size of 200 if no bounds are specified.
			result = 200;
		} else {
			//always return the full available bounds
			result = specSize;
		}
		return result;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.save();
		//draw background
		canvas.drawCircle(cX, cY, bgRadius, bgPaint);

		//draw handle
		handleX = touchX + cX;
		handleY = touchY + cY;
		canvas.drawCircle(handleX, handleY, handleRadius, handlePaint);
		
		canvas.restore();
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
	    final int action = ev.getAction();
		switch (action & MotionEvent.ACTION_MASK) {
		    case MotionEvent.ACTION_MOVE: {
	    		return processMoveEvent(ev);
		    }	    
		    case MotionEvent.ACTION_CANCEL: 
		    case MotionEvent.ACTION_UP: {
		    	if (pointerId != INVALID_POINTER_ID) {
			    	returnHandleToCenter();
		        	setPointerId(INVALID_POINTER_ID);
		    	}
		        break;
		    }
		    case MotionEvent.ACTION_POINTER_UP: {
		    	if (pointerId != INVALID_POINTER_ID) {
			        final int pointerIndex = (action & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
			        final int pointerId = ev.getPointerId(pointerIndex);
			        if (pointerId == this.pointerId) {
			        	returnHandleToCenter();
			        	setPointerId(INVALID_POINTER_ID);
			    		return true;
			        }
		    	}
		        break;
		    }
		    case MotionEvent.ACTION_DOWN: {
		    	if (pointerId == INVALID_POINTER_ID) {
		    		int x = (int) ev.getX();
		    		if (x >= offsetX && x < offsetX + dimX) {
			        	setPointerId(ev.getPointerId(0));
			    		return true;
		    		}
		    	}
		        break;
		    }
		    case MotionEvent.ACTION_POINTER_DOWN: {
		    	if (pointerId == INVALID_POINTER_ID) {
			        final int pointerIndex = (action & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
			        final int pointerId = ev.getPointerId(pointerIndex);
		    		int x = (int) ev.getX(pointerId);
		    		if (x >= offsetX && x < offsetX + dimX) {
			        	setPointerId(pointerId);
			    		return true;
		    		}
		    	}
		        break;
		    }
	    }
		return false;
	}
	
	private boolean processMoveEvent(MotionEvent ev) {
		if (pointerId != INVALID_POINTER_ID) {
			final int pointerIndex = ev.findPointerIndex(pointerId);
			
			float x = ev.getX(pointerIndex);
			touchX = x - cX - offsetX;
			float y = ev.getY(pointerIndex);
			touchY = y - cY - offsetY;
        	
			reportOnMoved();
			invalidate();
			
			return true;
		}
		return false;
	}

	private void reportOnMoved() {
		//constrains movement as a circle
		float diffX = touchX;
		float diffY = touchY;
		double radial = Math.sqrt((diffX*diffX) + (diffY*diffY));
		if ( radial > movementRadius ) {
			touchX = (int)((diffX / radial) * movementRadius);
			touchY = (int)((diffY / radial) * movementRadius);
		}
		
		//calculate cartesian coordinates
		userX = (int)(touchX / movementRadius * movementRange);
		userY = (int)(touchY / movementRadius * movementRange);

		if (moveListener != null) {
			boolean rx = Math.abs(touchX - reportX) >= moveResolution;
			boolean ry = Math.abs(touchY - reportY) >= moveResolution;
			if (rx || ry) {
				this.reportX = touchX;
				this.reportY = touchY;
				
				moveListener.OnMoved(userX, userY);
			}
		}
	}

	//return handle to center
	private void returnHandleToCenter() {
		final int numberOfFrames = 5;
		final double intervalsX = (0 - touchX) / numberOfFrames;
		final double intervalsY = (0 - touchY) / numberOfFrames;

		for (int i = 0; i < numberOfFrames; i++) {
			final int j = i;
			postDelayed(new Runnable() {
				@Override
				public void run() {
					touchX += intervalsX;
					touchY += intervalsY;
					
					reportOnMoved();
					invalidate();
					
					if (moveListener != null && j == numberOfFrames - 1) {
						moveListener.OnReturnedToCenter();
					}
				}
			}, i * 40);
		}

		if (moveListener != null) {
			moveListener.OnReleased();
		}
	}

	//------------------------------------------------------------------------------------------------
	//------------------------------------------------------------------------------------------------
	//								simple setter and getter
	//------------------------------------------------------------------------------------------------
	//------------------------------------------------------------------------------------------------
	
	public void setTouchOffset(int x, int y) {
		offsetX = x;
		offsetY = y;
	}
	
	public void setPointerId(int id) {
		this.pointerId = id;
	}
}
