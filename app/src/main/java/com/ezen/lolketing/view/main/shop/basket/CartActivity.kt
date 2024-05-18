package com.ezen.lolketing.view.main.shop.basket

import android.os.Bundle
import androidx.activity.viewModels
import com.ezen.lolketing.R
import com.ezen.lolketing.StatusViewModelActivity
import com.ezen.lolketing.databinding.ActivityCartBinding
import com.ezen.lolketing.util.argumentEncode
import com.ezen.lolketing.util.createIntent
import com.ezen.lolketing.view.main.shop.purchase.PurchaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CartActivity :
    StatusViewModelActivity<ActivityCartBinding, CartViewModel>(R.layout.activity_cart) {
    override val viewModel: CartViewModel by viewModels()
    val adapter by lazy {
        CartAdapter(
            isBasket = true,
            onClick = viewModel::updateBasketChecked,
            onDecreaseAmount = viewModel::decreaseAmount,
            onIncreaseAmount = viewModel::increaseAmount
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.activity = this
        binding.vm = viewModel
    }

    override fun onResume() {
        super.onResume()

        viewModel.fetchBasketList()
    }

    fun goToPurchase() {
        startActivity(
            createIntent(PurchaseActivity::class.java).also {
                it.putExtra(PurchaseActivity.PURCHASE_LIST, viewModel.getPurchaseItem())
            }
        )
    }
}