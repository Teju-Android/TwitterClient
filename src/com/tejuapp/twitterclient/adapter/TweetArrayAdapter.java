package com.tejuapp.twitterclient.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.tejuapp.twitterclient.R;
import com.tejuapp.twitterclient.models.Tweet;

public class TweetArrayAdapter extends ArrayAdapter<Tweet> {

	public TweetArrayAdapter(Context context, List<Tweet> objects) {
		super(context, 0, objects);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Tweet tweet = getItem(position);
		if(convertView == null){
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.tweet_item, parent,false);
		}
		ImageView ivProfileImage = (ImageView) convertView.findViewById(R.id.ivComposeProfileImage);
		TextView tvScreenName = (TextView) convertView.findViewById(R.id.tvScreenName);
		TextView tvBody = (TextView) convertView.findViewById(R.id.tvBody);
		TextView tvDiffTime = (TextView) convertView.findViewById(R.id.tvDiffTime);
		TextView tvUserName = (TextView) convertView.findViewById(R.id.tvUserName);
		
		ivProfileImage.setImageResource(android.R.color.transparent);
		ImageLoader imageLoader = ImageLoader.getInstance();
		
		//populate the tweets with data
		imageLoader.displayImage(tweet.getUser().getProfileImageUrl(), ivProfileImage);
		tvScreenName.setText("@"+tweet.getUser().getScreenName());
		tvBody.setText(tweet.getBody());
		tvDiffTime.setText(tweet.getDiff());
		tvUserName.setText(tweet.getUser().getName());
		return convertView;
	}

}
