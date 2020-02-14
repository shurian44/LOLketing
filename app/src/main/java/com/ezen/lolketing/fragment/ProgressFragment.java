package com.ezen.lolketing.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ezen.lolketing.LeagueInfoActivity;
import com.ezen.lolketing.R;

public class ProgressFragment extends Fragment {
    LeagueInfoActivity leagueInfoActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView =
                (ViewGroup) inflater.inflate(R.layout.fragment_progress, container, false);
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        leagueInfoActivity = (LeagueInfoActivity) getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
