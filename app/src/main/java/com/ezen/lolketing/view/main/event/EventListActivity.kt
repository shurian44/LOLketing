package com.ezen.lolketing.view.main.event

import android.os.Bundle
import android.view.View
import com.ezen.lolketing.BaseActivity
import com.ezen.lolketing.R
import com.ezen.lolketing.databinding.ActivityEventListBinding
import com.ezen.lolketing.util.createIntent
import com.ezen.lolketing.util.startActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EventListActivity : BaseActivity<ActivityEventListBinding>(R.layout.activity_event_list) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initViews()
    }

    /** 각종 뷰들 초기화 **/
    private fun initViews() = with(binding) {
        activity = this@EventListActivity
        title = getString(R.string.event)

        layoutTop.btnBack.setOnClickListener { onBackClick(it) }
    }

    /** 페이지 이동 **/
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
}