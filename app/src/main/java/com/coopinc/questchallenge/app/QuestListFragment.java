package com.coopinc.questchallenge.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;


import java.util.ArrayList;


public class QuestListFragment extends Fragment implements AdapterView.OnItemClickListener {

    ArrayList<QuestInfo> quests = new ArrayList<QuestInfo>();
    private ListView mListView;
    private QuestListAdapter mAdapter = new QuestListAdapter();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quest_list, container, false);
        mListView = (ListView) view;
        mListView.setAdapter(mAdapter);
        quests = retrieveQuests(false);
        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setOnItemClickListener(this);

        return view;
    }
    private ArrayList<QuestInfo> retrieveQuests (boolean reset) {
        if (reset) {
            ((MainActivity)getActivity()).cacheParseQuestQuery();
        }
        return ((MainActivity)getActivity()).quests;
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
        Bundle args = new Bundle();
        args.putInt("quest", position);
        ((MainActivity)getActivity()).fragmentSwap(this, new QuestDetails(), args);
    }
}

