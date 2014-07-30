package com.coopinc.questchallenge.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.GoogleMap;
import com.parse.FindCallback;
import com.parse.ParseException;
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
    private GoogleMap map;
    private Button acceptComplete;
    private int questStatus;
    private User user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quest_details, container, false);

        mQuestTitle = (TextView) view.findViewById(R.id.quest_title);
        mQuestGiver = (TextView) view.findViewById(R.id.quest_giver);
        mQuestDetails = (TextView) view.findViewById(R.id.quest_details);
        SupportMapFragment mapFrag = SupportMapFragment.newInstance();
        map = mapFrag.getMap();
        acceptComplete = (Button) view.findViewById(R.id.accept_complete);
        user = (User) ParseUser.getCurrentUser();
        acceptComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acceptComplete();
            }
        });
        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        fragmentTransaction.add(view.getId(), mapFrag);
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
            }
        });
        updateUI();
        return view;
    }

    private void updateUI() {
        User questGiver = ((User)quest.getQuestGiver());
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
        Marker questGiverMarker = map.addMarker(new MarkerOptions().position(giverLatLng));
        questGiverMarker.setTitle(questGiver.getName());
        ParseGeoPoint questGeoPoint = quest.getLocation();
        LatLng questLatLng = new LatLng(questGeoPoint.getLatitude(), questGeoPoint.getLongitude());
        Marker questMarker = map.addMarker(new MarkerOptions().position(questLatLng));
        questMarker.setTitle(quest.getQuestName());
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(giverLatLng, 12);
        map.moveCamera(cameraUpdate);

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
                acceptComplete.setTextColor(getResources().getColor(R.color.grey));
        }
    }
}
