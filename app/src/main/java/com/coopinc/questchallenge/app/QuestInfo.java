package com.coopinc.questchallenge.app;

import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseClassName;
import com.parse.ParseUser;

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
        return getString("name");
    }

    public void setQuestName(String questName) {
        put("name",questName);
    }

    public String getQuestGiver() {
        /*
        String name = questGiver.getUsername();
        return name;*/
        return "hi";
    }

    public void setQuestGiver(String questGiver) {
        put("questGiver",questGiver);
    }
}
