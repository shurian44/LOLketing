package com.ezen.lolketing.view.main.chatting.list

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.activity.viewModels
import com.ezen.lolketing.R
import com.ezen.lolketing.StatusViewModelActivity
import com.ezen.lolketing.databinding.ActivityChattinglistBinding
import com.ezen.lolketing.model.ChattingInfo
import com.ezen.lolketing.util.createIntent
import com.ezen.lolketing.view.main.chatting.ChattingActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChattingListActivity :
    StatusViewModelActivity<ActivityChattinglistBinding, ChattingListViewModel>(R.layout.activity_chattinglist) {

    override val viewModel: ChattingListViewModel by viewModels()
    val adapter = ChattingListAdapter(::enterChatting)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initViews()

    } // onCreate()

    /** 각종 뷰들 초기화 **/
    private fun initViews() = with(binding) {
        activity = this@ChattingListActivity
        vm = viewModel
    }

    /** 날짜 선택 다이얼로그 **/
    fun showDatePickerDialog() {
        DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                viewModel.setDate(year, month, dayOfMonth)
            },
            viewModel.year,
            viewModel.month - 1,
            viewModel.day
        ).show()
    }

    /** 원하는 팀을 선택해서 채팅방 입장 **/
    private fun enterChatting(info: ChattingInfo) {
        startActivity(
            createIntent(ChattingActivity::class.java).also {
                it.putExtra(ChattingActivity.INfO, info)
            }
        )
    }
}