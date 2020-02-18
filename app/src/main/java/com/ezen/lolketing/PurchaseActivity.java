package com.ezen.lolketing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class PurchaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase);

        Intent intent = getIntent();
        String amount = intent.getStringExtra("amount");
        int totalAmount = Integer.parseInt(amount); // string to int convert // 총 수량
        int payment = intent.getIntExtra("payment", 0);
        Toast.makeText(getApplicationContext(), "선택된 수량: " + amount, Toast.LENGTH_SHORT).show();
        Toast.makeText(getApplicationContext(), "결제 금액: " + payment, Toast.LENGTH_SHORT).show();
        Log.e("amout", "수량: " + amount);
        Log.e("payment", "가격: " + payment);
    }
}
