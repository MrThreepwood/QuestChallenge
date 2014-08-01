package com.coopinc.questchallenge.app;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class SettingsActivity extends Activity {
    Spinner spAlignment;
    EditText etDisplayName;
    Button changeName;
    Button updatePicture;
    ImageView ivCurrentPicture;
    User user;
    int currentAlignment;
    private static final int PICK_PHOTO = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            setContentView(R.layout.activity_settings);
            etDisplayName = (EditText) findViewById(R.id.public_name);
            changeName = (Button) findViewById(R.id.change_name);
            spAlignment = (Spinner) findViewById(R.id.alignment_spinner);
            user = (User) ParseUser.getCurrentUser();
            updatePicture = (Button) findViewById(R.id.update_picture);
            ivCurrentPicture = (ImageView) findViewById(R.id.current_image);
            updatePicture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectNewImage();
                }
            });
            currentAlignment = user.getAlignment();
            etDisplayName.setText(user.getName());
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.alignment_spinner, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spAlignment.setAdapter(adapter);
            spAlignment.setSelection(currentAlignment);
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        ParseFile userImageFile = user.getUserImage();
        if(userImageFile != null) {
            userImageFile.getDataInBackground(new GetDataCallback() {
                @Override
                public void done(byte[] bytes, ParseException e) {
                    if (e == null && bytes != null && bytes.length != 0) {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        ivCurrentPicture.setImageBitmap(bitmap);
                    }
                }
            });
        } else {
            updatePicture.setText(R.string.pick_an_image);
        }
        spAlignment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
                changeName(etDisplayName.getText().toString());
            }
        });
    }

    public void setAlignment (int id) {
        if (currentAlignment != id) {
            user.setAlignment(id);
            user.saveEventually();
            currentAlignment = id;
        }
    }
    private void changeName (String newName) {
        if(TextUtils.isEmpty(newName)) {
            etDisplayName.setError("Please enter a display name.");
        }
        else {
            user.setName(newName);
            user.saveEventually();
        }
    }
    private void selectNewImage () {
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
                        InputStream imageStream = getContentResolver().openInputStream(selectedImage);
                        Bitmap pickedImage = BitmapFactory.decodeStream(imageStream);
                        //Bitmap scaledImage = BitmapAssistant.resizeToFit(pickedImage, ivCurrentPicture.getWidth(), ivCurrentPicture.getHeight());
                        Bitmap scaledImage = BitmapAssistant.resizeToPixelCount(pickedImage, 18000);
                        ivCurrentPicture.setImageBitmap(scaledImage);
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        scaledImage.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                        byte[] imageArray = bos.toByteArray();
                        ParseFile imageFile = new ParseFile(user.getUserName(),imageArray, "jpg");
                        try {
                            imageFile.save();
                        } catch(ParseException exception) {

                        }
                        user.setUserImage(imageFile);
                        user.saveEventually();
                    } catch (FileNotFoundException e) {

                    }
                }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        ivCurrentPicture.setImageBitmap(null);
    }
}
