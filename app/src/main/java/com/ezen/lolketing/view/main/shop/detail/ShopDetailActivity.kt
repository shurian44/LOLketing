package com.ezen.lolketing.view.main.shop.detail

import android.app.Activity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.ezen.lolketing.R
import com.ezen.lolketing.StatusViewModelActivity
import com.ezen.lolketing.databinding.ActivityShopDetailBinding
import com.ezen.lolketing.util.Constants
import com.ezen.lolketing.util.createIntent
import com.ezen.lolketing.util.showErrorMessageAndFinish
import com.ezen.lolketing.view.main.shop.purchase.PurchaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ShopDetailActivity :
    StatusViewModelActivity<ActivityShopDetailBinding, ShopDetailViewModel>(R.layout.activity_shop_detail) {

    override val viewModel: ShopDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        intent
            ?.getStringExtra(Constants.DOCUMENT_ID)
            ?.let { viewModel.setDocumentId(it) }
            ?: showErrorMessageAndFinish()

        binding.activity = this
        binding.vm = viewModel

    }

    fun goToPurchase() {
        launcher.launch(
            createIntent(PurchaseActivity::class.java).also {
                it.putExtra(Constants.DOCUMENT_ID, viewModel.getDocumentId())
                it.putExtra(PurchaseActivity.AMOUNT, viewModel.item.value.amount)
            }
        )
    }

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            finish()
        }
    }

}