package net.globalsuccess.club.GSCFeed;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;

public class GSCFeed extends AppWidgetProvider {
	//events and item ID
	public static final String NOTIFICATION_ACTION = "net.globalsuccess.club.GSCFeed.NOTIFICATION_ACTION";
	public static final String CLICK_ACTION = "net.globalsuccess.club.GSCFeed.CLICK_ACTION";
	public static final String CLICKED_ITEM = "net.globalsuccess.club.GSCFeed.CLICKED_ITEM";
	public static final String GO_TO_HP = "net.globalsuccess.club.GSCFeed.GO_TO_HP";
	private GlobalVars glob = GlobalVars.getInstance();
	
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {		
		//get all ids
	    ComponentName thisWidget = new ComponentName(context, GSCFeed.class);
	    int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);

	    //build the intent to call the service
	    Intent svcintent = new Intent(context.getApplicationContext(), NewsUpdateService.class);
	    svcintent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, allWidgetIds);
	    
	    //add click to globe
	    Intent intent = new Intent(context, GSCFeed.class);
	    intent.setAction(GO_TO_HP);
	    PendingIntent clickPendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
	    
	    RemoteViews widget = new RemoteViews(context.getPackageName(), R.layout.gscfeed);
	    widget.setOnClickPendingIntent(R.id.gsc_logo, clickPendingIntent);
	    appWidgetManager.updateAppWidget(appWidgetIds, widget);
	    
	    //update the widgets via the service
	    context.startService(svcintent);
	    
	    
	     

		super.onUpdate(context, appWidgetManager, appWidgetIds);
	}
	
	//when app is closed
	@Override
    public void onDisabled(Context context){
		super.onDisabled(context);
		Log.v("widget", "terminated");
    	context.stopService(new Intent(context.getApplicationContext(), NewsUpdateService.class));
	}
	
	@Override
    public void onEnabled(Context context){
		
	}
	
	
	@Override
    public void onReceive(Context context, Intent intent) {

		if (intent.getAction().equals(NOTIFICATION_ACTION)) {
			Log.v("notification", "click");
			int viewIndex = intent.getIntExtra(CLICKED_ITEM, 0);
			
			Intent resultIntent = new Intent(Intent.ACTION_VIEW);
	        resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	        resultIntent.setData(Uri.parse(glob.getNewsData().get(viewIndex).getUrl()));
	        glob.getNewsData().get(viewIndex).setDateAdded(null);
	        context.startActivity(resultIntent);
	        
	        //get all widget ids and update
            AppWidgetManager manager = AppWidgetManager.getInstance(context);
            int[] ids = manager.getAppWidgetIds(new ComponentName(context, GSCFeed.class));
        	manager.notifyAppWidgetViewDataChanged(ids, R.id.widget_list);
		}
		
		//if an item was clicked
		if (intent.getAction().equals(CLICK_ACTION)) {
            int viewIndex = intent.getIntExtra(CLICKED_ITEM, 0);
            //no need for a toast now
            //Toast.makeText(context, "Touched view " + viewIndex, Toast.LENGTH_SHORT).show();
            
            //reset new flag date
            glob.getNewsData().get(viewIndex).setDateAdded(null);
            
            //remove notification
            NotificationManager nMgr = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            nMgr.cancel(viewIndex);
            
            //get all widget ids and update
            AppWidgetManager manager = AppWidgetManager.getInstance(context);
            int[] ids = manager.getAppWidgetIds(new ComponentName(context, GSCFeed.class));
        	manager.notifyAppWidgetViewDataChanged(ids, R.id.widget_list);
        	
            //open site
    		Intent i = new Intent(Intent.ACTION_VIEW);
    		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    		i.setData(Uri.parse(glob.getNewsData().get(viewIndex).getUrl()));
    		context.startActivity(i);
    		
        }
        
		//update view when necessary 
        if(intent.getAction().equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE)){
        	//remoteview and widget manager
        	RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.gscfeed);
        	ComponentName thisWidget = new ComponentName(context, GSCFeed.class);
            AppWidgetManager manager = AppWidgetManager.getInstance(context);
            
            //set no news message visible when there are no news
        	if(glob.getNewsData().size() < 1){
        		remoteViews.setViewVisibility(R.id.empty_view, View.VISIBLE);
        	}
        	//set it invisible if there are some news
        	else {
        		remoteViews.setViewVisibility(R.id.empty_view, View.GONE);
        	}
        	manager.updateAppWidget(thisWidget, remoteViews);
        	
        	//get all IDs and uodate
        	int[] ids = manager.getAppWidgetIds(new ComponentName(context, GSCFeed.class));
        	manager.notifyAppWidgetViewDataChanged(ids, R.id.widget_list);
        }
        
        //clicked on globe
        if (intent.getAction().equals(GO_TO_HP)) {
        	Intent i = new Intent(Intent.ACTION_VIEW);
    		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    		i.setData(Uri.parse("https://globalsuccess-club.net"));
    		context.startActivity(i);
        }
        super.onReceive(context, intent);
    }
}
