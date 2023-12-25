package com.ezen.lolketing.view.main.chatting

import android.os.Bundle
import androidx.activity.viewModels
import com.ezen.lolketing.R
import com.ezen.lolketing.StatusViewModelActivity
import com.ezen.lolketing.adapter.ChattingAdapter
import com.ezen.lolketing.databinding.ActivityChattingBinding
import com.ezen.lolketing.model.ChattingInfo
import com.ezen.lolketing.util.getParcelableExtraCompat
import com.ezen.lolketing.util.showErrorMessageAndFinish
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChattingActivity :
    StatusViewModelActivity<ActivityChattingBinding, ChattingViewModel>(R.layout.activity_chatting) {

    override val viewModel: ChattingViewModel by viewModels()
    val adapter by lazy {
        ChattingAdapter(
            leftTeam = viewModel.info.value.leftTeam
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        intent
            ?.getParcelableExtraCompat<ChattingInfo>(INfO)
            ?.let { viewModel.setInfo(it) }
            ?: showErrorMessageAndFinish()

        binding.activity = this
        binding.vm = viewModel

        binding.layoutTop.btnBack.setOnClickListener { finish() }
        binding.editChat.setRegisterListener { viewModel.onRegister() }

    }

    companion object {
        const val INfO = "info"
    }
}