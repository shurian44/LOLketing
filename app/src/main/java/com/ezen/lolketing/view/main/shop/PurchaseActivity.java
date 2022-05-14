package com.ezen.lolketing.view.main.shop;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ezen.lolketing.view.login.DaumWebViewActivity;
import com.ezen.lolketing.view.main.MainActivity;
import com.ezen.lolketing.R;
import com.ezen.lolketing.model.PurchaseDTO;
import com.ezen.lolketing.model.Users;
import com.ezen.lolketing.view.main.my_page.cache.CacheChargingActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

public class PurchaseActivity extends AppCompatActivity {
    TextView textViewCategory, textViewName, textViewCount, textViewPrice, textViewHP,
            textViewId, textViewAddress;
    ImageView product_image;
    Button btnChange, btnMoney, btnPay;
    Spinner spinner;
    CheckBox checkBox;
//    ConstraintLayout container_purchase, container_purchaseInfo;

    // 파이어 베이스 인증
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    PurchaseDTO purchaseDTO = new PurchaseDTO();
    Users users = new Users();

    private static final int SEARCH_ADDRESS_ACTIVITY = 10000;
    private String id;
    int cache = 0;
    String phone;
    String nickname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase);

        textViewCategory = findViewById(R.id.textViewCategory);
        textViewName = findViewById(R.id.textViewName);
        textViewCount = findViewById(R.id.textViewCount);
        textViewPrice = findViewById(R.id.textViewPrice);
        textViewHP = findViewById(R.id.textViewHP);
        textViewId = findViewById(R.id.textViewId);
        textViewAddress = findViewById(R.id.textViewAddress);
        product_image = findViewById(R.id.product_image);
        spinner = findViewById(R.id.spinner);
        checkBox = findViewById(R.id.checkBox);

//        container_purchase = findViewById(R.id.container_purchase);
//        container_purchaseInfo = findViewById(R.id.container_purchaseInfo);

        // intent로 전달한 데이터 받기
        final Intent intent = getIntent();
        final String category = intent.getStringExtra("category"); // 상품 그룹 받아오기
        final String name = intent.getStringExtra("name"); // 상품명 데이터 받아오기
        final String amount = intent.getStringExtra("amount"); // 수량 받아오기
        final String images = intent.getStringExtra("image");
        final int payment = intent.getIntExtra("payment", 0); // 합계금액 받아오기

//        // 액티비티 재사용을 위한 조건
//        boolean isRecyler = intent.getBooleanExtra("recycler", false);
//        // 액티비티 재사용
//        if(isRecyler) {
//            container_purchaseInfo.setVisibility(View.GONE);
//            container_purchase.setVisibility(View.GONE);
//        }

        Log.e("PurchaseActivity", "구매상품 종류: " + category);
        Log.e("PurchaseActivity", "이미지: " + images);
        Log.e("PurchaseActivity", "구매상품: " + name);
        Log.e("PurchaseActivity", "구매수량: " + Integer.parseInt(amount)); // string to int convert
        Log.e("PurchaseActivity", "결제금액: " + payment);

        // 받아온 데이터 셋팅
        Glide.with(this).load(images).into(product_image);
        textViewCategory.setText(category);
        textViewName.setText(name);
        textViewCount.setText(amount);
        textViewPrice.setText(payment + "원");

        // DB에서 데이터 가져오기
        id = auth.getCurrentUser().getEmail();

        // 버튼 이벤트 처리
        // 주소 변경
        btnChange = findViewById(R.id.btnChange);
        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("PurchaseActivity", "주소 변경 버튼 클릭됨");
                Intent i = new Intent(getApplicationContext(), DaumWebViewActivity.class);
                startActivityForResult(i, SEARCH_ADDRESS_ACTIVITY);
                // 주소를 못받아온다. 뭐지?
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
                        final String address = textViewAddress.getText().toString();
                        final String message = spinner.getSelectedItem().toString();
                        // 셋팅
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
                        // update() : 수정
                        firestore.collection("Users").document(auth.getCurrentUser().getEmail())
                                .update("cache", FieldValue.increment(-payment)); // increment(paymnet)면 증가한다는 의미임.
                        Log.e("PurchaseActivity", "현재 가상머니 금액:" + cache);
                        // DB Purchase
                        firestore.collection("Purchase").document().set(purchaseDTO)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isComplete()) {
                                    // 결제 성공
                                    Log.e("PurchaseActivity", "결제성공");
                                    Intent intent = new Intent(getApplicationContext(), PurchaseResultActivity.class);
                                    // 데이터 전달
                                    intent.putExtra("category", category);
                                    intent.putExtra("name", name);
                                    intent.putExtra("image", images);
                                    intent.putExtra("amount", amount);
                                    intent.putExtra("payment", payment);
                                    intent.putExtra("message", message);
                                    intent.putExtra("nickname", nickname);
                                    intent.putExtra("address", address);
                                    intent.putExtra("phone", phone);
                                    startActivity(intent); // 액티비티 이동
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
                        AlertDialog.Builder builder = new AlertDialog.Builder(PurchaseActivity.this);
                        builder.setTitle("잔액이 부족합니다.");
                        builder.setMessage("캐시를 충전하시겠습니까?");
                        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startActivity(new Intent(getApplication(), CacheChargingActivity.class));
                                dialog.dismiss();
                            }
                        });
                        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                        builder.show();
                        // 캐쉬가 결제금액보다 적을 때
                        Toast.makeText(PurchaseActivity.this,
                                "잔액이 부족합니다.", Toast.LENGTH_SHORT).show();
                        Log.e("PurchaseActivity", "잔액부족으로 결제할 수 없다.");
                    }
                }
            }
        });

        firestore.collection("Users").document(id).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        users = documentSnapshot.toObject(Users.class);
                        cache = users.getCache();
                        phone = users.getPhone();
                        nickname = users.getNickname();
                        textViewId.setText(nickname);
                        textViewAddress.setText(users.getAddress());
                        textViewHP.setText(phone);
                    }
                });
    } // onCreate()

    @Override
    protected void onStart() {
        super.onStart();
        // onCreate() 보다 속도가 빠르고 액티비티로 돌아오거나 새로 올 때마다 다시 시작하기때문에
        // 덮어쓸 경우가 있다.
    }

    // DaumWebView 관련 자동완성되는 코드
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        switch (requestCode) {
            case SEARCH_ADDRESS_ACTIVITY:
                if (resultCode == RESULT_OK) {
                    String data = intent.getExtras().getString("data");
                    if (data != null) {
                        textViewAddress.setText(data);
                    }
                }
        }
    } // onActivityResult

    public void logout(View view) {
        auth.signOut();
    }

    public void moveHome(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
} // end class
