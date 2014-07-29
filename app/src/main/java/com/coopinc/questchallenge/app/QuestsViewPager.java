package com.coopinc.questchallenge.app;



import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



public class QuestsViewPager extends Fragment  {
    ViewPager mViewPager;
    TabAdapter adapter;


    public QuestsViewPager() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_quests_view_pager, container, false);
        mViewPager = (ViewPager) view;
        adapter = new TabAdapter(getChildFragmentManager());
        mViewPager.setAdapter(adapter);
        mViewPager.setOffscreenPageLimit(3);
        return view;
    }

    private static class TabAdapter extends FragmentStatePagerAdapter {

        public TabAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0: return "Available quests.";
                case 1: return "Accepted quests.";
                case 2: return "Completed quests.";
            }
            return super.getPageTitle(position);
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public Fragment getItem(int position) {
            Fragment questList = new QuestListFragment();
            Bundle args = new Bundle();
            args.putInt("questStatus", position);
            questList.setArguments(args);
            return questList;
        }

    }


}
