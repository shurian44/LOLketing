package com.ezen.lolketing;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.ezen.lolketing.adapter.ScheduleAdapter;
import com.ezen.lolketing.model.GameDTO;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class GameScheduleActivity extends AppCompatActivity {

    private ScheduleAdapter adapter;
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private RecyclerView schedule_recyclerview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_schedule);

        schedule_recyclerview = findViewById(R.id.schedule_recyclerview);

        Query query = firestore.collection("Game").orderBy("date");
        FirestoreRecyclerOptions<GameDTO> options = new FirestoreRecyclerOptions.Builder<GameDTO>()
                        .setQuery(query, GameDTO.class)
                        .build();

        adapter = new ScheduleAdapter(options);
        schedule_recyclerview.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
        // 어답터를 리사이클러뷰에 연결
        schedule_recyclerview.setAdapter(adapter);
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
}