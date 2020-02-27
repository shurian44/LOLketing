package com.ezen.lolketing;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ezen.lolketing.model.CouponDTO;
import com.ezen.lolketing.model.Users;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class RouletteActivity extends AppCompatActivity {

    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    ImageView iv_roulette;
    TextView txt_count;
    float startDegree = 0f;
    float endDegree = 0f;
    int degree_rand = 0;
    String result = "";
    int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roulette);

        // 애니메이션 이미지 인식
        iv_roulette = findViewById(R.id.roulette);
        txt_count = findViewById(R.id.txt_count);

        firestore.collection("Users").document(auth.getCurrentUser().getEmail()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Users user = documentSnapshot.toObject(Users.class);
                count = user.getRouletteCount();
                txt_count.setText("남은 횟수 : " + count + " 번");
            }
        });
    }

    // 룰렛 이미지 터치 시에 호출되는 메소드
    public void rotate(View v) {
        if(count <= 0){
            Toast.makeText(this, "기회를 다 소진하였습니다.", Toast.LENGTH_SHORT).show();
            return;
        }
        // ---------- 회전각도 설정 ----------
        startDegree = 0;    // 이전 정지 각도를 시작 각도로 설정
        Random rand = new Random(); // 랜덤 객체 생성
        degree_rand = rand.nextInt(360);    // 0~359 사이의 정수 추출
        endDegree = startDegree + 360 * 5 + degree_rand;  // 회전 종료각도 설정(초기 각도에서 세바퀴 돌고 0~359 난수만큼 회전)

        // ---------- 애니메이션 실행 ----------
        // 애니메이션 이미지에 대해 초기 각도에서 회전종료 각도까지 회전하는 애니메이션 객체 생성
        ObjectAnimator object = ObjectAnimator.ofFloat(iv_roulette, "rotation", startDegree, endDegree);

        object.setInterpolator(new AccelerateDecelerateInterpolator()); // 애니메이션 속력 설정
        object.setDuration(5000);   // 애니메이션 시간(5초)
        object.start();   // 애니메이션 시작
        object.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {}
            @Override
            public void onAnimationEnd(Animator animation) {
                if ( degree_rand >= 0 && degree_rand < 45 ) {
                    result = "2000RP";
                    Toast.makeText(getApplicationContext(), "결과 그룹1" , Toast.LENGTH_SHORT).show();
                }else if ( degree_rand >= 45 && degree_rand < 90 ) {
                    result = "300RP";
                    Toast.makeText(getApplicationContext(), "결과 그룹2" , Toast.LENGTH_SHORT).show();
                }else if ( degree_rand >= 90 && degree_rand < 135 ) {
                    result = "350RP";
                    Toast.makeText(getApplicationContext(), "결과 그룹3" , Toast.LENGTH_SHORT).show();
                }else if ( degree_rand >= 135 && degree_rand < 180 ) {
                    result = "200RP";
                    Toast.makeText(getApplicationContext(), "결과 그룹4" , Toast.LENGTH_SHORT).show();
                }else if ( degree_rand >= 180 && degree_rand < 225 ) {
                    result = "1000RP";
                    Toast.makeText(getApplicationContext(), "결과 그룹5" , Toast.LENGTH_SHORT).show();
                }else if ( degree_rand >= 225 && degree_rand < 270 ) {
                    result = "250RP";
                    Toast.makeText(getApplicationContext(), "결과 그룹6" , Toast.LENGTH_SHORT).show();
                }else if ( degree_rand >= 270 && degree_rand < 315 ) {
                    result = "450RP";
                    Toast.makeText(getApplicationContext(), "결과 그룹7" , Toast.LENGTH_SHORT).show();
                }else if ( degree_rand >= 315 && degree_rand < 360 ) {
                    result = "150RP";
                    Toast.makeText(getApplicationContext(), "결과 그룹8" , Toast.LENGTH_SHORT).show();
                }

                CouponDTO coupon = new CouponDTO();
                coupon.setId(auth.getCurrentUser().getEmail());
                coupon.setLimit("2222.01.01");
                coupon.setTitle("룰렛 쿠폰");
                coupon.setUse("사용 안함");
                coupon.setCouponNumber(setCouponNumber());

                firestore.collection("Users").document(auth.getCurrentUser().getEmail()).update("rouletteCount", FieldValue.increment(-1));
                firestore.collection("Coupon").document().set(coupon);
                count -= 1;
                txt_count.setText("남은 횟수 : " + count + " 번");
            }
            @Override
            public void onAnimationCancel(Animator animation) {}
            @Override
            public void onAnimationRepeat(Animator animation) {}
        });

    }

    private String setCouponNumber(){
        String couponNumber = "";
        String[] chooseNum = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R"
                , "S", "T", "U", "V", "W", "X", "Y", "Z", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
        Random random = new Random();

        for(int i = 0; i < 16; i++) {
            couponNumber += chooseNum[random.nextInt(36)];
            if(i % 4 == 3 && i != 15) {
                couponNumber += "-";
            }
        }
        return couponNumber;
    }

    public void logout(View view) {
        auth.signOut();
    }

    public void moveHome(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
