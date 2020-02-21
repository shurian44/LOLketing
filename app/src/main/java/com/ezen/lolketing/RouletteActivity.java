package com.ezen.lolketing;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class RouletteActivity extends AppCompatActivity {
    ImageView iv_roulette;
    float startDegree = 0f;
    float endDegree = 0f;
    int degree_rand = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roulette);

        // 애니메이션 이미지 인식
        iv_roulette = (ImageView)findViewById(R.id.roulette);
    }

    // 룰렛 이미지 터치 시에 호출되는 메소드
    public void rotate(View v) {
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
                    Toast.makeText(getApplicationContext(), "결과 그룹1" , Toast.LENGTH_SHORT).show();
                }else if ( degree_rand >= 45 && degree_rand < 90 ) {
                    Toast.makeText(getApplicationContext(), "결과 그룹2" , Toast.LENGTH_SHORT).show();
                }else if ( degree_rand >= 90 && degree_rand < 135 ) {
                    Toast.makeText(getApplicationContext(), "결과 그룹3" , Toast.LENGTH_SHORT).show();
                }else if ( degree_rand >= 135 && degree_rand < 180 ) {
                    Toast.makeText(getApplicationContext(), "결과 그룹4" , Toast.LENGTH_SHORT).show();
                }else if ( degree_rand >= 180 && degree_rand < 225 ) {
                    Toast.makeText(getApplicationContext(), "결과 그룹5" , Toast.LENGTH_SHORT).show();
                }else if ( degree_rand >= 225 && degree_rand < 270 ) {
                    Toast.makeText(getApplicationContext(), "결과 그룹6" , Toast.LENGTH_SHORT).show();
                }else if ( degree_rand >= 270 && degree_rand < 315 ) {
                    Toast.makeText(getApplicationContext(), "결과 그룹7" , Toast.LENGTH_SHORT).show();
                }else if ( degree_rand >= 315 && degree_rand < 360 ) {
                    Toast.makeText(getApplicationContext(), "결과 그룹8" , Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onAnimationCancel(Animator animation) {}
            @Override
            public void onAnimationRepeat(Animator animation) {}
        });

    }
}
