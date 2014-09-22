package com.tejuapp.twitterclient;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.tejuapp.twitterclient.models.User;

public class ComposeActivity extends Activity {

	private User user;
	private ImageView ivProfileImage;
	private TextView tvName;
	private TextView tvScreenName;
	private EditText etCompose;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_compose);
		user = (User) getIntent().getSerializableExtra("user");
		
		// Show the Up button in the action bar.
		setupActionBar();
		setupActivity();
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {
		ActionBar actionBar = getActionBar();
	    actionBar.setDisplayHomeAsUpEnabled(false);
	    actionBar.setHomeButtonEnabled(false);
	    actionBar.setDisplayUseLogoEnabled(false);
	    actionBar.setCustomView(R.layout.compose_actionbar_view);
	    actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.compose, menu);
		return true;
	}
	
	private void setupActivity(){
		ivProfileImage = (ImageView) findViewById(R.id.ivComposeProfileImage);
		tvName = (TextView) findViewById(R.id.tvComposeUserName);
		tvScreenName = (TextView) findViewById(R.id.tvComposeScreenName);
		etCompose = (EditText) findViewById(R.id.etCompose);
		
		ivProfileImage.setImageResource(android.R.color.transparent);
		ImageLoader imageLoader = ImageLoader.getInstance();
		imageLoader.displayImage(user.getProfileImageUrl(), ivProfileImage);
		tvName.setText(user.getName());
		tvScreenName.setText(user.getScreenName());
	}
	
	public void onTweet(View v){
		String tweetText = etCompose.toString();
		if(tweetText.isEmpty()){
			Toast.makeText(this, "Compose and Tweet", Toast.LENGTH_SHORT).show();
		}
		else{
			Intent i = new Intent();
			i.putExtra("tweet", tweetText);
			setResult(RESULT_OK,i);
			finish();
		}
	}

}
