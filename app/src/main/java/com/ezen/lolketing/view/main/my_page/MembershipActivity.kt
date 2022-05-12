package com.ezen.lolketing.view.main.my_page;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.ezen.lolketing.view.main.MainActivity;
import com.ezen.lolketing.R;
import com.ezen.lolketing.view.login.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class MembershipActivity extends AppCompatActivity {

    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    FirebaseAuth auth = FirebaseAuth.getInstance();

    TextView condition_silver, condition_gold, condition_platinum,
                benefit_bronze, benefit_silver, benefit_gold, benefit_platinum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_membership);

        condition_silver = findViewById(R.id.condition_silver);
        condition_gold = findViewById(R.id.condition_gold);
        condition_platinum = findViewById(R.id.condition_platinum);
        benefit_bronze = findViewById(R.id.benefit_bronze);
        benefit_silver = findViewById(R.id.benefit_silver);
        benefit_gold = findViewById(R.id.benefit_gold);
        benefit_platinum = findViewById(R.id.benefit_platinum);

        condition_silver.setText(Html.fromHtml("누적 포인트<br><font color=\"#6200EE\"><b>3천 P</b></font> 이상"));
        condition_gold.setText(Html.fromHtml("누적 포인트<br><font color=\"#6200EE\"><b>3만 P</b></font> 이상"));
        condition_platinum.setText(Html.fromHtml("누적 포인트<br><font color=\"#6200EE\"><b>30만 P</b></font> 이상"));
        benefit_bronze.setText(Html.fromHtml("상품 구매 시<br>결제 금액<br><font color=\"#8F705B\"><b>2% 할인</b></font>"));
        benefit_silver.setText(Html.fromHtml("상품 구매 시<br>결제 금액<br><font color=\"#64726B\"><b>5% 할인</b></font>"));
        benefit_gold.setText(Html.fromHtml("상품 구매 시<br>결제 금액<br><font color=\"#D4C477\"><b>8% 할인</b></font>"));
        benefit_platinum.setText(Html.fromHtml("상품 구매 시<br>결제 금액<br><font color=\"#679C7A\"><b>11% 할인</b></font>"));
    }

    public void logout(View view) {
        auth.signOut();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void moveHome(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
