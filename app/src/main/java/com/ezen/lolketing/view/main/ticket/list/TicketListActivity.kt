package com.ezen.lolketing.view.main.ticket.list

import android.os.Bundle
import androidx.activity.viewModels
import com.ezen.lolketing.R
import com.ezen.lolketing.StatusViewModelActivity
import com.ezen.lolketing.adapter.TicketListAdapter
import com.ezen.lolketing.databinding.ActivityTicketListBinding
import com.ezen.lolketing.util.Constants
import com.ezen.lolketing.util.createIntent
import com.ezen.lolketing.view.custom.TicketItem
import com.ezen.lolketing.view.dialog.CommonDialogItem
import com.ezen.lolketing.view.dialog.ConfirmDialog
import com.ezen.lolketing.view.main.ticket.detail.ReserveDetailActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TicketListActivity :
    StatusViewModelActivity<ActivityTicketListBinding, TicketListViewModel>(R.layout.activity_ticket_list) {

    override val viewModel: TicketListViewModel by viewModels()

    val adapter = TicketListAdapter { item ->
        when(item.status) {
            TicketItem.FINISH -> { showFinishDialog() }
            TicketItem.SOLD_OUT -> { showSoldOutDialog() }
            else -> {
                startActivity(
                    createIntent(ReserveDetailActivity::class.java).also {
                        it.putExtra(Constants.ID, item.id.toInt())
                    }
                )
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initViews()
    }

    override fun onResume() {
        super.onResume()

        viewModel.fetchGameList()
    }

    /** 각종 뷰들 초기화 **/
    private fun initViews() = with(binding) {
        activity = this@TicketListActivity
        vm = viewModel
    }

    private fun showSoldOutDialog() {
        ConfirmDialog(
            CommonDialogItem(
                message = "선택하신 경기는 매진되었습니다.\n다른 경기를 선택해주세요.",
                isSingleButton = true
            )
        ).show(supportFragmentManager, "")
    }

    private fun showFinishDialog() {
        ConfirmDialog(
            CommonDialogItem(
                message = "티켓 예매가 종료된 경기입니다.\n다른 경기를 선택해주세요.",
                isSingleButton = true
            )
        ).show(supportFragmentManager, "")
    }

}