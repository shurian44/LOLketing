package com.ezen.lolketing

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.View
import com.ezen.lolketing.model.CouponDTO
import com.ezen.lolketing.model.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_event_detail.*
import org.jetbrains.anko.toast

class EventDetailActivity : AppCompatActivity() {
    private var auth = FirebaseAuth.getInstance()
    private var firestore = FirebaseFirestore.getInstance()
    private lateinit var id : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_detail)

        id = auth.currentUser?.email!!
        firestore.collection("Users").document(id).get().addOnCompleteListener {
            var user = it.result?.toObject(Users::class.java)!!
            event_txt1.text = Html.fromHtml("<span>소환사 ${user.nickname}님<br><font color=\"#6200EE\">롤케팅</font>에 오신것을 환영합니다.<br>1회 한정 <font color=\"#6200EE\">500포인트</font>를 발급해드립니다.</span>")
        }


        btn_coupon.setOnClickListener {
            firestore.collection("Coupon").whereEqualTo("title", "신규 가입 쿠폰").whereEqualTo("id", id).addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                for(snapshot in querySnapshot!!){
                    var coupon = snapshot.toObject(CouponDTO::class.java)
                    Log.e("test", coupon.use)
                    if(coupon.use == "사용 안함"){
                        firestore.collection("Coupon").document(snapshot.id).update("use", "사용함")
                        firestore.collection("Users").document(id).update("point", FieldValue.increment(500))
                        toast("${snapshot.id} 포인트 지급")
                    }
                    else{
                        toast("이미 사용하셨습니다.")
                    }
                }
            }
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