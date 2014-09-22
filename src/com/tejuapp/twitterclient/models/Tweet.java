package com.tejuapp.twitterclient.models;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class Tweet {
	private String body;
	private String id;
	private String createdAt;
	private User user;
	private Date timestamp;
	private String diff;
	
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}
	
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
	public Date getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}
	
	@SuppressWarnings("deprecation")
	public static Tweet fromJSONToTweet(JSONObject json){
		Tweet tweet = new Tweet();
		try {
			tweet.body = json.getString("text");
			tweet.id = json.getString("id_str");
			tweet.createdAt = json.getString("created_at");
			tweet.user = User.fromJSONToUser(json.getJSONObject("user"));
			tweet.timestamp = new SimpleDateFormat("E MMM dd hh:mm:ss z yyyy").parse(tweet.createdAt);
			tweet.diff = getDiffTime(tweet.timestamp);
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		} catch (ParseException e){
			e.printStackTrace();
			return null;
		}
		return tweet;
	}
	
	private static String getDiffTime(Date tweetTime){
		Date currentTime = Calendar.getInstance().getTime();
		long diffHours = (currentTime.getTime() - tweetTime.getTime())/(60*60*1000) % 24;
		if(diffHours == 0){
			return Long.toString((currentTime.getTime() - tweetTime.getTime())/(60*1000) % 60)+"m";
		}
		else
			return diffHours+"h";
		
	}
	public static ArrayList<Tweet> fromJSONArray(JSONArray jsonArray) {
		ArrayList<Tweet> tweets = new ArrayList<Tweet>(jsonArray.length());
	      // Process each result in json array, decode and convert to business object
	      for (int i=0; i < jsonArray.length(); i++) {
	          JSONObject json = null;
	          try {
	            json = jsonArray.getJSONObject(i);
	          } catch (Exception e) {
	              e.printStackTrace();
	              continue;
	          }

	          Tweet tweet = Tweet.fromJSONToTweet(json);
	          if (tweet != null) {
	            tweets.add(tweet);
	          }
	      }
	      return tweets;
	}
	
	@Override
	public String toString() {
		if(getUser()!=null)
			return getBody()+" - "+getUser().getScreenName();
		else
			return getBody()+" - NO USER";
	}
	public String getDiff() {
		return diff;
	}
	public void setDiff(String diff) {
		this.diff = diff;
	}
	
	
}
