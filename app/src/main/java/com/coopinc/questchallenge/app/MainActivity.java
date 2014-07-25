package com.coopinc.questchallenge.app;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {
    View mainContainer;
    public ArrayList<QuestInfo> quests;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.container_main);
        Parse.enableLocalDatastore(this);
        ParseObject.registerSubclass(User.class);
        ParseObject.registerSubclass(QuestInfo.class);
        Parse.initialize(this, "ZABSkDfHCfLS3Ad1HXZgNDkK6VGaJ03aAqH2P2an", "R5dqUkiw5CEM5Ghb13Fy1Cww0kDWykoiIIH6RVRE");
        cacheParseQuestsQuery();
        if (savedInstanceState == null) {
            if (findViewById(R.id.main_container) != null) {

                mainContainer = findViewById(R.id.main_container);
                LoginFragment login = new LoginFragment();
                getSupportFragmentManager().beginTransaction().add(R.id.main_container, login).commit();
            }
        }
    }
    //Poor replacement for .PinAllInBackground
    public void cacheParseQuestsQuery() {
        ParseQuery<QuestInfo> query = ParseQuery.getQuery("Quests");
        query.include("questGiver");
        query.findInBackground(new FindCallback<QuestInfo>() {
            @Override
            public void done(List<QuestInfo> questInfos, ParseException e) {
                if (e == null) {
                    if(questInfos != null) {
                        QuestInfo.pinAllInBackground(questInfos);
                    }
                }
                else {
                    Log.d("parse error", "error with query" + e.getMessage());
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void fragmentSwap(Fragment current, Fragment next, Bundle args, Boolean addToBackStack) {
        if (args != null) {
            next.setArguments(args);
        }
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(mainContainer.getId(), next);
        if (addToBackStack) {
            ft.addToBackStack(null);
        }
        ft.commit();
    }
}
