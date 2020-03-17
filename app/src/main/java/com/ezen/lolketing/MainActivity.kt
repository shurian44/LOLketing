package com.ezen.lolketing

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.ezen.lolketing.adapter.EventSliderAdapter
import com.ezen.lolketing.model.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.smarteist.autoimageslider.IndicatorAnimations
import com.smarteist.autoimageslider.SliderAnimations
import com.smarteist.autoimageslider.SliderView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var auth = FirebaseAuth.getInstance()
    var firestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkDetailJoin()   // 상세 회원가입 여부 조회

        eventSlide()    // 이벤트 베너 등록

        btn_board.setOnClickListener {
            // 게시판 선택 다이얼로그 생성
            createDialog()
        }

        //progressDialog(title = "제목", message = "잠시만 기다려주세요")
    }

    // 상세 회원가입 여부 조회
    private fun checkDetailJoin(){
        firestore.collection("Users").document(auth.currentUser?.email!!).addSnapshotListener { documentSnapshot, firebaseFirestoreException ->
            var user = documentSnapshot?.toObject(Users::class.java) ?: return@addSnapshotListener

            // 닉네임이 없으면 상세 회원가입 페이지로 이동
            if(user.nickname == null){
                startActivity(Intent(this, JoinDetailActivity::class.java))
            }

            // 마스터 등급이면 관리자 페이지로 이동
            if(user.grade == "마스터"){
                btn_manager.visibility = View.VISIBLE
            }
        }
    }

    // 이벤트 베너 등록
    private fun eventSlide(){
        // AUTO Slider
        var images = listOf<Int>(R.drawable.banner1, R.drawable.banner2, R.drawable.banner3)
        img_ad.setSliderAdapter(EventSliderAdapter(images))
        img_ad.setIndicatorAnimation(IndicatorAnimations.WORM) // set indicator animation : SliderLayout.IndicatorAnimations.~
        img_ad.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION)
        img_ad.autoCycleDirection = SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH
        img_ad.indicatorSelectedColor = Color.WHITE
        img_ad.indicatorUnselectedColor = Color.GRAY
        img_ad.scrollTimeInSec = 4 // set scroll delay in seconds
        img_ad.startAutoCycle()
    }

    // 게시판 선택 다이얼로그 생성
    private fun createDialog(){
        var builder = AlertDialog.Builder(this)
        var view = layoutInflater.inflate(R.layout.dialog_team, null)
        builder.setView(view)
        val dialog = builder.show()
        var intent = Intent(this, BoardListActivity::class.java)

        // 게시판 버튼 클릭 리스너
        view.findViewById<ImageView>(R.id.icon_t1).setOnClickListener {
            intent.putExtra("team", "T1")
            startActivity(intent)
        }
        view.findViewById<ImageView>(R.id.icon_griffin).setOnClickListener {
            intent.putExtra("team", "GRIFFIN")
            startActivity(intent)
        }
        view.findViewById<ImageView>(R.id.icon_geng).setOnClickListener {
            intent.putExtra("team", "Gen.G Esports")
            startActivity(intent)
        }
        view.findViewById<ImageView>(R.id.icon_dragonx).setOnClickListener {
            intent.putExtra("team", "DragonX")
            startActivity(intent)
        }
        view.findViewById<ImageView>(R.id.icon_freecs).setOnClickListener {
            intent.putExtra("team", "Afreeca Freecs")
            startActivity(intent)
        }
        view.findViewById<ImageView>(R.id.icon_sandbox).setOnClickListener {
            intent.putExtra("team", "SANDBOX Gamming")
            startActivity(intent)
        }
        view.findViewById<ImageView>(R.id.icon_damwon).setOnClickListener {
            intent.putExtra("team", "DAMWON Gamming")
            startActivity(intent)
        }
        view.findViewById<ImageView>(R.id.icon_apk).setOnClickListener {
            intent.putExtra("team", "APK Prince")
            startActivity(intent)
        }
        view.findViewById<ImageView>(R.id.icon_rolster).setOnClickListener {
            intent.putExtra("team", "kt Rolster")
            startActivity(intent)
        }
        view.findViewById<ImageView>(R.id.icon_hanhwa).setOnClickListener {
            intent.putExtra("team", "Hanwha Life Esports")
            startActivity(intent)
        }
    }

    // 각 버튼별 페이지 이동
    fun moveActivity(view: View) {
        var intent = Intent()
        when(view.id){
            R.id.btn_event-> intent = Intent(this, EventListActivity::class.java)
            R.id.btn_myPage-> intent = Intent(this, MyPageActivity::class.java)
            R.id.btn_info-> intent = Intent(this, LeagueInfoActivity::class.java)
            R.id.btn_reserve-> intent = Intent(this, ReserveListActivity::class.java)
            R.id.btn_shop-> intent = Intent(this, ShopActivity::class.java)
            R.id.btn_guid-> intent = Intent(this, LoLGuideActivity::class.java)
            R.id.btn_news-> intent = Intent(this, NewsActivity::class.java)
            R.id.btn_chatting-> intent = Intent(this, ChattingListActivity::class.java)
        }
        startActivity(intent)
    }

    // 로그아웃 버튼 클릭
    fun logout(view: View) {
        auth.signOut()
        var intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
    }

    // 관리자 페이지 버튼 클릭
    fun managerPage(view: View) {
        startActivity(Intent(this, ManagerActivity::class.java))
    }
}
