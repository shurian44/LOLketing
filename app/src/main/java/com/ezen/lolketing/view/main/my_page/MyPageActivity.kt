package com.ezen.lolketing.view.main.my_page

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.ezen.lolketing.BaseActivity
import com.ezen.lolketing.view.main.MainActivity
import com.ezen.lolketing.view.main.shop.PurchaseHistoryActivity
import com.ezen.lolketing.R
import com.ezen.lolketing.WithdrawalActivity
import com.ezen.lolketing.databinding.ActivityMyPageBinding
import com.ezen.lolketing.model.CouponDTO
import com.ezen.lolketing.model.Users
import com.ezen.lolketing.view.login.LoginActivity
import com.ezen.lolketing.view.login.join.JoinDetailActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MyPageActivity : BaseActivity<ActivityMyPageBinding>(R.layout.activity_my_page) {
    private var firestore = FirebaseFirestore.getInstance()
    private var auth = FirebaseAuth.getInstance()
    private lateinit var id : String
    private lateinit var grade : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        id = auth.currentUser?.email!!

        firestore.collection("Coupon").whereEqualTo("id", id).get().addOnCompleteListener {
            var coupon = it.result!!.toObjects(CouponDTO::class.java)
            var couponCount = 0
            for(i in coupon.indices){
                if(coupon[i].use == "사용 안함"){
                    couponCount++
                }
            }
            binding.txtCoupon.text = "$couponCount 장"
        }
    }

    override fun onResume() {
        super.onResume()

        // 유저 정보 불러오기
        firestore.collection("Users").document(id).get().addOnCompleteListener {
            var user = it.result?.toObject(Users::class.java)!!
            // 마이페이지 UI에 회원 정보 보여주기
            grade = user.grade!!
            binding.txtGrade.text = grade
            binding.txtPoint.text = "${user.point}"
            binding.txtAccPoint.text = "${user.accPoint}"
            binding.txtCache.text = "${user.cache}"
            binding.txtName.text = user.nickname

            // 등급에 따라 아이콘과 프로그래스 바를 설정
            when(grade){
                "브론즈"->{
                    binding.imgGrade.setImageResource(R.drawable.icon_bronze)
                    binding.progressGrade.max = 3000
                    binding.txtGradeGauge.text = "3천 P"
                }
                "실버"-> {
                    binding.imgGrade.setImageResource(R.drawable.icon_silver)
                    binding.progressGrade.max = 30000
                    binding.txtGradeGauge.text = "3만 P"
                }
                "골드"-> {
                    binding.imgGrade.setImageResource(R.drawable.icon_gold)
                    binding.progressGrade.max = 300000
                    binding.txtGradeGauge.text = "30만 P"
                }
                "플래티넘"-> {
                    binding.imgGrade.setImageResource(R.drawable.icon_platinum)
                    binding.progressGrade.max = 0
                    binding.txtGradeGauge.text = "max"
                }
                "마스터"-> {
                    binding.imgGrade.setImageResource(R.drawable.icon_master)
                    binding.progressGrade.max = 0
                    binding.txtGradeGauge.text = "max"
                }
            }
            binding.progressGrade.progress = user.accPoint!!
        }
    }

    override fun logout(view: View) {
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