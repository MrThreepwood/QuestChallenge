package com.coopinc.questchallenge.app;

import android.support.v4.app.Fragment;

/**
 * Created by Guybrush on 7/19/2014.
 */
public class BaseFragment extends Fragment {

    public MainActivity getMainActivity() {
        return (MainActivity) getActivity();
    }

}
