package com.tejuapp.twitterclient.fragments;

import java.util.ArrayList;

import org.json.JSONArray;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.tejuapp.twitterclient.R;
import com.tejuapp.twitterclient.TwitterApplication;
import com.tejuapp.twitterclient.TwitterClient;
import com.tejuapp.twitterclient.adapter.TweetArrayAdapter;
import com.tejuapp.twitterclient.listener.EndlessScrollListener;
import com.tejuapp.twitterclient.models.Tweet;

import eu.erikw.PullToRefreshListView;
import eu.erikw.PullToRefreshListView.OnRefreshListener;

public class TweetsListFragment extends Fragment {
	
	protected PullToRefreshListView lvTweets;
	protected ArrayList<Tweet> tweets;
	protected ArrayAdapter<Tweet> aTweets;
	protected String lastTweetId=null;
	protected String lastMentionTweetId=null;
	View view;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_tweets_list, container, false);
		setupView();
		return view;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setup();
	}
	
	private void setup(){
		tweets = new ArrayList<Tweet>();
		aTweets = new TweetArrayAdapter(getActivity(), tweets);
	}
	
	private void setupView(){
		lvTweets = (PullToRefreshListView) view.findViewById(R.id.lvTweets);
		lvTweets.setAdapter(aTweets);
	}
	
	public void addAll(ArrayList<Tweet> tweets){
		aTweets.addAll(tweets);
	}
	
	protected void setOnRefreshListenerForTweets(OnRefreshListener onRefreshListener){
		lvTweets.setOnRefreshListener(onRefreshListener);
	}
	
	protected void setOnScrollListenerForTweets(EndlessScrollListener endlessScrollListener){
		lvTweets.setOnScrollListener(endlessScrollListener);
	}
	
}
