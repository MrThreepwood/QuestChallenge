package com.coopinc.questchallenge.app;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;


import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;


import java.util.ArrayList;
import java.util.List;


public class QuestListFragment extends BaseFragment implements AdapterView.OnItemClickListener {

    List<QuestInfo> quests = new ArrayList<QuestInfo>();
    List<QuestInfo> localQuests = new ArrayList<QuestInfo>();
    private ListView mListView;
    private QuestListAdapter mAdapter = new QuestListAdapter();
    private List<QuestInfo> adjustedQuests= new ArrayList<QuestInfo>();
    private int questDisplayStatus;
    public User user;
    private List<QuestInfo> acceptedQuests;
    private List<QuestInfo> completedQuests;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        questDisplayStatus = getArguments().getInt("questStatus");
        View view = inflater.inflate(R.layout.fragment_quest_list, container, false);
        mListView = (ListView) view.findViewById(R.id.quest_list);
        mListView.setAdapter(mAdapter);
        user = (User) ParseUser.getCurrentUser();
        //retrieveQuests(false, view);
        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setOnItemClickListener(this);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("Resume call", "resume is called");
        retrieveQuests();
    }

    private void retrieveQuests () {
        final ParseQuery<QuestInfo> query = new ParseQuery<QuestInfo>("Quests");
        query.include("questGiver");
        query.fromLocalDatastore();
        try {
            localQuests = query.find();
        } catch (ParseException exception) {

        }
        quests.clear();
        quests.addAll(localQuests);
        ParseQuery<QuestInfo> queryAccepted = new ParseQuery<QuestInfo>("Quests");
        queryAccepted.whereEqualTo("acceptedBy" , user.getObjectId());
        queryAccepted.fromLocalDatastore();
        try {
            acceptedQuests = queryAccepted.fromLocalDatastore().find();
        } catch (ParseException exception) {

        }
        ParseQuery<QuestInfo> queryCompleted = new ParseQuery<QuestInfo>("Quests");
        queryCompleted.whereEqualTo("completedBy" , user.getObjectId());
        queryCompleted.fromLocalDatastore();
        try {
            completedQuests = queryCompleted.fromLocalDatastore().find();
        } catch (ParseException exception) {

        }
        adjustedQuests.clear();

            switch (questDisplayStatus) {


                case 0:
                    for (QuestInfo quest : quests) {
                        if (!acceptedQuests.contains(quest) && !completedQuests.contains(quest)) {
                            if (quest.getAlignment() == user.getAlignment() || user.getAlignment() == 1 || quest.getAlignment() == 1) {
                                adjustedQuests.add(quest);
                            }
                        }
                    }
                    break;
                case 1:
                    adjustedQuests.addAll(acceptedQuests);
                    break;
                case 2:
                    adjustedQuests.addAll(completedQuests);
        }
        //Remove this.
//        adjustedQuests.clear();
//        adjustedQuests.addAll(quests);
        mAdapter.notifyDataSetChanged();
    }

    private class QuestListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return adjustedQuests.size();
        }

        @Override
        public Object getItem(int position) {
            return adjustedQuests.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(getActivity());
                convertView = inflater.inflate(R.layout.quest_layouts, null);
            }
            TextView titleView = (TextView) convertView.findViewById(R.id.quest_title);
            QuestInfo quest = adjustedQuests.get(position);
            String questName = quest.getQuestName();
            titleView.setText(questName);
            TextView giverView = (TextView) convertView.findViewById(R.id.quest_giver);
            String questGiver = ((User)quest.getQuestGiver()).getName();
            giverView.setText(questGiver);
            Log.d("Quest name", "Quest giver is " + questGiver);
            switch (quest.getAlignment()) {
                case 1: convertView.setBackgroundColor(getResources().getColor(R.color.grey));
                    break;
                case 2: convertView.setBackgroundColor(getResources().getColor(R.color.red));
                    break;
                default: convertView.setBackgroundColor(getResources().getColor(R.color.green));
            }
            return convertView;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Bundle args = new Bundle();
        //Convert the position of the adjusted list to the position of quests (so that details can pull the right quest).
        int adjustedPosition = quests.indexOf(adjustedQuests.get(position));
        args.putInt("quest", adjustedPosition);
        args.putInt("questStatus", questDisplayStatus);
        getMainActivity().fragmentSwap(new QuestDetails(), args, true);
    }
}

