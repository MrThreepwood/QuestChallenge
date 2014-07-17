package com.coopinc.questchallenge.app;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class MainActivity extends ActionBarActivity {
    View mainContainer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.container_main);
        if (findViewById(R.id.main_container) != null) {
            mainContainer = findViewById(R.id.main_container);
            LoginFragment login = new LoginFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.main_container, login).commit();
        }
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
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void fragmentSwap(android.support.v4.app.Fragment current, android.support.v4.app.Fragment next) {
        android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(mainContainer.getId(), next);
        ft.addToBackStack(null);
        ft.commit();
    }
}
