package com.ezen.lolketing.view.main.ticket.list

import android.app.Activity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.ezen.lolketing.R
import com.ezen.lolketing.StatusViewModelActivity
import com.ezen.lolketing.adapter.TicketListAdapter
import com.ezen.lolketing.databinding.ActivityTicketListBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TicketListActivity :
    StatusViewModelActivity<ActivityTicketListBinding, TicketListViewModel>(R.layout.activity_ticket_list) {

    override val viewModel: TicketListViewModel by viewModels()

    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                finish()
            }
        }

    val adapter = TicketListAdapter {
//        if (ticket.status == Code.TICKETING_ON.code) {
//            launcher.launch(
//                createIntent(ReserveDetailActivity::class.java).also {
//                    it.putExtra(Constants.TIME, "${ticket.date} ${ticket.time}")
//                    it.putExtra(Constants.TEAM, ticket.team)
//                }
//            )
//        } else {
//            toast(ticket.getMessage())
//        }
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