package com.coopinc.questchallenge.app;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import org.w3c.dom.Text;


public class LoginFragment extends BaseFragment {
    Button loginButton;
    EditText editName;
    CheckBox rememberUserName;
    Button signUp;
    EditText editPassword;
    TextView loginIndicator;
    boolean connection;
    boolean loggingIn;

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
        loginButton = (Button) getView().findViewById(R.id.login);
        loginIndicator = (TextView) getView().findViewById(R.id.login_indicator);
        rememberUserName = (CheckBox) getView().findViewById(R.id.remember_user_name);
        editName = (EditText) getView().findViewById(R.id.user_name);
        SharedPreferences sharedPreferences = getMainActivity().getApplicationContext().getSharedPreferences("UserName", 0);
        String rememberedName = sharedPreferences.getString("userName", "");
        if (!TextUtils.isEmpty(rememberedName)) {
            editName.setText(rememberedName);
            rememberUserName.setChecked(true);
        }
        editPassword = (EditText) getView().findViewById(R.id.password);
        signUp = (Button) getView().findViewById(R.id.sign_up);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkLogin(v);
            }
        });
        checkConnection();
    }
    private void checkConnection () {
        if (getMainActivity().checkConnectionMaybeQuery(true)) {
            loginIndicator.setText("");
            loginButton.setText(R.string.login);
            connection = true;
        }   else {
            loginIndicator.setText(R.string.no_connection);
            loginButton.setText(R.string.retry_connection);
            connection = false;
        }
    }
    private void checkLogin(View view) {
        if (loggingIn)
            return;
        if(!connection) {
            checkConnection();
            return;
        }
        boolean complete = true;
        String userName = editName.getText().toString();
        String password = editPassword.getText().toString();
        if(TextUtils.isEmpty(userName)) {
            editName.setError(getResources().getString(R.string.user_name_required));
            complete = false;
        }
        if(TextUtils.isEmpty(password)){
            editPassword.setError(getResources().getString(R.string.password_required));
            complete = false;
        }

        if (complete) {
            loggingIn = true;
            loginIndicator.setText("One moment...");
            ParseUser.logInInBackground(userName.toLowerCase(), password, new LogInCallback() {
                @Override
                public void done(ParseUser parseUser, ParseException e) {
                    if (e == null) {

                        login(parseUser);
                    }
                    else {
                        loggingIn = false;
                        loginIndicator.setText(R.string.login_failed);
                    }
                }
            });
        }
    }
    private void login (ParseUser parseUser) {
        if(rememberUserName.isChecked()) {
            String name = editName.getText().toString();
            SharedPreferences sharedPreferences = getMainActivity().getApplicationContext().getSharedPreferences("UserName", 0);
            sharedPreferences.edit().putString("userName", name).commit();
        }
        else {
            SharedPreferences sharedPreferences = getMainActivity().getApplicationContext().getSharedPreferences("UserName", 0);
            sharedPreferences.edit().remove("userName");
        }
        MainActivity mainActivity = getMainActivity();
        ((ApplicationInfo)mainActivity.getApplicationContext()).loggedUser = (User) parseUser;
        ((ApplicationInfo)mainActivity.getApplicationContext()).loggedIn = true;
        mainActivity.fragmentSwap(this, new QuestListFragment(), null, false);
    }
    private void register () {
        getMainActivity().fragmentSwap(this, new RegistrationFragment(), null, true);
    }
}
