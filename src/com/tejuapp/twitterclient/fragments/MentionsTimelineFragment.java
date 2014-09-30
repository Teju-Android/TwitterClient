package com.tejuapp.twitterclient.fragments;

import org.json.JSONArray;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.tejuapp.twitterclient.TwitterApplication;
import com.tejuapp.twitterclient.TwitterClient;
import com.tejuapp.twitterclient.listener.EndlessScrollListener;
import com.tejuapp.twitterclient.models.Tweet;

public class MentionsTimelineFragment extends TweetsListFragment {
	
private TwitterClient client;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		client = TwitterApplication.getRestClient();
		populateTimeline();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		setListeners();
		return view;
	}
	
	private void setListeners(){
		
		setOnRefreshListenerForTweets(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
            	aTweets.clear();
            	lastMentionTweetId = null;
            	firstMentionTweetId=Long.MIN_VALUE;
            	enableMentionsTimelineRequest = true;
                populateTimeline();
            } 
        });
		
		setOnScrollListenerForTweets(new EndlessScrollListener() {
		    @Override
		    public void onLoadMore(int page, int totalItemsCount) {
	                // Triggered only when new data needs to be appended to the list
	                // Add whatever code is needed to append new items to your AdapterView
		    	if(enableMentionsTimelineRequest){
		    		populateTimeline();
		    	}
		    }
		});
	}
	
	public void populateTimeline(){
		String max_id=null;
		String since_id=null;
		if(lastMentionTweetId!=null){
			max_id = lastMentionTweetId.toString();
		}
		
		if(firstMentionTweetId!=Long.MIN_VALUE){
			since_id = lastMentionTweetId.toString();
		}
		client.getMentionsTimeline(new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(JSONArray json) {
				int old_tweets_number = tweets.size();
				addAll(Tweet.fromJSONArray(json));
				int fetchedTweetCount = tweets.size() - old_tweets_number;
				setEnableMentionsRequest(fetchedTweetCount);
				lastMentionTweetId = tweets.get(tweets.size()-1).getId();
				firstMentionTweetId = tweets.get(0).getId();
				Log.d("DEBUG", tweets.get(tweets.size()-1).getBody());
				swipeContainer.setRefreshing(false);
			}
			
			@Override
			public void onFailure(Throwable e, String s) {
				Log.d("DEBUG",e.toString());
				Log.d("DEBUG",s.toString());
			}
			
		}, max_id, since_id);
	}
	
	private void setEnableMentionsRequest(int tweetsGained){
		if(tweetsGained < DEFAULT_TWEET_COUNT){
			enableMentionsTimelineRequest = false;
		}
	}

}
