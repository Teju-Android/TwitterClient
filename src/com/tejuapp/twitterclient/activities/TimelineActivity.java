package com.tejuapp.twitterclient.activities;

import org.json.JSONObject;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.tejuapp.twitterclient.R;
import com.tejuapp.twitterclient.TwitterApplication;
import com.tejuapp.twitterclient.TwitterClient;
import com.tejuapp.twitterclient.fragments.HomeTimelineFragment;
import com.tejuapp.twitterclient.fragments.MentionsTimelineFragment;
import com.tejuapp.twitterclient.listener.FragmentTabListener;
import com.tejuapp.twitterclient.models.User;

public class TimelineActivity extends FragmentActivity {
	
	private User currentUser;
	private TwitterClient client;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		client = TwitterApplication.getRestClient();
		setContentView(R.layout.activity_timeline);
		setupTabs();

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
	
	private void setupTabs() {
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayShowTitleEnabled(false);

		Tab homeTab = actionBar
			.newTab()
			.setIcon(R.drawable.ic_home)
			.setTag("HomeTimelineFragment")
			.setTabListener(
				new FragmentTabListener<HomeTimelineFragment>(R.id.flContainer, this, "first",
								HomeTimelineFragment.class));

		actionBar.addTab(homeTab);
		actionBar.selectTab(homeTab);

		Tab mentionsTab = actionBar
			.newTab()
			.setIcon(R.drawable.ic_home)
			.setTag("MentionsTimelineFragment")
			.setTabListener(
			    new FragmentTabListener<MentionsTimelineFragment>(R.id.flContainer, this, "second",
								MentionsTimelineFragment.class));

		actionBar.addTab(mentionsTab);
	}
}
