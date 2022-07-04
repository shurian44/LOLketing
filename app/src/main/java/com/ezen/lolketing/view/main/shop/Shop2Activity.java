package com.ezen.lolketing.view.main.shop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.ezen.lolketing.view.main.MainActivity;
import com.ezen.lolketing.R;
import com.ezen.lolketing.adapter.ShopAdapter;
import com.ezen.lolketing.adapter.ShopSliderAdapter;
import com.ezen.lolketing.model.ShopDTO;
import com.ezen.lolketing.model.ShopEventDTO;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class Shop2Activity extends AppCompatActivity
        implements ShopAdapter.MoveActivityListener, ShopSliderAdapter.MoveActivityListener {

    private ShopAdapter adapter;
    private RecyclerView shop_recyclerView;
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private FirebaseAuth auth = FirebaseAuth.getInstance();

    ArrayList<ShopDTO> shopDTOS = new ArrayList<>();
    ArrayList<String> images = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        // shop recyclerview
        shop_recyclerView = findViewById(R.id.shop_recyclerView);
        Query query = firestore.collection("Shop").orderBy("name");
        FirestoreRecyclerOptions<ShopDTO> options = new FirestoreRecyclerOptions.Builder<ShopDTO>()
                .setQuery(query, ShopDTO.class)
                .build();
        adapter = new ShopAdapter(options, this);

        shop_recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        shop_recyclerView.setAdapter(adapter);

        final ShopDTO shopDTO = new ShopDTO();

        String group = shopDTO.getGroup();
        String name = shopDTO.getName();
        Long price = shopDTO.getPrice();

        firestore.collection("ShopEvent").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
                            ShopEventDTO shopEventDTO = snapshot.toObject(ShopEventDTO.class);

                            shopDTOS.add(shopEventDTO.getShopDTO());
                            images.add(shopEventDTO.getImage());
                        }

                        // auto slide -> shop event banner
                        SliderView sliderView = findViewById(R.id.imageSlider);
                        ShopSliderAdapter shopSliderAdapter = new ShopSliderAdapter(Shop2Activity.this, images, shopDTOS);
                        sliderView.setSliderAdapter(shopSliderAdapter);
                        sliderView.setIndicatorAnimation(IndicatorAnimations.WORM); // set indicator animation : SliderLayout.IndicatorAnimations.~
                        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
                        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_RIGHT);
                        sliderView.setIndicatorSelectedColor(Color.WHITE);
                        sliderView.setIndicatorUnselectedColor(Color.GRAY);
                        sliderView.setScrollTimeInSec(4); // set scroll delay in seconds
                        sliderView.startAutoCycle();
                    }
                });


    } // onCreate()

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

    // ShopAdapter listener
    @Override
    public void MoveActivity(Intent intent) {
        startActivity(intent);
    }

    // SliderAdapter listener
    @Override
    public void moveActivity(@NotNull Intent intent) { startActivity(intent); }

    public void logout(View view) {
        auth.signOut();
    } // 로그아웃

    public void moveHome(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    } // 메인액티비티로 이동
} // end class
