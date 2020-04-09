package com.ezen.lolketing

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.ezen.lolketing.model.CouponDTO
import com.ezen.lolketing.model.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_my_page.*

class MyPageActivity : AppCompatActivity() {
    private var firestore = FirebaseFirestore.getInstance()
    private var auth = FirebaseAuth.getInstance()
    private lateinit var id : String
    private lateinit var grade : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_page)

        id = auth.currentUser?.email!!

        firestore.collection("Coupon").whereEqualTo("id", id).get().addOnCompleteListener {
            var coupon = it.result!!.toObjects(CouponDTO::class.java)
            var couponCount = 0
            for(i in coupon.indices){
                if(coupon[i].use == "사용 안함"){
                    couponCount++
                }
            }
            txt_coupon.text = "$couponCount 장"
        }
    }

    override fun onResume() {
        super.onResume()

        // 유저 정보 불러오기
        firestore.collection("Users").document(id).get().addOnCompleteListener {
            var user = it.result?.toObject(Users::class.java)!!
            // 마이페이지 UI에 회원 정보 보여주기
            grade = user.grade!!
            txt_grade.text = grade
            txt_Point.text = "${user.point}"
            txt_accPoint.text = "${user.accPoint}"
            txt_Cache.text = "${user.cache}"
            txt_name.text = user.nickname

            // 등급에 따라 아이콘과 프로그래스 바를 설정
            when(grade){
                "브론즈"->{
                    img_grade.setImageResource(R.drawable.icon_bronze)
                    progress_grade.max = 3000
                    txt_grade_gauge.text = "3천 P"
                }
                "실버"-> {
                    img_grade.setImageResource(R.drawable.icon_silver)
                    progress_grade.max = 30000
                    txt_grade_gauge.text = "3만 P"
                }
                "골드"-> {
                    img_grade.setImageResource(R.drawable.icon_gold)
                    progress_grade.max = 300000
                    txt_grade_gauge.text = "30만 P"
                }
                "플래티넘"-> {
                    img_grade.setImageResource(R.drawable.icon_platinum)
                    progress_grade.max = 0
                    txt_grade_gauge.text = "max"
                }
                "마스터"-> {
                    img_grade.setImageResource(R.drawable.icon_master)
                    progress_grade.max = 0
                    txt_grade_gauge.text = "max"
                }
            }
            progress_grade.progress = user.accPoint!!
        }
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

    // 각 버튼별 페이지 이동
    fun moveActivity(view: View) {
        var intent = Intent()
        when(view.id){
            R.id.txt_Cache -> intent = Intent(this, CacheChargingActivity::class.java)
            R.id.btn_history -> intent = Intent(this, PurchaseHistoryActivity::class.java)
            R.id.txt_withdrawal -> intent = Intent(this, WithdrawalActivity::class.java)
            R.id.txt_coupon -> intent = Intent(this, CouponActivity::class.java)
            R.id.txt_grade_detail -> intent = Intent(this, MembershipActivity::class.java)
            R.id.btn_modify ->{
                intent = Intent(this, JoinDetailActivity::class.java)
                intent.putExtra("modify", "modify")
            }
        }
        startActivity(intent)
    }
}