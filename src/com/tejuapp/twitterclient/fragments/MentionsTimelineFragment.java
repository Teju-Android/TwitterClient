package com.tejuapp.twitterclient.fragments;

import org.json.JSONArray;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.tejuapp.twitterclient.TwitterApplication;
import com.tejuapp.twitterclient.TwitterClient;
import com.tejuapp.twitterclient.listener.EndlessScrollListener;
import com.tejuapp.twitterclient.models.Tweet;

import eu.erikw.PullToRefreshListView.OnRefreshListener;

public class MentionsTimelineFragment extends TweetsListFragment {
	
private TwitterClient client;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		client = TwitterApplication.getRestClient();
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
                // Make sure you call listView.onRefreshComplete() when
                // once the network request has completed successfully.
            	tweets.clear();
            	lastMentionTweetId = null;
                populateTimeline();
            }
        });
		
		setOnScrollListenerForTweets(new EndlessScrollListener() {
		    @Override
		    public void onLoadMore(int page, int totalItemsCount) {
	                // Triggered only when new data needs to be appended to the list
	                // Add whatever code is needed to append new items to your AdapterView
		    	populateTimeline();
		    }
		});
	}
	
	public void populateTimeline(){
		client.getMentionsTimeline(new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(JSONArray json) {
				lvTweets.onRefreshComplete();
				addAll(Tweet.fromJSONArray(json));
				lastMentionTweetId = tweets.get(tweets.size()-1).getId();
				Log.d("DEBUG", tweets.get(tweets.size()-1).getBody());
			}
			
			@Override
			public void onFailure(Throwable e, String s) {
				Log.d("DEBUG",e.toString());
				Log.d("DEBUG",s.toString());
			}
			
		}, lastMentionTweetId);
	}

}
