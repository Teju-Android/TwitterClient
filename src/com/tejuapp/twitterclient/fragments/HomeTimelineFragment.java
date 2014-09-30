package com.tejuapp.twitterclient.fragments;

import org.json.JSONArray;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.tejuapp.twitterclient.TwitterApplication;
import com.tejuapp.twitterclient.TwitterClient;
import com.tejuapp.twitterclient.listener.EndlessScrollListener;
import com.tejuapp.twitterclient.models.Tweet;

public class HomeTimelineFragment extends TweetsListFragment {
	
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
            	lastTweetId = null;
            	firstTweetId=Long.MIN_VALUE;
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
		String max_id=null;
		String since_id=null;
		if(lastTweetId!=null){
			max_id = lastTweetId.toString();
		}
		
		if(firstTweetId!=Long.MIN_VALUE){
			since_id = firstTweetId.toString();
		}
		
				
		client.getHomeTimeline(new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(JSONArray json) {
				addAll(Tweet.fromJSONArray(json));
				lastTweetId = tweets.get(tweets.size()-1).getId();
				firstTweetId = tweets.get(0).getId();
				Log.d("DEBUG", tweets.get(tweets.size()-1).getBody());
				swipeContainer.setRefreshing(false);
			}
			
			@Override
			public void onFailure(Throwable e, String s) {
				Log.d("DEBUG",e.toString());
				Log.d("DEBUG",s.toString());
			}
			@Override
			protected void handleFailureMessage(Throwable arg0, String arg1) {
				Toast.makeText(getActivity(), "Network Request Failure", Toast.LENGTH_SHORT).show();
			}
		}, max_id, since_id);
	}
	

}
