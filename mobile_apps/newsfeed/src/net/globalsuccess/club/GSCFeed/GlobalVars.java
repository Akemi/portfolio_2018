package net.globalsuccess.club.GSCFeed;

import java.util.ArrayList;
import java.util.List;


public class GlobalVars{
	private static GlobalVars instance;

	//all the fetched news items
	private List<NewsItem> newsData = new ArrayList<NewsItem>();
	
	//static variables
	private static int NEW_INDICATOR_INTERVAL = 1;
	
	//
	private int newIndicatorIntervall = NEW_INDICATOR_INTERVAL;
	
	
	//Restrict the constructor from being instantiated
	private GlobalVars(){
		
	}
	
	//singleton pattern
	public static synchronized GlobalVars getInstance(){
		if(instance == null){
			instance = new GlobalVars();
		}
		return instance;
	}

	//basic getter and setter
	public List<NewsItem> getNewsData() {
		return newsData;
	}

	public void setNewsData(List<NewsItem> newsData) {
		this.newsData = newsData;
	}

	public int getNewIndicatorIntervall() {
		return newIndicatorIntervall;
	}

	public void setNewIndicatorIntervall(int newIndicatorIntervall) {
		this.newIndicatorIntervall = newIndicatorIntervall;
	}

	


}
