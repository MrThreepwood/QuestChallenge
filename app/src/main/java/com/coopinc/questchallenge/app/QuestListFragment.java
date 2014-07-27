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
    private List<QuestInfo> adjustedQuests= new ArrayList<QuestInfo>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quest_list, container, false);
        mListView = (ListView) view.findViewById(R.id.quest_list);
        mListView.setAdapter(mAdapter);
        //retrieveQuests(false, view);
        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setOnItemClickListener(this);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("Resume call", "resume is called");
        retrieveQuests(false, getView());
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
                adjustedQuests.clear();
                User user = ((ApplicationInfo)getMainActivity().getApplicationContext()).loggedUser;
                for (int n = 0; n< quests.size(); n++) {
                    if (quests.get(n).getAlignment() == user.getAlignment() || user.getAlignment() == 1 || quests.get(n).getAlignment() == 1) {
                        adjustedQuests.add(quests.get(n));
                    }
                }
                mAdapter.notifyDataSetChanged();
            }
        });
        loading.setVisibility(View.GONE);
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
            String questGiver = quest.getQuestGiver();
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
        getMainActivity().fragmentSwap(this, new QuestDetails(), args, true);
    }
}

