package com.tejuapp.twitterclient;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.tejuapp.twitterclient.adapter.TweetArrayAdapter;
import com.tejuapp.twitterclient.models.Tweet;
import com.tejuapp.twitterclient.models.User;

public class TimelineActivity extends Activity {
	
	private TwitterClient client;
	private ArrayList<Tweet> tweets;
	private ArrayAdapter<Tweet> aTweets;
	private ListView lvTweets;
	private User currentUser;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timeline);
		setupActivity();
		client = TwitterApplication.getRestClient();
		populateTimeline();
		getAccountDetails();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.timeline, menu);
		return true;
	}
	
	private void setupActivity(){
		lvTweets = (ListView) findViewById(R.id.lvTweets);
		tweets = new ArrayList<Tweet>();
		aTweets = new TweetArrayAdapter(this, tweets);
		lvTweets.setAdapter(aTweets);
	}
	
	public void populateTimeline(){
		client.getHomeTimeline(new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(JSONArray json) {
				aTweets.addAll(Tweet.fromJSONArray(json));
				Log.d("DEBUG",json.toString());
			}
			
			@Override
			public void onFailure(Throwable e, String s) {
				Log.d("DEBUG",e.toString());
				Log.d("DEBUG",s.toString());
			}
		});
	}
	
	public void getAccountDetails(){
		client.getAccountDetails(new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(JSONObject json) {
				currentUser = User.fromJSONToUser(json);
				Log.d("DEBUG","USER>> "+json.toString());
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
    			String composeTweet = (String) data.getSerializableExtra("tweet");
    			postTweet(composeTweet);
    			aTweets.clear();
    			populateTimeline();
    			Log.d("HELP-SITE","Tweet is "+composeTweet);
    			//Toast.makeText(this, settings.getSize(), Toast.LENGTH_SHORT).show();
    		}
    	}
    }
	
	public void postTweet(String status){
		client.postUpdateTweet(new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(JSONObject json) {
				currentUser = User.fromJSONToUser(json);
				Log.d("DEBUG","USER>> "+json.toString());
			}
			
			@Override
			public void onFailure(Throwable e, String s) {
				Log.d("DEBUG",e.toString());
				Log.d("DEBUG",s.toString());
			}
		}, status);
	}

}
