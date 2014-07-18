package com.coopinc.questchallenge.app;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;


public class LoginFragment extends Fragment {

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
                login(v);
            }
        });
    }
    public void login(View view) {
        EditText editName = (EditText) getView().findViewById(R.id.user_name);
        String userName = editName.getText().toString();
        EditText editPassword = (EditText) getView().findViewById(R.id.password);
        String password = editPassword.getText().toString();
        if(userName != null && password != null) {
            if(userName.length()==0) {
                editName.setError("A user name is required.");
            }
            if(password.length()==0){
                editPassword.setError("A password is required.");
            }
            TextView loginIndicator = (TextView) getView().findViewById(R.id.login_indicator);
            if (userName.equals("L") && password.equals("a")) {
                ((MainActivity)getActivity()).fragmentSwap(this, new QuestListFragment());
            }
            else {
                loginIndicator.setTextColor(getResources().getColor(R.color.red));
                loginIndicator.setText("Invalid user name or password");
            }
        }
    }
}
