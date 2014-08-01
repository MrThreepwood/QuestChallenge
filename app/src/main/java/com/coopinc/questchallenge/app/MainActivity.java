package com.coopinc.questchallenge.app;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.List;


public class MainActivity extends ActionBarActivity {
    View mainContainer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.container_main);
        if (savedInstanceState == null) {
            mainContainer = findViewById(R.id.main_container);
            if (ParseUser.getCurrentUser() == null) {
                LoginFragment login = new LoginFragment();
                getSupportFragmentManager().beginTransaction().add(mainContainer.getId(), login).commit();
            } else {
                QuestsViewPager viewPager = new QuestsViewPager();
                getSupportFragmentManager().beginTransaction().add(mainContainer.getId(), viewPager).commit();
            }
        }
    }
    public boolean checkConnectionMaybeQuery () {
        ConnectivityManager connection = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connection != null) {
            NetworkInfo[] info = connection.getAllNetworkInfo();
            if (info != null) {
                for (NetworkInfo anInfo : info) {
                    if (anInfo.getState() == NetworkInfo.State.CONNECTED) {
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
            public void done(final List<QuestInfo> questInfos, ParseException e) {
                if (e == null) {
                    if(questInfos != null) {
                        QuestInfo.unpinAllInBackground(new DeleteCallback() {
                            @Override
                            public void done(ParseException e) {
                                QuestInfo.pinAllInBackground(questInfos);
                            }
                        });
                    }
                    else {
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.cant_contact_parse), Toast.LENGTH_LONG).show();
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
            public void done(final List<User> users, ParseException e) {
                if ( e == null ) {
                    if (users != null) {
                        QuestInfo.unpinAllInBackground(new DeleteCallback() {
                            @Override
                            public void done(ParseException e) {
                                User.pinAllInBackground(users);
                            }
                        });
                    }
                    else {
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.cant_contact_parse), Toast.LENGTH_LONG).show();
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
        menu.add("Log off");
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings && ParseUser.getCurrentUser() != null) {
            startSettingsActivity();
            return true;
        }
        if (item.getTitle().toString().equalsIgnoreCase("Log off")) {
            while (getSupportFragmentManager().popBackStackImmediate());
            ParseUser.logOut();
            fragmentSwap(new LoginFragment(), null, false);
            return true;
        }
        Toast.makeText(this, "Please login first", Toast.LENGTH_LONG).show();
        return super.onOptionsItemSelected(item);
    }
    public void fragmentSwap(Fragment next, Bundle args, Boolean addToBackStack) {
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
    public void startSettingsActivity () {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }
}
