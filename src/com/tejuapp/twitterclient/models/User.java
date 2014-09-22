package com.tejuapp.twitterclient.models;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

public class User implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 9108915029551725815L;
	private String name;
	private long uid;
	private String screenName;
	private String profileImageUrl;

	public static User fromJSONToUser(JSONObject json) {
		User u = new User();
		try {
			u.name = json.getString("name");
			u.uid = json.getLong("id");
			u.screenName = json.getString("screen_name");
			u.profileImageUrl = json.getString("profile_image_url");
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		return u;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getUid() {
		return uid;
	}

	public void setUid(long uid) {
		this.uid = uid;
	}

	public String getScreenName() {
		return screenName;
	}

	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}

	public String getProfileImageUrl() {
		return profileImageUrl;
	}

	public void setProfileImageUrl(String profileImageUrl) {
		this.profileImageUrl = profileImageUrl;
	}

}
