package com.ezen.lolketing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class PurchaseActivity extends AppCompatActivity {
    TextView textViewCategory, textViewName, textViewCount, textViewPrice;
    ImageView img_goods;
    EditText editTextName, editTextAddress;
    Button btnCard, btnDeposit, btnPay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase);

        // intent로 전달한 데이터 받기
        Intent intent = getIntent();
        String category = intent.getStringExtra("category"); // 상품 그룹 받아오기
        String name = intent.getStringExtra("name"); // 상품명 데이터 받아오기
        String amount = intent.getStringExtra("amount"); // 수량 받아오기
        String images = intent.getStringExtra("image");
        int payment = intent.getIntExtra("payment", 0); // 합계금액 받아오기

        Log.e("PurchaseActivity", "구매상품 종류: " + category);
        Log.e("PurchaseActivity", "이미지: " + images);
        Log.e("PurchaseActivity", "구매상품: " + name);
        Log.e("PurchaseActivity", "구매수량: " + Integer.parseInt(amount)); // string to int convert
        Log.e("PurchaseActivity", "결제금액: " + payment);

        textViewCategory = findViewById(R.id.textViewCategory);
        textViewName = findViewById(R.id.textViewName);
        textViewCount = findViewById(R.id.textViewCount);
        textViewPrice = findViewById(R.id.textViewPrice);
        editTextName = findViewById(R.id.editTextName);
        editTextAddress = findViewById(R.id.editTextAddress);
        img_goods = findViewById(R.id.img_goods);

        Glide.with(this).load(images).into(img_goods);
        textViewCategory.setText(category);
        textViewName.setText(name);
        textViewCount.setText(amount);
        textViewPrice.setText(Integer.toString(payment) + "원");
    }

    public void onBtnChange(View v) {
        Toast.makeText(getApplicationContext(), "주소 변경 버튼 클릭", Toast.LENGTH_SHORT).show();
        Log.e("PurchaseActivity", "주소 변경 버튼 클릭됨");
    }
    public void onBtnCard(View v) {
//        btnCard.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
//        btnCard.setTextColor(getResources().getColor(R.color.common_google_signin_btn_text_dark_default));
        Toast.makeText(getApplicationContext(), "신용카드 결제 선택됨", Toast.LENGTH_SHORT).show();
        Log.e("PurchaseActivity", "신용카드 결제 선택됨");
    }

    public void onBtnDeposit(View v) {
        Toast.makeText(getApplicationContext(), "무통장입금 선택됨", Toast.LENGTH_SHORT).show();
        Log.e("PurchaseActivity", "무통장입금 선택됨");
    }

    public void onBtnPay(View v) {
        Toast.makeText(getApplicationContext(), "결제하기 선택됨", Toast.LENGTH_SHORT).show();
        Log.e("PurchaseActivity", "결제하기 선택됨");
    }

    public void logout(View view) {
    }
}
