package com.tejuapp.twitterclient.models;

import java.io.Serializable;
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

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Column.ForeignKeyAction;
import com.activeandroid.annotation.Table;

@Table(name = "Tweet")
public class Tweet extends Model implements Serializable{
	
	private static final long serialVersionUID = 7865554975454958010L;
	@Column(name = "body")
	private String body;
	
	@Column(name = "tid", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
	private long tid;
	
	@Column(name = "createdAt")
	private String createdAt;
	
	@Column(name = "user", onUpdate = ForeignKeyAction.CASCADE, onDelete = ForeignKeyAction.CASCADE)
	private User user;
	
	@Column(name = "timestamp")
	private Date timestamp;
	private String diff;
	
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
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
			tweet.tid = json.getLong("id_str");
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
		tweet.save();
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
		Log.d("DEBUG","The number of tweets got is :"+jsonArray.length());
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
	
	public String getDiff() {
		return diff;
	}
	public void setDiff(String diff) {
		this.diff = diff;
	}
	
	public Tweet(){
		super();	
	}
	
	
}
