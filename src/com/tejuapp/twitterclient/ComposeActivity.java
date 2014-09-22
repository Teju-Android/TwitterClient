package com.tejuapp.twitterclient;

import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tejuapp.twitterclient.models.User;

public class ComposeActivity extends Activity {

	private User user;
	private ImageView ivProfileImage;
	private TextView tvName;
	private TextView tvScreenName;
	private EditText etCompose;
	private Button btTweet;
	private TwitterClient client;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_compose);
		client = TwitterApplication.getRestClient();
		user = (User) getIntent().getSerializableExtra("user");
		getActionBar().hide();
		// Show the Up button in the action bar.
		//setupActionBar();
		setupActivity();
		setupComposeListener();
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	/*private void setupActionBar() {
		ActionBar actionBar = getActionBar();
	    actionBar.setDisplayHomeAsUpEnabled(false);
	    actionBar.setHomeButtonEnabled(false);
	    actionBar.setDisplayUseLogoEnabled(false);
	    actionBar.setDisplayShowHomeEnabled(false);
	    actionBar.setCustomView(R.layout.compose_actionbar_view);
	    actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.hide();
        View v = actionBar.getCustomView();
        btTweet = (Button)v.findViewById(R.id.btTweet);
        btTweet.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				String tweetText = etCompose.getText().toString();
				if(tweetText.isEmpty()){
					Toast.makeText(getBaseContext(), "Compose and Tweet", Toast.LENGTH_SHORT).show();
				}
				else{
					postTweet(tweetText);
					Intent i = new Intent();
					i.putExtra("tweet", tweetText);
					setResult(RESULT_OK,i);
					finish();
				}
			}
        });
	}*/

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
		btTweet = (Button) findViewById(R.id.btTweet);
		
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
	
	public void setupComposeListener(){
		etCompose.addTextChangedListener(new TextWatcher(){
	        public void afterTextChanged(Editable s) {
	            String tweet = etCompose.getText().toString();
	            if(tweet.isEmpty()){
	            	btTweet.setBackgroundResource(R.drawable.tweet_false);
	            }
	            else{
	            	btTweet.setBackgroundResource(R.drawable.tweet_true);
	            } 	
	        }
	        public void beforeTextChanged(CharSequence s, int start, int count, int after){}
	        public void onTextChanged(CharSequence s, int start, int before, int count){}
	    }); 
	}
	
	public void postTweet(String status){
		client.postUpdateTweet(new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(JSONObject json) {
				Log.d("DEBUG","USER>> POSTED!!!");
			}
			
			@Override
			public void onFailure(Throwable e, String s) {
				Log.d("DEBUG",e.toString());
				Log.d("DEBUG",s.toString());
			}
		}, status);
	}
	
	public void onPostTweet(View v) {
		String tweetText = etCompose.getText().toString();
		if(tweetText.isEmpty()){
			Toast.makeText(getBaseContext(), "Compose and Tweet", Toast.LENGTH_SHORT).show();
		}
		else{
			postTweet(tweetText);
			Intent i = new Intent();
			i.putExtra("tweet", tweetText);
			setResult(RESULT_OK,i);
			finish();
		}
	}

}
