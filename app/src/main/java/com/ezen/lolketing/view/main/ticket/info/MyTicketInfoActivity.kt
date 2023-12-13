package com.ezen.lolketing.view.main.ticket.info

import android.os.Bundle
import androidx.activity.viewModels
import com.ezen.lolketing.R
import com.ezen.lolketing.StatusViewModelActivity
import com.ezen.lolketing.databinding.ActivityMyTicketInfoBinding
import com.ezen.lolketing.util.showErrorMessageAndFinish
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyTicketInfoActivity :
    StatusViewModelActivity<ActivityMyTicketInfoBinding, MyTicketInfoViewModel>(R.layout.activity_my_ticket_info) {

    override val viewModel: MyTicketInfoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        intent?.getStringExtra(DOCUMENT_ID)?.let {
            viewModel.setDocumentId(it)
        } ?: showErrorMessageAndFinish()

        initViews()
    } // onCreate()

    /** 각종 뷰들 초기화 **/
    private fun initViews() = with(binding) {
        activity = this@MyTicketInfoActivity
        vm = viewModel
        layoutTop.btnBack.setOnClickListener { finish() }
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchTicketInfo()
    }

    companion object {
        const val DOCUMENT_ID = "document_id"
    }

}