package com.tejuapp.twitterclient;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.tejuapp.twitterclient.models.Tweet;

public class ViewTweetActivity extends Activity {

	private Tweet tweet;
	private ImageView ivProfileImage;
	private TextView tvUsername;
	private TextView tvScreenName;
	private TextView tvBody;
	private TextView tvTimestamp;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_tweet);
		// Show the Up button in the action bar.
		setupActionBar();
		tweet = (Tweet) getIntent().getSerializableExtra("tweet");
		Log.d("DEBUG","FROM VIEW ACTIVITY------> "+tweet.getUser().getName());
		setupActivity();
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view_tweet, menu);
		return true;
	}
	
	private void setupActivity(){
		ivProfileImage = (ImageView) findViewById(R.id.ivViewProfileImage);
		tvUsername = (TextView) findViewById(R.id.tvViewName);
		tvScreenName = (TextView) findViewById(R.id.tvViewScreenName);
		tvBody = (TextView) findViewById(R.id.tvViewBody);
		tvTimestamp = (TextView) findViewById(R.id.tvViewTimestamp);
		
		ivProfileImage.setImageResource(android.R.color.transparent);
		ImageLoader imageLoader = ImageLoader.getInstance();
		imageLoader.displayImage(tweet.getUser().getProfileImageUrl(), ivProfileImage);
		
		tvUsername.setText(tweet.getUser().getName());
		tvScreenName.setText("@"+tweet.getUser().getScreenName());
		tvBody.setText(tweet.getBody());
		tvTimestamp.setText(tweet.getCreatedAt());
		
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onBackPressed() {
		finish();
		return;
	}

}
