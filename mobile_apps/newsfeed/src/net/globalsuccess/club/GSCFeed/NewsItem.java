package net.globalsuccess.club.GSCFeed;

import java.util.Date;

public class NewsItem {

	private String title;
	private String content;
	private String date;
	private String url;
	private Date dateAdded;
	
	public NewsItem(String title, String content, String date, String url){
		this.title = title;
		this.content = content;
		this.date = date;
		this.url = url;
		this.setDateAdded(new Date());
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Date getDateAdded() {
		return dateAdded;
	}

	public void setDateAdded(Date dateAdded) {
		this.dateAdded = dateAdded;
	}
}
