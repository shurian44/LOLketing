package com.ezen.lolketing.view.main.event

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.ezen.lolketing.BaseActivity
import com.ezen.lolketing.view.main.MainActivity
import com.ezen.lolketing.R
import com.ezen.lolketing.databinding.ActivityEventListBinding
import com.ezen.lolketing.view.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth

class EventListActivity : BaseActivity<ActivityEventListBinding>(R.layout.activity_event_list) {
    private var auth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 신규 회원 이벤트 페이지
        binding.eventCard1.setOnClickListener {
            var intent = Intent(this, EventDetailActivity::class.java)
            intent.putExtra("page", 1)
            startActivity(intent)
        }
        // 티켓 구입 이벤트 안내 페이지
        binding.eventCard2.setOnClickListener{
            var intent = Intent(this, EventDetailActivity::class.java)
            intent.putExtra("page", 2)
            startActivity(intent)
        }
        // 룰렛 이벤트 페이지
        binding.eventCard3.setOnClickListener{
            startActivity(Intent(this, RouletteActivity::class.java))
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
}