package com.coopinc.questchallenge.app;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginFragment extends BaseFragment {
    Button loginButton;
    EditText etEmail;
    CheckBox rememberUserName;
    Button signUp;
    EditText etPassword;
    TextView tvLoginIndicator;
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
        tvLoginIndicator = (TextView) getView().findViewById(R.id.login_indicator);
        rememberUserName = (CheckBox) getView().findViewById(R.id.remember_user_name);
        etEmail = (EditText) getView().findViewById(R.id.user_name);
        SharedPreferences sharedPreferences = getMainActivity().getApplicationContext().getSharedPreferences("UserName", 0);
        String rememberedName = sharedPreferences.getString("userName", "");
        if (!TextUtils.isEmpty(rememberedName)) {
            etEmail.setText(rememberedName);
            rememberUserName.setChecked(true);
        }
        etPassword = (EditText) getView().findViewById(R.id.password);
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
                checkLogin();
            }
        });
        checkConnection();
    }
    private void checkConnection () {
        if (getMainActivity().checkConnectionMaybeQuery()) {
            tvLoginIndicator.setText("");
            loginButton.setText(R.string.login);
            connection = true;
        }   else {
            tvLoginIndicator.setText(R.string.no_connection);
            loginButton.setText(R.string.retry_connection);
            connection = false;
        }

    }


    private void checkLogin() {
        if (loggingIn)
            return;
        if(!connection) {
            checkConnection();
            return;
        }
        boolean complete = true;
        String userName = etEmail.getText().toString();
        String password = etPassword.getText().toString();
        if(TextUtils.isEmpty(userName)) {
            etEmail.setError(getResources().getString(R.string.user_name_required));
            complete = false;
        }
        if(TextUtils.isEmpty(password)){
            etPassword.setError(getResources().getString(R.string.password_required));
            complete = false;
        }

        if (complete) {
            loggingIn = true;
            tvLoginIndicator.setText("One moment...");
            ParseUser.logInInBackground(userName.toLowerCase(), password, new LogInCallback() {
                @Override
                public void done(ParseUser parseUser, ParseException e) {
                    if (e == null) {

                        login();
                    }
                    else {
                        loggingIn = false;
                        if (e.getCode() == 100) {
                            tvLoginIndicator.setText(R.string.login_timeout);
                        }

                        else{
                            tvLoginIndicator.setText(R.string.login_failed);
                        }
                        Log.e("Parse Error", e.getMessage() + "code: " + e.getCode());
                    }
                }
            });
        }
    }
    private void login () {
        if(rememberUserName.isChecked()) {
            String name = etEmail.getText().toString();
            SharedPreferences sharedPreferences = getMainActivity().getApplicationContext().getSharedPreferences("UserName", 0);
            sharedPreferences.edit().putString("userName", name).apply();
        }
        else {
            SharedPreferences sharedPreferences = getMainActivity().getApplicationContext().getSharedPreferences("UserName", 0);
            sharedPreferences.edit().remove("userName");
        }
        MainActivity mainActivity = getMainActivity();
        mainActivity.fragmentSwap(new QuestsViewPager(), null, false);
    }
    private void register () {
        if(!connection) {
            Toast.makeText(getMainActivity(), R.string.must_be_connected, Toast.LENGTH_LONG);
        }
        getMainActivity().fragmentSwap(new RegistrationFragment(), null, true);
    }
}
