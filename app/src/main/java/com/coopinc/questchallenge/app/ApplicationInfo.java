package com.coopinc.questchallenge.app;

import android.app.Application;

import com.coopinc.questchallenge.app.MainActivity;
import com.coopinc.questchallenge.app.QuestInfo;
import com.coopinc.questchallenge.app.User;
import com.parse.Parse;
import com.parse.ParseObject;

/**
 * Created by Lori Breitbarth on 7/26/14.
 */
public class ApplicationInfo extends Application {
    public boolean loggedIn = false;
    public User loggedUser;
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.enableLocalDatastore(this);
        ParseObject.registerSubclass(User.class);
        ParseObject.registerSubclass(QuestInfo.class);
        Parse.initialize(this, "ZABSkDfHCfLS3Ad1HXZgNDkK6VGaJ03aAqH2P2an", "R5dqUkiw5CEM5Ghb13Fy1Cww0kDWykoiIIH6RVRE");
    }
}
