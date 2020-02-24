package com.ezen.lolketing

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.ezen.lolketing.model.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_cache_charging.*
import java.text.DecimalFormat
import java.util.logging.SimpleFormatter

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
            firestore.collection("Users").document(email).get().addOnCompleteListener {
                var user = it.result?.toObject(Users::class.java)!!
                var myCache = user.cache!!
                var point = user.point!!
                var grade = user.grade

                if(myCache + cache > 2000000000){
                    point += (2000000000 - myCache) / 10
                    myCache = 2000000000
                }else{
                    point += cache / 10
                    myCache += cache
                }
                if(point > 200000000){
                    point = 200000000
                }

                if(grade != "마스터"){
                    grade = when(point){
                        in 0..2900-> "브론즈"
                        in 3000..29999 -> "실버"
                        in 30000..299999 -> "골드"
                        else -> "플래티넘"
                    }
                }

                firestore.collection("Users").document(email).update("cache", myCache, "point", point, "grade", grade)
                finish()
            }
        }
    }

    fun plusCache(view: View) {
        when(view.id){
            R.id.btn_cache1 -> cache += 100
            R.id.btn_cache2 -> cache += 1000
            R.id.btn_cache3 -> cache += 10000
            R.id.btn_cache4 -> cache += 100000
        }
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