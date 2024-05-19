package com.ezen.lolketing.view.main.event

import android.os.Bundle
import androidx.activity.viewModels
import com.ezen.lolketing.R
import com.ezen.lolketing.StatusViewModelActivity
import com.ezen.lolketing.databinding.ActivityEventDetailBinding
import dagger.hilt.android.AndroidEntryPoint

// 제거 예정
@AndroidEntryPoint
class EventDetailActivity : StatusViewModelActivity<ActivityEventDetailBinding, EventDetailViewModel>(R.layout.activity_event_detail) {
    override val viewModel: EventDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initViews()

    }

    /** 각종 뷰들 초기화 **/
    private fun initViews() = with(binding) {
        activity = this@EventDetailActivity
        vm = viewModel

        layoutTop.btnBack.setOnClickListener { finish() }
        viewModel.setPage(intent.getIntExtra(PAGE, NewUser))
    }

    companion object {
        const val PAGE = "page"
        const val NewUser = 1
        const val TicketPurchase = 2
    }

}