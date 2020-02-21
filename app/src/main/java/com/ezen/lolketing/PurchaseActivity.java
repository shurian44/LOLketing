package com.ezen.lolketing;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ezen.lolketing.model.PurchaseDTO;
import com.ezen.lolketing.model.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class PurchaseActivity extends AppCompatActivity {
    TextView textViewCategory, textViewName, textViewCount, textViewPrice, textViewHP,
            editTextName, editTextAddress;
    ImageView img_goods;
    ConstraintLayout container_purchase, container_purchaseInfo;
    Button btnChange, btnMoney, btnPay;
    Spinner spinner;
    CheckBox checkBox;

    // 파이어 베이스 인증
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    PurchaseDTO purchaseDTO = new PurchaseDTO();
    Users users = new Users();

    private static final int SEARCH_ADDRESS_ACTIVITY = 10000;
    int cache = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase);

        textViewCategory = findViewById(R.id.textViewCategory);
        textViewName = findViewById(R.id.textViewName);
        textViewCount = findViewById(R.id.textViewCount);
        textViewPrice = findViewById(R.id.textViewPrice);
        textViewHP = findViewById(R.id.textViewHP);
        editTextName = findViewById(R.id.editTextName);
        editTextAddress = findViewById(R.id.editTextAddress);
        img_goods = findViewById(R.id.img_goods);
        spinner = findViewById(R.id.spinner);
        checkBox = findViewById(R.id.checkBox);

        container_purchase = findViewById(R.id.container_purchase);
        container_purchaseInfo = findViewById(R.id.container_purchaseInfo);

        // intent로 전달한 데이터 받기
        final Intent intent = getIntent();
        final String category = intent.getStringExtra("category"); // 상품 그룹 받아오기
        final String name = intent.getStringExtra("name"); // 상품명 데이터 받아오기
        final String amount = intent.getStringExtra("amount"); // 수량 받아오기
        final String images = intent.getStringExtra("image");
        final int payment = intent.getIntExtra("payment", 0); // 합계금액 받아오기
        boolean isRecyler = intent.getBooleanExtra("recycler", false);

        if(isRecyler){
            container_purchaseInfo.setVisibility(View.GONE);
            container_purchase.setVisibility(View.GONE);

        }

        Log.e("PurchaseActivity", "구매상품 종류: " + category);
        Log.e("PurchaseActivity", "이미지: " + images);
        Log.e("PurchaseActivity", "구매상품: " + name);
        Log.e("PurchaseActivity", "구매수량: " + Integer.parseInt(amount)); // string to int convert
        Log.e("PurchaseActivity", "결제금액: " + payment);

        // 받아온 데이터 셋팅
        Glide.with(this).load(images).into(img_goods);
        textViewCategory.setText(category);
        textViewName.setText(name);
        textViewCount.setText(amount);
        textViewPrice.setText(payment + "원");

        // DB에서 데이터 가져오기
        final String id = auth.getCurrentUser().getEmail();
        firestore.collection("Users").document(id).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        users = documentSnapshot.toObject(Users.class);
                        cache = users.getCache();

                        editTextName.setText(users.getNickname());
                        editTextAddress.setText(users.getAddress());
                        textViewHP.setText(users.getPhone());
                    }
                });

        // 버튼 이벤트 처리
        // 주소 변경
        btnChange = findViewById(R.id.btnChange);
        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("PurchaseActivity", "주소 변경 버튼 클릭됨");
                Intent i = new Intent(getApplicationContext(), DaumWebViewActivity.class);
                startActivityForResult(i, SEARCH_ADDRESS_ACTIVITY);
            }
        });

        // 가상머니 버튼
        btnMoney = findViewById(R.id.btnMoney);
        btnMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "현재 잔액: " + cache, Toast.LENGTH_SHORT).show();
                Log.e("PurchaseActivity", "결제방법 가상머니 선택됨, 현재 잔액:" + cache);
            }
        });

        // 결제하기
        btnPay = findViewById(R.id.btnPay);
        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 약관 동의
                if (checkBox.isChecked() != true) {
                    // 약관 동의 안함
                    Toast.makeText(PurchaseActivity.this, "약관에 동의해주세요.", Toast.LENGTH_SHORT).show();
                    Log.e("checkbox", "false");
                    return;
                } else {
                    // 약관 동의함
                    Log.e("checkbox", "true");

                    if (cache >= payment) {
                        // 캐쉬가 결제금액 이상일 때

                        String address = editTextAddress.getText().toString();
                        String message = spinner.getSelectedItem().toString();

                        purchaseDTO.setId(id);
                        purchaseDTO.setImage(images);
                        purchaseDTO.setGroup(category);
                        purchaseDTO.setName(name);
                        purchaseDTO.setAmount(Integer.parseInt(amount));
                        purchaseDTO.setPrice(payment);
                        purchaseDTO.setAddress(address);
                        purchaseDTO.setMessage(message);
                        purchaseDTO.setTimestamp(System.currentTimeMillis());

                        // DB Users
                        firestore.collection("Users").document(auth.getCurrentUser().getEmail()).update("cache", FieldValue.increment(-payment));

                        // DB Purchase
                        firestore.collection("Purchase").document().set(purchaseDTO)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isComplete()) {
                                    // 결제 성공
                                    Log.e("PurchaseActivity", "결제성공");
                                    Intent intent = new Intent(getApplicationContext(), PurchaseActivity.class);
                                    intent.putExtra("category", category);
                                    intent.putExtra("name", name);
                                    intent.putExtra("image", images);
                                    intent.putExtra("amount", amount);
                                    intent.putExtra("payment", payment);
                                    intent.putExtra("recycler", true);

                                    startActivity(intent);
                                } else {
                                    // 결제 실패
                                    Log.e("PurchaseActivity", "결제에 실패했습니다.");
                                    Toast.makeText(PurchaseActivity.this,
                                            "결제에 실패했습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            }
                        });
                    } else if (cache < payment) {
                        // 캐쉬가 결제금액보다 적을 때
                        Toast.makeText(PurchaseActivity.this,
                                "잔액이 부족합니다.", Toast.LENGTH_SHORT).show();
                        Log.e("PurchaseActivity", "잔액부족으로 결제할 수 없다.");
                        return;
                    }
                }
            }
        });
    } // onCreate()

    public void logout(View view) {
    } // logout()

    // DaumWebView 관련 자동완성되는 코드
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        switch (requestCode) {
            case SEARCH_ADDRESS_ACTIVITY:
                if (resultCode == RESULT_OK) {
                    String data = intent.getExtras().getString("data");
                    if (data != null) {
                        editTextAddress.setText(data);
                    }
                }
        }
    } // onActivityResult
}
