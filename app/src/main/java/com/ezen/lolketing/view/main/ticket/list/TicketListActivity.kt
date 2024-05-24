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
import com.ezen.lolketing.view.main.ticket.detail.ReserveDetailActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TicketListActivity :
    StatusViewModelActivity<ActivityTicketListBinding, TicketListViewModel>(R.layout.activity_ticket_list) {

    override val viewModel: TicketListViewModel by viewModels()

    val adapter = TicketListAdapter { item ->
        when(item.status) {
            TicketItem.FINISH -> {}
            TicketItem.SOLD_OUT -> {}
            else -> {
                startActivity(
                    createIntent(ReserveDetailActivity::class.java).also {
                        it.putExtra(Constants.ID, item.id)
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

}