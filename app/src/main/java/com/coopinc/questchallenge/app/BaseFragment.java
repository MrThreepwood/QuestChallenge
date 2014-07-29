package com.coopinc.questchallenge.app;

import android.support.v4.app.Fragment;

public class BaseFragment extends Fragment {

    public MainActivity getMainActivity() {
        return (MainActivity) getActivity();
    }

}
