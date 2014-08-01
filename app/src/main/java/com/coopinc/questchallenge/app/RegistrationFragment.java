package com.coopinc.questchallenge.app;



import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class RegistrationFragment extends BaseFragment {
    private EditText etEmail;
    private EditText etDisplayName;
    private EditText etPassword;
    private EditText etVerifyPassword;
    private Button registerButton;
    TextView takePicture;
    private boolean registering = false;
    private TextView registeringIndicator;
    private ImageView ivPicture;
    private Bitmap pickedImage;
    private String imagePath;
    private static final int PICK_PHOTO = 100;

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
        registerButton = (Button) view.findViewById(R.id.register);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
        registeringIndicator = (TextView) view.findViewById(R.id.trying_signup);
        ivPicture = (ImageView) view.findViewById(R.id.picture);
        takePicture = (TextView) view.findViewById(R.id.take_picture);
        takePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findPicture();
            }
        });
        return view;
    }
    public void register () {
        if (registering)
            return;
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
            registering = true;
            registeringIndicator.setText(R.string.trying_signup);
            User newUser = new User();
            newUser.setEmail(email.toLowerCase());
            newUser.setPassword(password);
            newUser.setUsername(email.toLowerCase());
            newUser.setName(displayName);
            newUser.setAlignment(1);
            if (pickedImage != null) {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                pickedImage.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                byte[] imageArray = bos.toByteArray();
                ParseFile imageFile = new ParseFile(email,imageArray, "jpeg");
                try {
                    imageFile.save();
                } catch(ParseException exception) {

                }
                newUser.setUserImage(imageFile);
            }
            newUser.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        tryLogin(email, password);
                    } else {
                        registeringIndicator.setText("");
                        registering = false;
                        Log.e("parse error", e.getMessage()+ " code " + Integer.toString(e.getCode()));
                        if (e.getCode() == 125) {
                            etEmail.setError(getResources().getString(R.string.not_email));
                        }
                        if (e.getCode() == 202 || e.getCode() == 203) {
                            etEmail.setError(getResources().getString(R.string.email_taken));
                        }
                    }
                }
            });
        }
    }
    private void tryLogin(String email, String password) {
        ParseUser.logInInBackground(email.toLowerCase(), password, new LogInCallback() {
            @Override
            public void done(ParseUser parseUser, ParseException e) {
                if (e == null) {
                    getMainActivity().getSupportFragmentManager().popBackStack();
                    getMainActivity().fragmentSwap(new QuestsViewPager(), null, false);
                } else {
                    registeringIndicator.setText("Registration complete, login failed.");
                    Log.e("parse error", e.getMessage()+ " code " + Integer.toString(e.getCode()));
                }
            }
        });
    }
    private void findPicture () {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, PICK_PHOTO);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch(requestCode) {
            case PICK_PHOTO:
                if(resultCode == Activity.RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();
                    try {
                        InputStream imageStream = getMainActivity().getContentResolver().openInputStream(selectedImage);
                        pickedImage = BitmapFactory.decodeStream(imageStream);
                        pickedImage = BitmapAssistant.resize(pickedImage, takePicture.getWidth(), takePicture.getHeight());
                        takePicture.setVisibility(View.INVISIBLE);
                        ivPicture.setImageBitmap(pickedImage);
                    } catch (FileNotFoundException e) {

                    }
                }
        }
    }

}
