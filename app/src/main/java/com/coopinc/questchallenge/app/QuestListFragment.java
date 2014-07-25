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


import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;


public class QuestListFragment extends BaseFragment implements AdapterView.OnItemClickListener {

    List<QuestInfo> quests = new ArrayList<QuestInfo>();
    private ListView mListView;
    private QuestListAdapter mAdapter = new QuestListAdapter();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quest_list, container, false);
        mListView = (ListView) view.findViewById(R.id.quest_list);
        mListView.setAdapter(mAdapter);
        retrieveQuests(false, view);
        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setOnItemClickListener(this);

        return view;
    }
    private void retrieveQuests (boolean reset, View view) {
        if (reset) {
            getMainActivity().cacheParseQuestsQuery();
        }
        ParseQuery<QuestInfo> query = new ParseQuery<QuestInfo>("Quests");
        query.include("questGiver");
        query.fromLocalDatastore();
        View loading = view.findViewById(R.id.loading_text);
        loading.setVisibility(View.VISIBLE);
        query.findInBackground(new FindCallback<QuestInfo>() {
            @Override
            public void done(List<QuestInfo> questInfos, ParseException e) {
                quests.clear();
                quests.addAll(questInfos);
                mAdapter.notifyDataSetChanged();
            }
        });
        loading.setVisibility(View.GONE);
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
        getMainActivity().fragmentSwap(this, new QuestDetails(), args, true);
    }
}

