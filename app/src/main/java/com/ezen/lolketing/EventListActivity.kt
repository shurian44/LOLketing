package com.ezen.lolketing

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_event_list.*

class EventListActivity : AppCompatActivity() {
    private var auth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_list)
        // 신규 회원 이벤트 페이지
        event_card1.setOnClickListener {
            var intent = Intent(this, EventDetailActivity::class.java)
            intent.putExtra("page", 1)
            startActivity(intent)
        }
        // 티켓 구입 이벤트 안내 페이지
        event_card2.setOnClickListener{
            var intent = Intent(this, EventDetailActivity::class.java)
            intent.putExtra("page", 2)
            startActivity(intent)
        }
        // 루렛 이벤트 페이지
        event_card3.setOnClickListener{
            startActivity(Intent(this, RouletteActivity::class.java))
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