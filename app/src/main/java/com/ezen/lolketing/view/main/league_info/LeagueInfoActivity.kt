package com.ezen.lolketing.view.main.league_info;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ezen.lolketing.R;
import com.ezen.lolketing.adapter.LeagueInfoAdapter;
import com.ezen.lolketing.fragment.OutlineFragment;
import com.ezen.lolketing.fragment.PrizeFragment;
import com.ezen.lolketing.fragment.ProgressFragment;
import com.ezen.lolketing.view.main.MainActivity;
import com.ezen.lolketing.view.main.shop.GameScheduleActivity;
import com.ezen.lolketing.view.main.ticket.SeatGuideActivity;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

public class LeagueInfoActivity extends AppCompatActivity {

    TextView seat_guide;
    TextView game_schedule;
    private TabLayout tabs;
    private ViewPager viewPager;
    private LeagueInfoAdapter leagueInfoAdapter;
    private FirebaseAuth auth = FirebaseAuth.getInstance();

    OutlineFragment outlineFragment;
    ProgressFragment progressFragment;
    PrizeFragment prizeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_league_info);

        // 프래그먼트 연결
        outlineFragment = new OutlineFragment();
        progressFragment = new ProgressFragment();
        prizeFragment = new PrizeFragment();

        // 좌석안내 클릭시 이벤트
        seat_guide = findViewById(R.id.seat_guide);
        seat_guide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentSeat = new Intent(getApplicationContext(), SeatGuideActivity.class);
                intentSeat.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intentSeat);
            }
        });

        // 경기일정 클릭시 이벤트
        game_schedule = findViewById(R.id.game_schedule);
        game_schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentGame = new Intent(getApplicationContext(), GameScheduleActivity.class);
                intentGame.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intentGame);
            }
        });

        // 탭레이아웃, 뷰페이저
        tabs = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        // 뷰페이저 어답터
        leagueInfoAdapter = new LeagueInfoAdapter(getSupportFragmentManager(), tabs.getTabCount());
        viewPager.setAdapter(leagueInfoAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs));
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}
            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    } // onCreate()


    public void logout(View view) {
        auth.signOut();
    }

    public void moveHome(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
} // class