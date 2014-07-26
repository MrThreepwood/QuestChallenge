package com.coopinc.questchallenge.app;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {
    View mainContainer;
    private boolean started = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.container_main);
        if (savedInstanceState == null) {
            mainContainer = findViewById(R.id.main_container);
            if (((ApplicationInfo)getApplicationContext()).loggedUser == null) {
                LoginFragment login = new LoginFragment();
                getSupportFragmentManager().beginTransaction().add(R.id.main_container, login).commit();
            } else {
                QuestListFragment questList = new QuestListFragment();
                getSupportFragmentManager().beginTransaction().add(R.id.main_container, questList).commit();
            }
        }
    }
    public boolean checkConnectionMaybeQuery (boolean query) {
        ConnectivityManager connection = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connection != null) {
            NetworkInfo[] info = connection.getAllNetworkInfo();
            if (info != null) {
                for (int n = 0; n < info.length; n++) {
                    if (info[n].getState() == NetworkInfo.State.CONNECTED) {
                        cacheParseQuestsQuery();
                        cacheParseUsersQuery();
                        return true;
                    }
                }
            }
        }
        return false;
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
                    else {
                        //TODO:Display server contact issues.
                    }
                }
                else {
                    Log.d("parse error", "error with query" + e.getMessage());
                }
            }
        });
    }
    public void cacheParseUsersQuery () {
        ParseQuery<User> query = ParseQuery.getQuery("_Users");
        query.findInBackground(new FindCallback<User>() {
            @Override
            public void done(List<User> users, ParseException e) {
                if ( e == null ) {
                    if (users != null) {
                        User.pinAllInBackground(users);
                    }
                    else {
                        //TODO: Display that we couldn't contact the servers.
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
        if (id == R.id.action_settings && ((ApplicationInfo)getApplicationContext()).loggedUser != null) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        } else {
            Toast.makeText(this, "Please login first", Toast.LENGTH_LONG).show();
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
