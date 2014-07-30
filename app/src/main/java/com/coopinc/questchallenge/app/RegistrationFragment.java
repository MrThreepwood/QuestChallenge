package com.coopinc.questchallenge.app;



import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
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
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class RegistrationFragment extends BaseFragment {
    private EditText etEmail;
    private EditText etDisplayName;
    private EditText etPassword;
    private EditText etVerifyPassword;
    private Button etRegister;
    TextView takePicture;
    private boolean registering = false;
    private TextView registeringIndicator;
    private ImageView picture;
    Camera camera;
    boolean hasCamera = true;
    int cameraId;

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
        registeringIndicator = (TextView) view.findViewById(R.id.trying_signup);
        picture = (ImageView) view.findViewById(R.id.picture);
        takePicture = (TextView) view.findViewById(R.id.take_picture);
        takePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
            }
        });
        pictureCheck();
        return view;
    }
    private void pictureCheck () {
        if (!getMainActivity().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            hasCamera = false;
            Toast.makeText(getMainActivity(), "No camera found.", Toast.LENGTH_LONG).show();
        } else {
            cameraId = findFrontFacingCamera();
        }
    }
    private int findFrontFacingCamera () {
        for (int n = 0; n < Camera.getNumberOfCameras(); n++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(n, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                return n;
            }
        }
        return -1;
    }
    private void managePicture(byte[] image) {
        Log.d("picture callback", "callback is going through");
        Bitmap bitmap;
        bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
        picture.setImageBitmap(bitmap);
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
            registeringIndicator.setText(getResources().getString(R.string.trying_signup));
            User newUser = new User();
            newUser.setEmail(email.toLowerCase());
            newUser.setPassword(password);
            newUser.setUsername(email.toLowerCase());
            newUser.setName(displayName);
            newUser.setAlignment(1);
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
                    getMainActivity().fragmentSwap(new QuestListFragment(), null, false);
                } else {
                    registeringIndicator.setText("Registration complete, login failed.");
                    Log.e("parse error", e.getMessage()+ " code " + Integer.toString(e.getCode()));
                }
            }
        });
    }
    private void takePicture () {
        if (cameraId >= 0 ) {
            camera = Camera.open(cameraId);
            camera.takePicture(null, null, new Camera.PictureCallback() {
                @Override
                public void onPictureTaken(byte[] data, Camera camera) {
                    managePicture(data);
                }
            });
        }
    }

}
