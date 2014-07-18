package com.coopinc.questchallenge.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Guybrush on 7/18/2014.
 */
public class QuestDetails extends Fragment {
    QuestInfo quest;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quest_deatails, container, false);
        Bundle args = getArguments();
        //Retrieves the quest from the main activity.
        quest = ((MainActivity)getActivity()).quests.get(args.getInt("quest"));
        return view;
    }
    //Sets up the view.
    @Override
    public void onStart() {
        super.onStart();
        TextView questTitle = (TextView) getView().findViewById(R.id.quest_title);
        questTitle.setText(quest.getQuestName());
        TextView questGiver = (TextView) getView().findViewById(R.id.quest_giver);
        questGiver.setText("Posted by:" + quest.getQuestGiver());
        TextView questDetails = (TextView) getView().findViewById(R.id.quest_details);
        questDetails.setText(quest.getDescription());
    }
}
