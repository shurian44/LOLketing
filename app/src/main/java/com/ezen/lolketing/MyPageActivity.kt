package com.ezen.lolketing

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.ezen.lolketing.model.Users
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*
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
            var couponCount = it.result?.size() ?: 0
            txt_coupon.text = "$couponCount 장"
        }

        txt_Cache.setOnClickListener {
            startActivity(Intent(this, CacheChargingActivity::class.java))
        }

        btn_modify.setOnClickListener {
            var intent = Intent(this, JoinDetailActivity::class.java)
            intent.putExtra("modify", "modify")
            startActivity(intent)
        }

        btn_history.setOnClickListener {
            var intent = Intent(this, PurchaseHistoryActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()

        firestore.collection("Users").document(id).get().addOnCompleteListener {
            var user = it.result?.toObject(Users::class.java)!!
            grade = user.grade!!
            txt_grade.text = grade
            txt_Point.text = user.point.toString()
            txt_name.text = user.nickname
            txt_Cache.text = user.cache.toString()

            when(grade){
                "브론즈"->{
                    Glide.with(this).load("https://firebasestorage.googleapis.com/v0/b/lolketing.appspot.com/o/bronze.png?alt=media&token=8826d27f-be2d-423e-a4cb-37b6587d914c").into(img_grade)
                    progress_grade.max = 3000
                    txt_grade_gauge.text = "3천 P"
                }
                "실버"-> {
                    Glide.with(this).load("https://firebasestorage.googleapis.com/v0/b/lolketing.appspot.com/o/silver.png?alt=media&token=083c09ba-b311-45de-ba9c-852f2b73afee").into(img_grade)
                    progress_grade.max = 30000
                    txt_grade_gauge.text = "3만 P"
                }
                "골드"-> {
                    Glide.with(this).load("https://firebasestorage.googleapis.com/v0/b/lolketing.appspot.com/o/gold.png?alt=media&token=42ba695d-2f81-43fa-8e6b-c44bda6c827a").into(img_grade)
                    progress_grade.max = 300000
                    txt_grade_gauge.text = "30만 P"
                }
                "플래티넘"-> {
                    Glide.with(this).load("https://firebasestorage.googleapis.com/v0/b/lolketing.appspot.com/o/platinum.png?alt=media&token=e87bbc24-e3dc-4d9b-8695-96dce9e4979e").into(img_grade)
                    txt_grade_gauge.text = "max"
                }
                "마스터"-> {
                    Glide.with(this).load("https://firebasestorage.googleapis.com/v0/b/lolketing.appspot.com/o/master.png?alt=media&token=72ebd38e-23b2-4462-89ab-598c85d06760").into(img_grade)
                    txt_grade_gauge.text = "max"
                }
            }
            progress_grade.progress = user.point!!
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
}