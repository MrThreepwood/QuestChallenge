package com.coopinc.questchallenge.app;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLngBounds;
import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;

import java.util.List;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.ParseUser;

public class QuestDetails extends Fragment {
    QuestInfo quest;

    private TextView mQuestGiver;
    private TextView mQuestTitle;
    private TextView mQuestDetails;
    private SupportMapFragment map;
    private Button acceptComplete;
    private TextView questImageLoading;
    private ImageView questImageView;
    private TextView giverImageLoading;
    private ImageView giverImageView;
    private int questStatus;
    private User user;
    static final private int mapPadding = 40;
    private User questGiver;
    private Marker questGiverMarker;
    private Marker questMarker;
    private Bitmap questBitmap;
    private Bitmap giverBitmap;
    private boolean bitmapsReady;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quest_details, container, false);

        mQuestTitle = (TextView) view.findViewById(R.id.quest_title);
        mQuestGiver = (TextView) view.findViewById(R.id.quest_giver);
        mQuestDetails = (TextView) view.findViewById(R.id.quest_details);
        map = SupportMapFragment.newInstance();
        acceptComplete = (Button) view.findViewById(R.id.accept_complete);
        user = (User) ParseUser.getCurrentUser();
        questImageLoading = (TextView) view.findViewById(R.id.quest_image_loading);
        questImageView = (ImageView) view.findViewById(R.id.quest_image);
        giverImageLoading = (TextView) view.findViewById(R.id.giver_image_loading);
        giverImageView = (ImageView) view.findViewById(R.id.giver_image);
        acceptComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acceptComplete();
            }
        });
        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        fragmentTransaction.add(view.getId(), map);
        fragmentTransaction.commit();


        final Bundle args = getArguments();
        questStatus = args.getInt("questStatus");
        switch (questStatus) {
            case 0: acceptComplete.setText(getResources().getString(R.string.accept_quest));
                break;
            case 1: acceptComplete.setText(getResources().getString(R.string.complete_quest));
                break;
            case 2:
                acceptComplete.setText(getResources().getString(R.string.quest_already_completed));
                acceptComplete.setTextColor(getResources().getColor(R.color.red));
                break;
        }
        //Retrieves the quest from the main activity.
        ParseQuery<QuestInfo> query = new ParseQuery<QuestInfo>("Quests");
        query.include("questGiver");
        query.fromLocalDatastore();
        query.findInBackground(new FindCallback<QuestInfo>() {
            @Override
            public void done(List<QuestInfo> questInfos, ParseException e) {
                quest = questInfos.get(args.getInt("quest"));
                updateUI();
                getPictures();
            }
        });
        return view;
    }

    private void updateUI() {
        questGiver = ((User)quest.getQuestGiver());
        if (mQuestDetails == null)
            return;

        mQuestTitle.setText(quest.getQuestName());
        switch(quest.getAlignment()) {
            case 1: mQuestTitle.setTextColor(getResources().getColor(R.color.grey));
                break;
            case 2: mQuestTitle.setTextColor(getResources().getColor(R.color.red));
                break;
            default: mQuestTitle.setTextColor(getResources().getColor(R.color.green));
        }
        mQuestGiver.setText("Posted by:" + questGiver.getName());
        mQuestDetails.setText(quest.getDescription());

        ParseGeoPoint giverGeoPoint = questGiver.getLocation();
        LatLng giverLatLng = new LatLng(giverGeoPoint.getLatitude(),giverGeoPoint.getLongitude());
        questGiverMarker = map.getMap().addMarker(new MarkerOptions().position(giverLatLng));
        questGiverMarker.setTitle(questGiver.getName());
        ParseGeoPoint questGeoPoint = quest.getLocation();
        LatLng questLatLng = new LatLng(questGeoPoint.getLatitude(), questGeoPoint.getLongitude());
        questMarker = map.getMap().addMarker(new MarkerOptions().position(questLatLng));
        questMarker.setTitle(quest.getQuestName());
        questGiverMarker.showInfoWindow();
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(giverLatLng);
        builder.include(questLatLng);
        LatLngBounds bounds = builder.build();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, mapPadding);
        map.getMap().moveCamera(cameraUpdate);

    }
    private void acceptComplete () {
        switch (questStatus) {
            case 0:
                quest.addAcceptedBy(user.getObjectId());
                acceptComplete.setText(getResources().getString(R.string.complete_quest));
                quest.saveEventually();
                break;
            case 1:
                quest.setCompletedBy(user.getObjectId());
                quest.removeAcceptedyBy(user.getObjectId());
                quest.saveEventually();
                acceptComplete.setText(getResources().getString(R.string.quest_already_completed));
                acceptComplete.setTextColor(getResources().getColor(R.color.red));
        }
    }
    private void getPictures(){
        final ParseFile giverImage = questGiver.getUserImage();
        giverImage.getDataInBackground(new GetDataCallback() {
            @Override
            public void done(byte[] bytes, ParseException e) {
                if(e == null && bytes != null && bytes.length != 0) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    giverBitmap = BitmapAssistant.resize(bitmap, giverImageView.getWidth(), giverImageView.getHeight());
                    giverImageView.setImageBitmap(giverBitmap);
                    giverImageLoading.setVisibility(View.INVISIBLE);
                } else {
                    giverImageLoading.setText(getResources().getString(R.string.no_image_found));
                }
                if(bitmapsReady)
                    updateMarkers();
                bitmapsReady = true;
            }
        });
        ParseFile questImage = quest.getQuestImage();
        questImage.getDataInBackground(new GetDataCallback() {
            @Override
            public void done(byte[] bytes, ParseException e) {
                if(e == null && bytes != null && bytes.length != 0) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    questBitmap = BitmapAssistant.resize(bitmap, questImageLoading.getWidth(), questImageLoading.getHeight());
                    questImageView.setImageBitmap(questBitmap);
                    questImageLoading.setVisibility(View.INVISIBLE);
                } else {
                    giverImageLoading.setText(getResources().getString(R.string.no_image_found));
                }
                if(bitmapsReady)
                    updateMarkers();
                bitmapsReady = true;
            }
        });
    }
    private void updateMarkers() {
        questGiverMarker.setIcon(BitmapDescriptorFactory.fromBitmap(giverBitmap));
        questMarker.setIcon(BitmapDescriptorFactory.fromBitmap(questBitmap));
    }
}
