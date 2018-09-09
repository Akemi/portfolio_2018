package net.globalsuccess.club.GSCFeed;

import android.os.Bundle;
import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class GSCFeedConfig extends Activity {
	Button configOkButton;
	int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gscfeed_config);
		
		setResult(RESULT_CANCELED);

	    Intent intent = getIntent();
	    Bundle extras = intent.getExtras();
	    if (extras != null) {
	         mAppWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
	    }
	  
	    // If they gave us an intent without the widget id, just bail.
	    if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
	         finish();
	    }
	}

	public void changeActivity(View view){
		Intent resultValue = new Intent();
		resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
		setResult(RESULT_OK, resultValue);
		finish();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		return true;
	}

}
