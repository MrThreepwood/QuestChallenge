package com.coopinc.questchallenge.app;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
    private QuestListAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_questlist, container, false);
        mListView = (ListView) view.findViewById(android.R.id.list);
        mListView.setAdapter(mAdapter);

        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setOnItemClickListener(this);
        ParseQuery<QuestInfo> query = ParseQuery.getQuery("Quests");
        query.findInBackground(new FindCallback<QuestInfo>() {
            @Override
            public void done(List<QuestInfo> parseObjects, ParseException e) {
                quests.clear();
                quests.addAll(parseObjects);
                mAdapter.notifyDataSetChanged();
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
                
            }
            return convertView;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}

