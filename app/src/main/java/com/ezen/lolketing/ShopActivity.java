package com.ezen.lolketing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.ezen.lolketing.adapter.ShopAdapter;
import com.ezen.lolketing.fragment.ShopFragment1;
import com.ezen.lolketing.fragment.ShopFragment2;
import com.ezen.lolketing.fragment.ShopFragment3;
import com.ezen.lolketing.model.ShopDTO;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.rd.PageIndicatorView;

import java.util.ArrayList;

public class ShopActivity extends AppCompatActivity implements ShopAdapter.MoveActivityListener{

    private ShopAdapter adapter;
    private RecyclerView shop_recyclerView;
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    private ViewPager shop_pager;
    private PageIndicatorView pageIndicatorView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        // shop banner
        shop_pager = findViewById(R.id.shop_banner);
        shop_pager.setOffscreenPageLimit(3); // 페이지 갯수

        MyPagerAdapter myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        ShopFragment1 shopFragment1 = new ShopFragment1();
        ShopFragment2 shopFragment2 = new ShopFragment2();
        ShopFragment3 shopFragment3 = new ShopFragment3();
        myPagerAdapter.addItem(shopFragment1);
        myPagerAdapter.addItem(shopFragment2);
        myPagerAdapter.addItem(shopFragment3);
        // 아답터와 페이저 연결
        shop_pager.setAdapter(myPagerAdapter);

        // page indicator
        pageIndicatorView = findViewById(R.id.pageIndicatorView);
        pageIndicatorView.setCount(3);
        pageIndicatorView.setSelection(2);

        shop_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                pageIndicatorView.setSelection(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        // shop recyclerview
        shop_recyclerView = findViewById(R.id.shop_recyclerView);

        Query query = firestore.collection("Shop").orderBy("name");
        FirestoreRecyclerOptions<ShopDTO> options = new FirestoreRecyclerOptions.Builder<ShopDTO>()
                .setQuery(query, ShopDTO.class)
                .build();
        adapter = new ShopAdapter(options, this);

        shop_recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        shop_recyclerView.setAdapter(adapter);
    }

    public void logout(View view) {
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    public void changePage(int index) {
        shop_pager.setCurrentItem(index);
    }

    @Override
    public void MoveActivity(Intent intent) {
        startActivity(intent);
    }

    // MyPagerAdapter class
    class MyPagerAdapter extends FragmentStatePagerAdapter {
        ArrayList<Fragment> items = new ArrayList<Fragment>();

        public MyPagerAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        public void addItem(Fragment item) {
            items.add(item);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return items.get(position);
        }

        @Override
        public int getCount() {
            return items.size();
        }
    }
}
