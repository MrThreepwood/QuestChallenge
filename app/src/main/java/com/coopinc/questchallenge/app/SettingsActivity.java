package com.coopinc.questchallenge.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.parse.ParseFacebookUtils;
import com.parse.ParseQuery;

/**
 * Created by Guybrush on 7/18/2014.
 */
public class SettingsActivity extends Activity {
    Spinner alignmentSpinner;
    EditText displayName;
    Button resetLocation;
    Button changeName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            setContentView(R.layout.activity_settings);
            displayName = (EditText) findViewById(R.id.public_name);
            changeName = (Button) findViewById(R.id.change_name);
            alignmentSpinner = (Spinner) findViewById(R.id.alignment_spinner);
            resetLocation = (Button) findViewById(R.id.update_location);
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.alignment_spinner, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            alignmentSpinner.setAdapter(adapter);
            //TODO:Figure out what listener works on spinners
            /*alignmentSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    setAlignment(position);
                }
            });*/
            changeName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    changeName(changeName.getText().toString());
                }
            });
            resetLocation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    resetLocation();
                }
            });
        }
    }
    public void setAlignment (int alignment) {
        User user = ((MainActivity)getParent()).loggedUser;
        user.setAlignment(alignment);
        user.saveEventually();
    }
    private void changeName (String newName) {
        User user = ((MainActivity)getParent()).loggedUser;
        user.setUserName(newName);
        user.saveEventually();
    }
    private void resetLocation () {
        //TODO:Figure out how to access gps
    }
}
