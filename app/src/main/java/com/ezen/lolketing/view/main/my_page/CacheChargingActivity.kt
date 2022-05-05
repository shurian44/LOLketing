package com.ezen.lolketing.view.main.my_page

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.ezen.lolketing.view.main.MainActivity
import com.ezen.lolketing.R
import com.ezen.lolketing.model.Users
import com.ezen.lolketing.view.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_cache_charging.*
import java.text.DecimalFormat

class CacheChargingActivity : AppCompatActivity() {

    var format = DecimalFormat("###,###,###,###")
    var cache = 0
    private var firestore = FirebaseFirestore.getInstance()
    private var auth = FirebaseAuth.getInstance()
    private lateinit var email : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cache_charging)

        btn_addCache.setOnClickListener {
            email = auth?.currentUser?.email!!
            // 유저 정보 받아오기
            firestore.collection("Users").document(email).get().addOnCompleteListener {
                var user = it.result?.toObject(Users::class.java)!!
                var myCache = user.cache!!
                var point = user.point!!
                var grade = user.grade
                var accPoint = user.accPoint!!

                // 캐시가 20억이 넘을 경우 20억으로 고정 : Int 형 값 넘어가는 오류 발생 방지
                if(myCache + cache.toLong() > 2000000000){
                    point += (2000000000 - myCache) / 10
                    accPoint += (2000000000 - myCache) / 10
                    myCache = 2000000000
                }else{ // 그 외는 충전한 금액 만큼 충전
                    point += cache / 10
                    accPoint += cache / 10
                    myCache += cache
                }

                // 포인트와 누적 포인트도 2억 이상일 경우 2억으로 고정
                if(point > 200000000){
                    point = 200000000
                }
                if(accPoint > 200000000){
                    accPoint = 200000000
                }

                // 마스터 등급을 제외하고 누적 포인트에 따라 등급 설정
                if(grade != "마스터"){
                    grade = when(accPoint){
                        in 0..2900-> "브론즈"
                        in 3000..29999 -> "실버"
                        in 30000..299999 -> "골드"
                        else -> "플래티넘"
                    }
                }
                // 유저 데이터베이스 갱신
                firestore.collection("Users").document(email).update("cache", myCache, "point", point, "grade", grade, "accPoint", accPoint)
                finish()
            }
        }
    } // onCreate()

    // 각 금액 버튼을 클릭
    fun plusCache(view: View) {
        // 금액에 맞게 충전할 캐시 증가
        when(view.id){
            R.id.btn_cache1 -> cache += 100
            R.id.btn_cache2 -> cache += 1000
            R.id.btn_cache3 -> cache += 10000
            R.id.btn_cache4 -> cache += 100000
        }
        // 20억이상 불가능하게 막음
        if(cache > 2000000000){
            cache = 2000000000
        }
        txt_Cache.text = "${format.format(cache)} 원"
    }

    fun logout(view: View) {
        auth.signOut()
        var intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
    }

    fun moveHome(view: View) {
        var intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
    }
}