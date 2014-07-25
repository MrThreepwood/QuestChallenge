package com.coopinc.questchallenge.app;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import org.w3c.dom.Text;


public class LoginFragment extends BaseFragment {

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }
    @Override
    public void onStart() {
        super.onStart();
        Button loginButton = (Button) getView().findViewById(R.id.login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkLogin(v);
            }
        });
    }
    private void checkLogin(View view) {
        boolean complete = true;
        EditText editName = (EditText) getView().findViewById(R.id.user_name);
        String userName = editName.getText().toString();
        EditText editPassword = (EditText) getView().findViewById(R.id.password);
        String password = editPassword.getText().toString();
        if(userName.length() == 0) {
            editName.setError("A user name is required.");
            complete = false;
        }
        if(password.length()==0){
            editPassword.setError("A password is required.");
            complete = false;
        }
        final TextView loginIndicator = (TextView) getView().findViewById(R.id.login_indicator);
        if (complete) {
            ParseUser.logInInBackground(userName.toLowerCase(), password, new LogInCallback() {
                @Override
                public void done(ParseUser parseUser, ParseException e) {
                    if (e == null)
                        login();
                    else {
                        loginIndicator.setText("User name or login invalid");
                        loginIndicator.setTextColor(getResources().getColor(R.color.red));
                    }
                }
            });
        }
    }
    private void login () {
        getMainActivity().fragmentSwap(this, new QuestListFragment(), null, false);
    }
}
