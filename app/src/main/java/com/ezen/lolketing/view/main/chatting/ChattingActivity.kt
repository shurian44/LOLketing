package com.ezen.lolketing.view.main.chatting

import android.os.Bundle
import androidx.activity.viewModels
import com.ezen.lolketing.R
import com.ezen.lolketing.StatusViewModelActivity
import com.ezen.lolketing.adapter.ChattingAdapter
import com.ezen.lolketing.databinding.ActivityChattingBinding
import com.ezen.lolketing.util.getParcelableExtraCompat
import com.ezen.network.model.ChattingRoomInfo
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChattingActivity :
    StatusViewModelActivity<ActivityChattingBinding, ChattingViewModel>(R.layout.activity_chatting) {

    override val viewModel: ChattingViewModel by viewModels()
    val adapter by lazy { ChattingAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.activity = this
        binding.vm = viewModel

        intent?.let {
            val info = it.getParcelableExtraCompat<ChattingRoomInfo>(INFO)
            val selectTeam = it.getStringExtra(SELECT_TEAM)
            viewModel.setInfo(info, selectTeam)
        }

        binding.layoutTop.btnBack.setOnClickListener { finish() }
        binding.editChat.setRegisterListener { viewModel.addChat() }
    }

    companion object {
        const val INFO = "info"
        const val SELECT_TEAM = "selectTeam"
    }
}