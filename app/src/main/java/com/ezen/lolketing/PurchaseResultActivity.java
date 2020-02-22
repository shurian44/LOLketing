package com.ezen.lolketing;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class PurchaseResultActivity extends AppCompatActivity {

    TextView spinnerMessage, textViewHP, textViewId, textViewAddress, textViewPrice,
            textViewCount, textViewName, textViewCategory;
    ImageView product_image;
    Button btnOk, btnMore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_result);

        spinnerMessage = findViewById(R.id.spinnerMessage);
        textViewHP = findViewById(R.id.textViewHP);
        textViewAddress = findViewById(R.id.textViewAddress);
        textViewId = findViewById(R.id.textViewId);
        textViewPrice = findViewById(R.id.textViewPrice);
        textViewCount = findViewById(R.id.textViewCount);
        textViewName = findViewById(R.id.textViewName);
        textViewCategory = findViewById(R.id.textViewCategory);
        product_image = findViewById(R.id.product_image);

        // intent로 전달된 데이터 받기
        Intent intent = getIntent();
        String category = intent.getStringExtra("category");
        String name = intent.getStringExtra("name");
        String image = intent.getStringExtra("image");
        String amount = intent.getStringExtra("amount");
        int payment = intent.getIntExtra("payment", 0);
        String nickname = intent.getStringExtra("nickname");
        String address = intent.getStringExtra("address");
        String phone = intent.getStringExtra("phone");
        String message = intent.getStringExtra("message");

        // 받은 데이터로 셋팅
        Glide.with(this).load(image).into(product_image);
        textViewCategory.setText(category);
        textViewName.setText(name);
        textViewCount.setText(amount);
        textViewPrice.setText(payment + ""); // 변수명 + "" == Integer.toString()
        textViewId.setText(nickname);
        textViewAddress.setText(address);
        textViewHP.setText(phone);
        spinnerMessage.setText(message);

        // 확인 버튼
        btnOk = findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("PurchaseResultActivity", "확인버튼 눌림 -> 메인 액티비티로 이동");
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
        // 전체 구매내역
        btnMore = findViewById(R.id.btnMore);
        btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("PurchaseResultActivity", "구매내역 버튼 눌림 -> 구매내역 액티비티로 이동");
                Intent intent = new Intent(getApplicationContext(), PurchaseHistoryActivity.class);
                startActivity(intent);
            }
        });

    } // onCreate()

    public void logout(View view) {
    }
} // end class
