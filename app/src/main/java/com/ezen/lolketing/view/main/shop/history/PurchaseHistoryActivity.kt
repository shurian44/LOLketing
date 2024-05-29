package com.ezen.lolketing.view.main.shop.history

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.ezen.lolketing.R
import com.ezen.lolketing.StatusViewModelActivity
import com.ezen.lolketing.databinding.ActivityPurchaseHistoryBinding
import com.ezen.lolketing.util.Constants
import com.ezen.lolketing.util.createIntent
import com.ezen.lolketing.view.main.ticket.info.MyTicketInfoActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PurchaseHistoryActivity :
    StatusViewModelActivity<ActivityPurchaseHistoryBinding, PurchaseHistoryViewModel>(R.layout.activity_purchase_history) {
    override val viewModel: PurchaseHistoryViewModel by viewModels()

    val goodsAdapter = PurchaseGoodsHistoryAdapter()
    val ticketAdapter = PurchaseTicketHistoryAdapter { item ->
        startActivity(
            createIntent(MyTicketInfoActivity::class.java).also {
                it.putExtra(Constants.ID, item)
            }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.activity = this
        binding.vm = viewModel
        binding.layoutTop.btnBack.setOnClickListener { finish() }

    }

    fun updateIsTicket(view: View) {
        when(view.id) {
            binding.txtTicket.id -> viewModel.updateIsTicket(true)
            binding.txtGoods.id -> viewModel.updateIsTicket(false)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchItems()
    }
}