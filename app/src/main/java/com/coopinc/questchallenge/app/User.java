package com.coopinc.questchallenge.app;

import com.parse.ParseClassName;

import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseClassName;
import com.parse.ParseUser;

@ParseClassName("_User")
public class User extends ParseUser {
    public String getUserName() {
        return getString("username");
    }

    public ParseGeoPoint getLocation() {
        return getParseGeoPoint("location");
    }

    public void setLocation(ParseGeoPoint location) {
        put("location", location);
    }

    public void setUserName(String userName) {
       put("username", userName);
    }
    public int getAlignment () {
        return getInt("alignment");
    }
    public void setAlignment(int alignment) {
        put("alignment", alignment);
    }

    public String getName() {
        return getString("name");
    }

    public void setName(String name) {
        put("name", name);
    }

    public String getImageUrl() {
        return getString("imageUrl");
    }

    public void setImageUrl(String imageUrl) {
        put("imageUrl", imageUrl);
    }
    public ParseFile getUserImage () {
        return getParseFile("userImage");
    }
    public void setUserImage (ParseFile image) {
        put("userImage", image);
    }

}
