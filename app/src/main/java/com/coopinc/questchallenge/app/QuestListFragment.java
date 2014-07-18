package com.coopinc.questchallenge.app;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;


import com.coopinc.questchallenge.app.dummy.DummyContent;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;


public class QuestListFragment extends Fragment implements AdapterView.OnItemClickListener {

    ArrayList<QuestInfo> quests = new ArrayList<QuestInfo>();
    private ListView mListView;
    private QuestListAdapter mAdapter = new QuestListAdapter();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_questlist, container, false);
        mListView = (ListView) view;
        if(mAdapter == null) {
            System.out.println("The adapter is null.");
        }
        if(mListView == null) {
            System.out.println("The list view is null.");
        }
        mListView.setAdapter(mAdapter);

        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setOnItemClickListener(this);
        ParseQuery<QuestInfo> query = ParseQuery.getQuery("Quests");
        query.findInBackground(new FindCallback<QuestInfo>() {
            @Override
            public void done(List<QuestInfo> parseObjects, ParseException e) {
                if(e != null) {
                    Log.d("quests", "Error" + e.getMessage());
                }
                else {
                    quests.clear();
                    quests.addAll(parseObjects);
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
        return view;
    }

    private class QuestListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return quests.size();
        }

        @Override
        public Object getItem(int position) {
            return quests.get(position);
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
            QuestInfo quest = quests.get(position);
            String questName = quest.getQuestName();
            titleView.setText(questName);
            TextView giverView = (TextView) convertView.findViewById(R.id.quest_giver);
            String questGiver = quest.getQuestGiver();
            giverView.setText(questGiver);
            Log.d("Quest name", "Quest giver is " + questGiver);
            switch (quest.getAlignment()) {
                case 1: convertView.setBackgroundColor(getResources().getColor(R.color.green));
                    break;
                case 2: convertView.setBackgroundColor(getResources().getColor(R.color.red));
                    break;
                default: convertView.setBackgroundColor(getResources().getColor(R.color.grey));
            }
            return convertView;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}

