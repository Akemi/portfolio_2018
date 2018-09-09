package net.globalsuccess.club.GSCFeed;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;


public class NewsViewsFactory implements RemoteViewsService.RemoteViewsFactory {
	
	private Context context = null;	
	private GlobalVars glob = GlobalVars.getInstance();
	private List<NewsItem> newsData = new ArrayList<NewsItem>();

	public NewsViewsFactory(Context ctxt, Intent intent) {
		this.context = ctxt;
	}

	@Override
	public void onCreate() {
		
        /*newsData.add(new NewsItem("Global Success Club Newsletter 1", "Kompakt und auf den Punkt gebracht - im Newsletter des Global Success Club erhalten Sie vielseitige Informationen zu aktuellen Wachstumsmarkten. Je nach Schwerpunktland und markt finden Sie im Newsletter interessante Entwicklungen und Trends im Uberblick. So erweitern Sie online schnell und unkompliziert Ihr Wissen zu Landern und Markten, die fur sie von Bedeutung sind.", "11th April", "URL"));
        newsData.add(new NewsItem("GSC News 2", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Morbi at tortor nunc, ut convallis lacus. Nam at leo non nunc congue porttitor. Aenean dolor elit, mattis eget lacinia vel, porttitor vitae felis. Integer nisl nibh, dignissim a auctor et, pretium quis elit. In mauris nibh, faucibus vel suscipit ut, auctor in lacus. Fusce laoreet mattis metus, id ullamcorper sapien lobortis id", "10th April", "URL2"));
        newsData.add(new NewsItem("GSC News 1 Important", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Morbi at tortor nunc, ut convallis lacus. Nam at leo non nunc congue porttitor. Aenean dolor elit, mattis eget lacinia vel, porttitor vitae felis. Integer nisl nibh, dignissim a auctor et, pretium quis elit. In mauris nibh, faucibus vel suscipit ut, auctor in lacus. Fusce laoreet mattis metus, id ullamcorper sapien lobortis id", "29th March", "URL3"));
        newsData.add(new NewsItem("GSC Event 1", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Morbi at tortor nunc, ut convallis lacus. Nam at leo non nunc congue porttitor. Aenean dolor elit, mattis eget lacinia vel, porttitor vitae felis. Integer nisl nibh, dignissim a auctor et, pretium quis elit. In mauris nibh, faucibus vel suscipit ut, auctor in lacus. Fusce laoreet mattis metus, id ullamcorper sapien lobortis id", "10th March", "URL4"));
        newsData.add(new NewsItem("GSC Reminder 1", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Morbi at tortor nunc, ut convallis lacus. Nam at leo non nunc congue porttitor. Aenean dolor elit, mattis eget lacinia vel, porttitor vitae felis. Integer nisl nibh, dignissim a auctor et, pretium quis elit. In mauris nibh, faucibus vel suscipit ut, auctor in lacus. Fusce laoreet mattis metus, id ullamcorper sapien lobortis id", "27th February", "URL5"));
        */
        //glob.setNewsData(newsData);
	}

	@Override
	public void onDestroy() {
	}

	@Override
	public int getCount() {
		//number of list items, set to static number to limit the list
		return newsData.size();
	}

	@Override
	public RemoteViews getViewAt(int position) {
		//get news data tmp
		newsData = glob.getNewsData();
		
		//time difference when item was added
		int sec = -1;
		
		//only try to calculate difference when not null
		if(newsData.get(position).getDateAdded() != null){
			long difference = (new Date()).getTime() - newsData.get(position).getDateAdded().getTime();
			//int min = (int) (difference/(1000*60));
			sec = (int) (difference/1000);
			Log.v("time", String.valueOf(sec));
		}
		
		//remote view
		RemoteViews row;
		
		//if time difference is still small, mark as new item
		if(sec < 15 && sec >= 0){
			row = new RemoteViews(context.getPackageName(), R.layout.list_row_new);
		}
		else {
			row = new RemoteViews(context.getPackageName(), R.layout.list_row);
			glob.getNewsData().get(position).setDateAdded(null);						//set date null since it's not new anymore
		}
		
		
		//set text for all textviews
		row.setTextViewText(R.id.list_title, newsData.get(position).getTitle());
		row.setTextViewText(R.id.list_content, newsData.get(position).getContent());
		row.setTextViewText(R.id.list_date, newsData.get(position).getDate());

		//click event for the whole row
	    Bundle extras = new Bundle();
	    extras.putInt(GSCFeed.CLICKED_ITEM, position);
	    Intent fillInIntent = new Intent();
	    fillInIntent.putExtras(extras);
	    row.setOnClickFillInIntent(R.id.list_row_item, fillInIntent);
	    
		return row;
	}

	@Override
	public RemoteViews getLoadingView() {
		return null;
	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public void onDataSetChanged() {
		Log.v("update", "data");
		//get the new data
		newsData = glob.getNewsData();
	}

}
