package com.tejuapp.twitterclient.activities;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.tejuapp.twitterclient.R;
import com.tejuapp.twitterclient.TwitterApplication;
import com.tejuapp.twitterclient.TwitterClient;
import com.tejuapp.twitterclient.R.layout;
import com.tejuapp.twitterclient.R.menu;
import com.tejuapp.twitterclient.adapter.TweetArrayAdapter;
import com.tejuapp.twitterclient.fragments.TweetsListFragment;
import com.tejuapp.twitterclient.listener.EndlessScrollListener;
import com.tejuapp.twitterclient.models.Tweet;
import com.tejuapp.twitterclient.models.User;

import eu.erikw.PullToRefreshListView;
import eu.erikw.PullToRefreshListView.OnRefreshListener;

public class TimelineActivity extends FragmentActivity {
	
	private User currentUser;
	private TwitterClient client;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		client = TwitterApplication.getRestClient();
		setContentView(R.layout.activity_timeline);
		
		ActionBar actionBar = getActionBar();
	    actionBar.setDisplayShowTitleEnabled(false);

//		populateTimeline();
		getAccountDetails();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.timeline, menu);
		return true;
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
    			String composeTweet = (String) data.getStringExtra("tweet");
    			/*tweets.clear();
    			lastTweetId = null;
    			populateTimeline();*/
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
