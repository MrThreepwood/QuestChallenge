package com.coopinc.questchallenge.app;



import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;


public class QuestsViewPager extends Fragment  {
    TextView tvUserImageLoad;
    ImageView ivUserImage;
    Bitmap userImage;
    ViewPager mViewPager;
    TabAdapter adapter;
    static CharSequence availableQuests;
    static CharSequence acceptedQuests;
    static CharSequence completedQuests;



    public QuestsViewPager() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_quests_view_pager, container, false);
        mViewPager = (ViewPager) view.findViewById(R.id.quests_view_pager);
        ivUserImage = (ImageView) view.findViewById(R.id.user_image);
        tvUserImageLoad = (TextView) view.findViewById(R.id.user_image_loading);
        loadUserImage();
        adapter = new TabAdapter(getChildFragmentManager());
        mViewPager.setAdapter(adapter);
        mViewPager.setOffscreenPageLimit(3);
        availableQuests = getResources().getString(R.string.available_quests);
        acceptedQuests = getResources().getString(R.string.accepted_quests);
        completedQuests = getResources().getString(R.string.completed_quests);
        return view;
    }

    private static class TabAdapter extends FragmentStatePagerAdapter {

        public TabAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0: return availableQuests;
                case 1: return acceptedQuests;
                case 2: return completedQuests;
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
    private void loadUserImage () {

        ParseFile userImageFile = ((User)ParseUser.getCurrentUser()).getUserImage();
        if(userImageFile != null) {
            userImageFile.getDataInBackground(new GetDataCallback() {
                @Override
                public void done(byte[] bytes, ParseException e) {
                    if (e == null && bytes != null && bytes.length != 0) {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        ivUserImage.setImageBitmap(bitmap);
                        userImage = bitmap;
                        tvUserImageLoad.setVisibility(View.GONE);
                    }
                }
            });
        } else {
            tvUserImageLoad.setText(R.string.choose_an_image);
            tvUserImageLoad.setClickable(true);
            tvUserImageLoad.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainActivity)getActivity()).startSettingsActivity();
                }
            });
        }
    }
}
