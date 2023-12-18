package com.ezen.lolketing.view.main.shop.history

import android.os.Bundle
import androidx.activity.viewModels
import com.ezen.lolketing.R
import com.ezen.lolketing.StatusViewModelActivity
import com.ezen.lolketing.databinding.ActivityPurchaseHistoryBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PurchaseHistoryActivity :
    StatusViewModelActivity<ActivityPurchaseHistoryBinding, PurchaseHistoryViewModel>(R.layout.activity_purchase_history) {
    override val viewModel: PurchaseHistoryViewModel by viewModels()

    val adapter = PurchaseHistoryAdapter {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.activity = this
        binding.vm = viewModel
        binding.layoutTop.btnBack.setOnClickListener { finish() }

    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchPurchaseHistory()
    }
}