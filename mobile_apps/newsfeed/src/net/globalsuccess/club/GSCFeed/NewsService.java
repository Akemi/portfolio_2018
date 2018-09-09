package net.globalsuccess.club.GSCFeed;

import android.content.Intent;
import android.widget.RemoteViewsService;

public class NewsService extends RemoteViewsService {
	
	@Override
	public RemoteViewsFactory onGetViewFactory(Intent intent) {
		return(new NewsViewsFactory(this.getApplicationContext(), intent));
	}

}
