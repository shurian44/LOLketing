package com.ezen.lolketing;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.ezen.lolketing.adapter.ShopAdapter;
import com.ezen.lolketing.model.ShopDTO;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class ShopActivity extends AppCompatActivity {

    private ShopAdapter adapter;
    private RecyclerView shop_recyclerView;
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        shop_recyclerView = findViewById(R.id.shop_recyclerView);

        Query query = firestore.collection("Shop").orderBy("name");
        FirestoreRecyclerOptions<ShopDTO> options = new FirestoreRecyclerOptions.Builder<ShopDTO>()
                .setQuery(query, ShopDTO.class)
                .build();
        adapter = new ShopAdapter(options);

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
}
