package com.ezen.lolketing.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.ezen.lolketing.fragment.OutlineFragment;
import com.ezen.lolketing.fragment.PrizeFragment;
import com.ezen.lolketing.fragment.ProgressFragment;

public class LeagueInfoAdapter extends FragmentStatePagerAdapter {
    private int leagueInfoPagerCount;

    public LeagueInfoAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        this.leagueInfoPagerCount = behavior;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                OutlineFragment outlineFragment = new OutlineFragment();
                return outlineFragment;
            case 1:
                ProgressFragment progressFragment = new ProgressFragment();
                return progressFragment;
            case 2:
                PrizeFragment prizeFragment = new PrizeFragment();
                return  prizeFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return leagueInfoPagerCount;
    }
}
