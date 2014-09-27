package com.tejuapp.twitterclient;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.tejuapp.twitterclient.adapter.TweetArrayAdapter;
import com.tejuapp.twitterclient.listener.EndlessScrollListener;
import com.tejuapp.twitterclient.models.Tweet;
import com.tejuapp.twitterclient.models.User;

import eu.erikw.PullToRefreshListView;
import eu.erikw.PullToRefreshListView.OnRefreshListener;

public class TimelineActivity extends Activity {
	
	private TwitterClient client;
	private ArrayList<Tweet> tweets;
	private ArrayAdapter<Tweet> aTweets;
	private PullToRefreshListView lvTweets;
	private User currentUser;
	private String lastTweetId=null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timeline);
		client = TwitterApplication.getRestClient();
		ActionBar actionBar = getActionBar();
	    actionBar.setDisplayShowTitleEnabled(false);
	    
	    setupActivity();
//		populateTimeline();
		getAccountDetails();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.timeline, menu);
		return true;
	}
	
	private void setupActivity(){
		lvTweets = (PullToRefreshListView) findViewById(R.id.lvTweets);
		tweets = new ArrayList<Tweet>();
		aTweets = new TweetArrayAdapter(this, tweets);
		lvTweets.setAdapter(aTweets);
		lvTweets.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call listView.onRefreshComplete() when
                // once the network request has completed successfully.
            	tweets.clear();
            	lastTweetId = null;
                populateTimeline();
            }
        });
		
		lvTweets.setOnScrollListener(new EndlessScrollListener() {
		    @Override
		    public void onLoadMore(int page, int totalItemsCount) {
	                // Triggered only when new data needs to be appended to the list
	                // Add whatever code is needed to append new items to your AdapterView
		    	populateTimeline();
		    }
		});
	}
	
	public void populateTimeline(){
		client.getHomeTimeline(new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(JSONArray json) {
				lvTweets.onRefreshComplete();
				aTweets.addAll(Tweet.fromJSONArray(json));
				lastTweetId = tweets.get(tweets.size()-1).getId();
				Log.d("DEBUG", tweets.get(tweets.size()-1).getBody());
			}
			
			@Override
			public void onFailure(Throwable e, String s) {
				Log.d("DEBUG",e.toString());
				Log.d("DEBUG",s.toString());
			}
		}, lastTweetId);
	}
	
	public void getAccountDetails(){
		client.getAccountDetails(new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(JSONObject json) {
				currentUser = User.fromJSONToUser(json);
			}
			
			@Override
			public void onFailure(Throwable e, String s) {
				Log.d("DEBUG",e.toString());
				Log.d("DEBUG",s.toString());
			}
		});
	}
	
	public void onCompose(MenuItem mi){
		Intent i = new Intent(this, ComposeActivity.class);
		i.putExtra("user", currentUser);
		startActivityForResult(i, 5);
	}
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	// TODO Auto-generated method stub
    	if(requestCode ==5){
    		if(resultCode == RESULT_OK){
    			String composeTweet = (String) data.getStringExtra("tweet");
    			tweets.clear();
    			lastTweetId = null;
    			populateTimeline();
    			Log.d("DEBUG","Tweet is "+composeTweet);
    			//Toast.makeText(this, settings.getSize(), Toast.LENGTH_SHORT).show();
    		}
    	}
    }
	
	public void onLogout(MenuItem mi){
		client.clearAccessToken();
		Intent i = new Intent(this, LoginActivity.class);
		startActivity(i);
		finish();
	}
}
