package com.coopinc.questchallenge.app;

import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseClassName;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

@ParseClassName("Quests")
public class QuestInfo extends ParseObject {

    public List<String> getAcceptedBy() {
        JSONArray jsonArray = getJSONArray("acceptedBy");
        List<String> acceptedBy = new ArrayList<String>();
        for (int n = 0; n < jsonArray.length(); n++) {
            try {
                acceptedBy.add(jsonArray.getString(n));
            } catch (JSONException exception) {
            }
        }
        return acceptedBy;
    }
    public void addAcceptedBy(String value) {
        addUnique("acceptedBy", value);
    }
    public void removeAcceptedyBy(String value) {
        List<String> userID = new ArrayList<String>();
        userID.add(value);
        removeAll("acceptedBy", userID);
    }
    public List<String> getCompletedBy() {
        JSONArray jsonArray = getJSONArray("completedBy");
        List<String> completedBy = new ArrayList<String>();
        for (int n = 0; n< jsonArray.length(); n++) {
            try {
                completedBy.add(jsonArray.getString(n));
            } catch (JSONException exception) {
            }
        }
        return completedBy;
    }
    public void setCompletedBy(String value) {
        addUnique("completedBy", value);
    }

    public int getAlignment() {
        return getInt("alignment");
    }

    public void setAlignment(int alignment) {
        put("alignment", alignment);
    }

    public String getDescription() {
        return getString("description");
    }

    public void setDescription(String description) {
        put("description",description);
    }

    public ParseGeoPoint getLocation() {
        return getParseGeoPoint("location");
    }

    public void setLocation(ParseGeoPoint location) {
        put("location", location);
    }

    public String getLocationImageUrl() {
        return getString("locationImageUrl");
    }

    public void setLocationImageUrl(String locationImageUrl) {
        put("locationImageUrl",locationImageUrl);
    }

    public String getQuestName() {
        return getString("name");
    }

    public void setQuestName(String questName) {
        put("name",questName);
    }

    public String getQuestGiver() {
        ParseUser user = getParseUser("questGiver");
        return user.getString("name");
    }

    public void setQuestGiver(String questGiver) {
        put("questGiver",questGiver);
    }
}
