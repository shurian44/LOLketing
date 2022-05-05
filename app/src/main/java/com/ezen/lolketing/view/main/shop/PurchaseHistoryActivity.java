package com.ezen.lolketing.view.main.shop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.ezen.lolketing.view.main.MainActivity;
import com.ezen.lolketing.R;
import com.ezen.lolketing.adapter.PurchaseAdapter;
import com.ezen.lolketing.model.PurchaseDTO;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class PurchaseHistoryActivity extends AppCompatActivity {

    private PurchaseAdapter adapter;
    private RecyclerView purchase_recyclerView;
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_history);

        purchase_recyclerView = findViewById(R.id.purchase_recyclerView);

        Query query = firestore.collection("Purchase").orderBy("timestamp", Query.Direction.DESCENDING)
                .whereEqualTo("id", auth.getCurrentUser().getEmail());
        FirestoreRecyclerOptions<PurchaseDTO> options = new FirestoreRecyclerOptions.Builder<PurchaseDTO>()
                .setQuery(query, PurchaseDTO.class)
                .build();
        adapter = new PurchaseAdapter(options);

        purchase_recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),
                LinearLayoutManager.VERTICAL, false));
        purchase_recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    } // onStart()

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    } // onStop()

    public void logout(View view) {
        auth.signOut();
    }

    public void moveHome(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
