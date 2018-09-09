package de.thm.asteroidshooter.Joystick;

import de.thm.asteroidshooter.Global.GlobalVars;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class DualJoystickView extends LinearLayout {
	@SuppressWarnings("unused")
	private static final String TAG = DualJoystickView.class.getSimpleName();

	private JoystickView stickL;
	private JoystickView stickR;
	private GlobalVars glob = GlobalVars.getInstance();

	private View pad;

	public DualJoystickView(Context context) {
		super(context);
		stickL = new JoystickView(context);
		stickR = new JoystickView(context);
		initDualJoystickView();
	}

	public DualJoystickView(Context context, AttributeSet attrs) {
		super(context, attrs);
		stickL = new JoystickView(context, attrs);
		stickR = new JoystickView(context, attrs);
		initDualJoystickView();
	}

	private void initDualJoystickView() {
		setOrientation(LinearLayout.HORIZONTAL);	
		pad = new View(getContext());
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		removeView(stickL);
		removeView(stickR);

		float padW = getMeasuredWidth()-(getMeasuredHeight()*2);
		int joyWidth = (int) ((getMeasuredWidth()-padW)/2);
		LayoutParams joyLParams = new LayoutParams(joyWidth, getMeasuredHeight());
		
		stickL.setLayoutParams(joyLParams);
		stickR.setLayoutParams(joyLParams);
		
		stickL.setMovementRange(glob.getLeftJoyStickPrecission());
		stickR.setMovementRange(GlobalVars.RIGHT_JOYSTICK_PRECISSION);
		
		stickL.TAG = "L";
		stickR.TAG = "R";
		stickL.setPointerId(JoystickView.INVALID_POINTER_ID);
		stickR.setPointerId(JoystickView.INVALID_POINTER_ID);

		addView(stickL);

		ViewGroup.LayoutParams padLParams = new ViewGroup.LayoutParams((int) padW, getMeasuredHeight());
		removeView(pad);
		pad.setLayoutParams(padLParams);
		addView(pad);
		
		addView(stickR);
	}
	
	//------------------------------------------------------------------------------------------------
	//------------------------------------------------------------------------------------------------
	//								set listener and standard methods
	//------------------------------------------------------------------------------------------------
	//------------------------------------------------------------------------------------------------
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		stickR.setTouchOffset(stickR.getLeft(), stickR.getTop());
	}
	
	public void setOnJostickMovedListener(JoystickMovedListener left, JoystickMovedListener right) {
		stickL.setOnJostickMovedListener(left);
		stickR.setOnJostickMovedListener(right);
	}

	public void setMovementRange(float movementRangeLeft, float movementRangeRight) {
		stickL.setMovementRange(movementRangeLeft);
		stickR.setMovementRange(movementRangeRight);
	}

	@Override
	protected void dispatchDraw(Canvas canvas) {
		super.dispatchDraw(canvas);
	}
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
    	boolean l = stickL.dispatchTouchEvent(ev);
    	boolean r = stickR.dispatchTouchEvent(ev);
    	return l || r;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
    	boolean l = stickL.onTouchEvent(ev);
    	boolean r = stickR.onTouchEvent(ev);
    	return l || r;
	}
}
