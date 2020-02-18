package com.ezen.lolketing;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ezen.lolketing.adapter.ScheduleAdapter;
import com.ezen.lolketing.adapter.ShopAdapter;
import com.ezen.lolketing.model.GameDTO;
import com.ezen.lolketing.model.ShopDTO;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ShopDetailActivity extends AppCompatActivity {
    TextView product_name;
    TextView textViewCount;
    TextView textViewPrice;
    ImageView product_img1, product_img2, product_img3;
    Button btn_plus;
    Button btn_minus;
    Button btn_purchase;

    private int count = 0;
    private String amount;
    private int price;
    private int payment;

//    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_detail);

        // intent로 전달한 데이터 받기
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        price = intent.getIntExtra("price", 0);

        ArrayList<String> images = intent.getStringArrayListExtra("image");

//        DB로 접근할 때 방법
//        firestore.collection("Shop").whereEqualTo("name", name).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//            @Override
//            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//
//            }
//        });

        product_name = findViewById(R.id.product_name);
        product_img1 = findViewById(R.id.product_img1);
        product_img2 = findViewById(R.id.product_img2);
        product_img3 = findViewById(R.id.product_img3);
        textViewCount = findViewById(R.id.textViewCount);
        textViewPrice = findViewById(R.id.textViewPrice);
        btn_plus = findViewById(R.id.btn_plus);
        btn_minus = findViewById(R.id.btn_minus);

        product_name.setText(name);
        Glide.with(this).load(images.get(0)).into(product_img1);
        Glide.with(this).load(images.get(1)).into(product_img2);
        Glide.with(this).load(images.get(2)).into(product_img3);

        btn_purchase = findViewById(R.id.btn_purchase);
        btn_purchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentPurchase = new Intent(getApplicationContext(), PurchaseActivity.class);
                intentPurchase.putExtra("amount", amount);
                intentPurchase.putExtra("payment", payment);
                intentPurchase.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intentPurchase);
            }
        });
    }

    public void onBtnMinus(View view) {
        if (count > 1) {
            count--;
        } else {
            Toast.makeText(getApplicationContext(), "잘못된 수량입니다.",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        amount = Integer.toString(count);
        textViewCount.setText(amount);
        payment = price * Integer.parseInt(amount);
        textViewPrice.setText(Integer.toString(payment) + "원");
    }

    public void onBtnPlus(View view) {
        if (count < 999) {
            count++;
        } else {
            Toast.makeText(getApplicationContext(), "잘못된 수량입니다.",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        amount = Integer.toString(count);
        textViewCount.setText(amount);
        payment = price * Integer.parseInt(amount);
        textViewPrice.setText(Integer.toString(payment) + "원");
    }

    public void logout(View view) {
    }
}
