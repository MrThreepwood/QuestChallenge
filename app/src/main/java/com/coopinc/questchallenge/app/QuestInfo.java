package com.coopinc.questchallenge.app;

import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseClassName;

@ParseClassName("Quests")
public class QuestInfo extends ParseObject {
    public String getAcceptedBy() {
        return getString("acceptedBy");
    }
    public void setAcceptedBy(String value) {
        put("AcceptedBy", value);
    }

    public int getAlignment() {
        return getInt("alignment");
    }

    public void setAlignment(int alignment) {
        put("alignment", alignment);
    }

    public boolean isCompleted() {
        return getBoolean("completed");
    }

    public void setCompleted(boolean completed) {
        put("completed", completed);
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
        return getString("questName");
    }

    public void setQuestName(String questName) {
        put("questName",questName);
    }

    public String getQuestGiver() {
        return getString("questGiver");
    }

    public void setQuestGiver(String questGiver) {
        put("questGiver",questGiver);
    }

    String objectId;
    String acceptedBy;
    int alignment;
    boolean completed;
    String description;
    ParseGeoPoint location;
    String locationImageUrl;
    String questName;
    String questGiver;
}
