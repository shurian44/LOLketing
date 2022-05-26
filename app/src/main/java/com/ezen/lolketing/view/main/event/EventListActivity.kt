package com.ezen.lolketing.view.main.event

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.ezen.lolketing.BaseActivity
import com.ezen.lolketing.view.main.MainActivity
import com.ezen.lolketing.R
import com.ezen.lolketing.databinding.ActivityEventListBinding
import com.ezen.lolketing.util.createIntent
import com.ezen.lolketing.util.startActivity
import com.ezen.lolketing.view.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class EventListActivity : BaseActivity<ActivityEventListBinding>(R.layout.activity_event_list) {

    @Inject lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initViews()

    }

    private fun initViews() = with(binding) {
        activity = this@EventListActivity
        title = getString(R.string.event)

        layoutTop.btnBack.setOnClickListener { onBackClick(it) }

    }

    fun moveActivity(view: View) = with(binding) {
        when(view.id) {
            cardEvent1.id -> {
                // 신규 회원 이벤트 페이지
                startActivity(createIntent(EventDetailActivity::class.java).also {
                    it.putExtra(EventDetailActivity.PAGE, 1)
                })
            }
            cardEvent2.id -> {
                // 티켓 구입 이벤트 안내 페이지
                startActivity(createIntent(EventDetailActivity::class.java).also {
                    it.putExtra(EventDetailActivity.PAGE, 2)
                })
            }
            cardEvent3.id -> {
                // 룰렛 이벤트 페이지
                startActivity(RouletteActivity::class.java)
            }
        }
    }

    override fun logout(view: View) {
        auth.signOut()
        startActivity(LoginActivity::class.java, Intent.FLAG_ACTIVITY_CLEAR_TOP)
        finish()
    }

    override fun moveHome(view: View) {
        startActivity(MainActivity::class.java, Intent.FLAG_ACTIVITY_CLEAR_TOP)
        finish()
    }
}