package com.coopinc.questchallenge.app;



import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import org.w3c.dom.Text;


/**
 * A simple {@link Fragment} subclass.
 *
 */
public class RegistrationFragment extends BaseFragment {
    private EditText etEmail;
    private EditText etDisplayName;
    private EditText etPassword;
    private EditText etVerifyPassword;
    private Button etRegister;


    public RegistrationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_registration, container, false);
        etEmail = (EditText) view.findViewById(R.id.register_email);
        etDisplayName = (EditText) view.findViewById(R.id.display_name);
        etPassword = (EditText) view.findViewById(R.id.register_password);
        etVerifyPassword = (EditText) view.findViewById(R.id.verify_password);
        etRegister = (Button) view.findViewById(R.id.register);
        etRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
        return view;
    }
    public void register () {
        boolean complete = true;
        final String email = etEmail.getText().toString();
        String displayName = etDisplayName.getText().toString();
        final String password = etPassword.getText().toString();
        String verifyPassword = etVerifyPassword.getText().toString();
        if (TextUtils.isEmpty(email)) {
            etEmail.setError(getResources().getString(R.string.email_required));
            complete = false;
        } else {
            if (!email.contains("@") || !email.contains(".")) {
                etEmail.setError(getResources().getString(R.string.not_email));
                complete = false;
            }
        }
        if (TextUtils.isEmpty(displayName)) {
            etDisplayName.setError(getResources().getString(R.string.choose_display_name));
            complete = false;
        }
        if (TextUtils.isEmpty(password)) {
            etPassword.setError(getResources().getString(R.string.register_password_required));
            complete = false;
        }
        if (!password.equals(verifyPassword)) {
            etVerifyPassword.setError(getResources().getString(R.string.passwords_dont_match));
            complete = false;
        }
        if (TextUtils.isEmpty(verifyPassword)) {
            etVerifyPassword.setError(getResources().getString(R.string.verify_password_required));
            complete = false;
        }
        if(complete) {
            ParseUser newUser = new ParseUser();
            newUser.setEmail(email.toLowerCase());
            newUser.setPassword(password);
            newUser.setUsername(email.toLowerCase());
            newUser.put("Alignment", 1);
            newUser.put("Name", displayName);
            newUser.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        tryLogin(email, password, e);
                    }
                }
            });
        }
    }
    private void tryLogin(String email, String password, ParseException e) {
        ParseUser.logInInBackground(email, password, new LogInCallback() {
            @Override
            public void done(ParseUser parseUser, ParseException e) {
                if (e == null) {
                    getMainActivity().loggedUser = (User) parseUser;
                    toQuestList();
                }
            }
        });
    }
    private void toQuestList () {
        getMainActivity().fragmentSwap(this, new QuestListFragment(), null, false);
    }

}
