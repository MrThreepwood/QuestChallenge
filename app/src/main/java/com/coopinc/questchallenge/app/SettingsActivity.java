package com.coopinc.questchallenge.app;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.parse.ParseFacebookUtils;
import com.parse.ParseQuery;
public class SettingsActivity extends Activity {
    Spinner alignmentSpinner;
    EditText displayName;
    Button resetLocation;
    Button changeName;
    int currentAlignment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            setContentView(R.layout.activity_settings);
            displayName = (EditText) findViewById(R.id.public_name);
            changeName = (Button) findViewById(R.id.change_name);
            alignmentSpinner = (Spinner) findViewById(R.id.alignment_spinner);
            User user = ((ApplicationInfo)getApplicationContext()).loggedUser;
            currentAlignment = user.getAlignment();
            displayName.setText(user.getName());

            resetLocation = (Button) findViewById(R.id.update_location);
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.alignment_spinner, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            alignmentSpinner.setAdapter(adapter);
            alignmentSpinner.setSelection(currentAlignment);
            resetLocation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    resetLocation();
                }
            });
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        alignmentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setAlignment(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        changeName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeName(displayName.getText().toString());
            }
        });
    }

    public void setAlignment (int id) {

        User user = ((ApplicationInfo)getApplicationContext()).loggedUser;
        if (currentAlignment != id) {
            user.setAlignment(id);
            user.saveEventually();
            currentAlignment = id;
        }
    }
    private void changeName (String newName) {
        if(TextUtils.isEmpty(newName)) {
            displayName.setError("Please enter a display name.");
        }
        else {
            User user = ((ApplicationInfo) getApplicationContext()).loggedUser;
            user.setName(newName);
            user.saveEventually();
        }
    }
    private void resetLocation () {
        //TODO:Figure out how to access gps
    }
}
