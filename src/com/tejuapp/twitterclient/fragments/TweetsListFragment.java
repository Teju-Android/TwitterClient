package com.tejuapp.twitterclient.fragments;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.tejuapp.twitterclient.R;
import com.tejuapp.twitterclient.adapter.TweetArrayAdapter;
import com.tejuapp.twitterclient.listener.EndlessScrollListener;
import com.tejuapp.twitterclient.models.Tweet;

public class TweetsListFragment extends Fragment {
	
	protected ListView lvTweets;
	protected ArrayList<Tweet> tweets;
	protected ArrayAdapter<Tweet> aTweets;
	protected Long lastTweetId=null;
	protected Long firstTweetId=Long.MIN_VALUE;
	protected Long lastMentionTweetId=null;
	protected Long firstMentionTweetId=Long.MIN_VALUE;
	protected boolean enableHomeTimelineRequest = true;
	protected boolean enableMentionsTimelineRequest = true;
	protected final int DEFAULT_TWEET_COUNT = 20;
	protected SwipeRefreshLayout swipeContainer;
	
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
		lvTweets = (ListView) view.findViewById(R.id.lvTweets);
		lvTweets.setAdapter(aTweets);
		swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
	}
	
	public void addAll(ArrayList<Tweet> new_tweets){
		ArrayList<Tweet> add_tweets = new ArrayList<Tweet>();
		add_tweets.addAll(new_tweets);
		add_tweets.addAll(tweets);
		tweets = add_tweets;	
		aTweets.addAll(tweets);
	}
	
	protected void setOnRefreshListenerForTweets(OnRefreshListener onRefreshListener){
		swipeContainer.setOnRefreshListener(onRefreshListener);
	}
	
	protected void setOnScrollListenerForTweets(EndlessScrollListener endlessScrollListener){
		lvTweets.setOnScrollListener(endlessScrollListener);
	}
	
}
