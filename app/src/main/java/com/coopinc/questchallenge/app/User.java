package com.coopinc.questchallenge.app;

import com.parse.ParseClassName;

import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseClassName;
import com.parse.ParseUser;

@ParseClassName("_User")
public class User extends ParseUser {
    public String getUserName() {
        return getString("userName");
    }
    private ParseGeoPoint location;

    public ParseGeoPoint getLocation() {
        return getParseGeoPoint("location");
    }

    public void setLocation(ParseGeoPoint location) {
        put("location", location);
    }

    public void setUserName(String userName) {
       put("userName", userName);
    }
    public String getAlignment () {
        return getString("Alignment");
    }
    public void setAlignment(int alignment) {
        put("alignment", alignment);
    }
}
