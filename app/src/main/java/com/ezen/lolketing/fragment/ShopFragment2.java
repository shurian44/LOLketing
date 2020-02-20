package com.ezen.lolketing.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ezen.lolketing.R;
import com.ezen.lolketing.ShopActivity;


public class ShopFragment2 extends Fragment {

    ShopActivity shopActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_shop_fragment2, container, false);
        shopActivity.changePage(2);
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        shopActivity = (ShopActivity) getActivity();
    }

}
