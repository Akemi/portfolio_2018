package net.globalsuccess.club.GSCFeed;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

public class NewsUpdateService extends Service {

	private GlobalVars glob = GlobalVars.getInstance();
	private List<NewsItem> newsData = new ArrayList<NewsItem>();		//all news items
	private ScheduledExecutorService scheduler = null;					//scheduler object
	
	@SuppressWarnings("deprecation")
	@Override
	public void onStart(Intent intent, int startId) {
		//service start message
		Log.v("Service", "start");
		
		//get all IDs
		AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this.getApplicationContext());
		int[] appWidgetIds = intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);
		
		//click events
		for (int i = 0; i < appWidgetIds.length; i++) {
			//service intent
			Intent svcIntent = new Intent(getApplicationContext(), NewsService.class);
			
			//set remote adapter for list
			RemoteViews widget = new RemoteViews(getApplicationContext().getPackageName(), R.layout.gscfeed);
			widget.setRemoteAdapter(appWidgetIds[i], R.id.widget_list, svcIntent);
			
			//set click action for element
			Intent clickIntent = new Intent(getApplicationContext(), GSCFeed.class);
            clickIntent.setAction(GSCFeed.CLICK_ACTION);
            clickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
            svcIntent.setData(Uri.parse(svcIntent.toUri(Intent.URI_INTENT_SCHEME)));
            PendingIntent clickPendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, clickIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            widget.setPendingIntentTemplate(R.id.widget_list, clickPendingIntent);
			
            //uodate widget
			appWidgetManager.updateAppWidget(appWidgetIds[i], widget);
		}
		
		//scheduler to update data, only initiate new scheduler when new scheduler
		if(scheduler == null){
			scheduler = Executors.newSingleThreadScheduledExecutor();
			scheduler.scheduleAtFixedRate(new fetchInternetNewsData(), 10, 10, TimeUnit.SECONDS); 
		}

		super.onStart(intent, startId);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	//runnable to fetch news data from internet
	public class fetchInternetNewsData implements Runnable {
		
		@Override
		public void run() {
			Log.i("service", "fetching data");
            newsData = glob.getNewsData();
            newsData.add(0, new NewsItem("Test 1", "Kompakt und auf den Punkt gebracht - im Newsletter des Global Success Club erhalten Sie vielseitige Informationen zu aktuellen Wachstumsmarkten. Je nach Schwerpunktland und markt finden Sie im Newsletter interessante Entwicklungen und Trends im Uberblick. So erweitern Sie online schnell und unkompliziert Ihr Wissen zu Landern und Markten, die fur sie von Bedeutung sind.", "11th April", "http://www.google.de"));
            glob.setNewsData(newsData);
            Intent i = new Intent(getApplicationContext(), GSCFeed.class);
            i.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            getApplicationContext().sendBroadcast(i);
            postNotification(0);
            //postNotification(1);
		}
		
	}
	
	//post a notification when new item was added
	public void postNotification(int clickedElement){
		NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(getApplicationContext())
                .setSmallIcon(R.drawable.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.logo_globe))
                .setContentTitle(glob.getNewsData().get(clickedElement).getTitle())
                .setContentText(glob.getNewsData().get(clickedElement).getContent())
                //.setNumber(5)
                .setAutoCancel(true);
		//
        //Intent resultIntent = new Intent(Intent.ACTION_VIEW);
        //resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //resultIntent.setData(Uri.parse("http://www.google.de"));

        Intent clickIntent = new Intent(getApplicationContext(), GSCFeed.class);
        clickIntent.setAction(GSCFeed.NOTIFICATION_ACTION);
        clickIntent.putExtra(GSCFeed.CLICKED_ITEM, clickedElement);
        PendingIntent clickPendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, clickIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        //widget.setPendingIntentTemplate(R.id.widget_list, clickPendingIntent);
        
        
        
        
        //TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
        //stackBuilder.addNextIntent(resultIntent);
        //PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(clickPendingIntent);
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
        mNotificationManager.notify(clickedElement, mBuilder.build()); 
		
	}
	
	//stops service and scheduler
	@Override
	public void onDestroy(){
	    Log.v("service","stopped");
	    scheduler.shutdown();
	    stopSelf();
	    super.onDestroy();  
	}
}
