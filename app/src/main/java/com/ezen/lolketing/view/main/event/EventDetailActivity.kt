package com.ezen.lolketing.view.main.event

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.ezen.lolketing.BaseActivity
import com.ezen.lolketing.view.main.MainActivity
import com.ezen.lolketing.R
import com.ezen.lolketing.databinding.ActivityEventDetailBinding
import com.ezen.lolketing.model.CouponDTO
import com.ezen.lolketing.model.Users
import com.ezen.lolketing.util.toast
import com.ezen.lolketing.view.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class EventDetailActivity : BaseActivity<ActivityEventDetailBinding>(R.layout.activity_event_detail) {
    private var auth = FirebaseAuth.getInstance()
    private var firestore = FirebaseFirestore.getInstance()
    private lateinit var id : String
    private var coupon = CouponDTO()
    private var documentID = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var page = intent.getIntExtra("page", 1)
        // 신규 회원가입 페이지의 경우
        if(page == 1) {
            setNewUserPage()
        }

        // 티켓 구매 이벤트 안내 페이지 일 경우
        if(page == 2){
            binding.imgMain.setImageResource(R.drawable.banner2)
            binding.eventTxt1.text = Html.fromHtml("티켓 구매하시고 <big><font color=\"#6200EE\">RP</big></font> 받아가세요~!<br><br><br>- 구매 티켓 1장 당 룰렛 1회 이용 가능<br>- 해당 이벤트는 <font color=\"#6200EE\">횟 수 제한 없이</font> 참여 가능합니다.")
            binding.btnCoupon.visibility = View.GONE
        }
    } // onCreate()

    private fun setNewUserPage(){
        binding.imgMain.setImageResource(R.drawable.banner1)
        id = auth.currentUser?.email!!
        // 사용자의 닉네임 가져오기
        firestore.collection("Users").document(id).get().addOnCompleteListener {
            var user = it.result?.toObject(Users::class.java)!!
            binding.eventTxt1.text = Html.fromHtml("<span>소환사 <font color=\"#6200EE\">${user.nickname}</font>님<br><font color=\"#6200EE\">롤케팅</font>에 오신것을 환영합니다.<br><br><br>환영의 의미로 <font color=\"#6200EE\">500포인트</font>를 발급해드립니다.</span>")
        }
        // 사용자의 신규 회원 쿠폰 가져오기
        firestore.collection("Coupon").whereEqualTo("title", "신규 가입 쿠폰").whereEqualTo("id", id).addSnapshotListener { querySnapshot, firebaseFirestoreException ->
            for (snapshot in querySnapshot!!) {
                coupon = snapshot.toObject(CouponDTO::class.java)
                documentID = snapshot.id
            }
        }
        // 신규 회원 쿠폰 사용 버튼 클릭
        binding.btnCoupon.setOnClickListener {
            if (coupon.use == "사용 안함") { // 신규 회원 쿠폰을 사용하지 않았을 경우
                // 신규 회원 쿠폰 업데이트
                firestore.collection("Coupon").document(documentID).update("use", "사용함")
                // 포인트 지급
                firestore.collection("Users").document(id).update("point", FieldValue.increment(500)).addOnCompleteListener {
                    while (!it.isComplete){} // 중복 지급 방지
                }
                toast("500 포인트가 지급되었습니다.")
            } else { // 신규 회원 쿠폰을 이미 사용했을 경우
                toast("이미 사용하셨습니다.")
            }
        } // setOnClickListener()
    } // setNewUserPage()

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
}