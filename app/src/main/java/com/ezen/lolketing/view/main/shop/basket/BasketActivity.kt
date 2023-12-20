package com.ezen.lolketing.view.main.shop.basket

import android.os.Bundle
import androidx.activity.viewModels
import com.ezen.lolketing.R
import com.ezen.lolketing.StatusViewModelActivity
import com.ezen.lolketing.databinding.ActivityBasketBinding
import com.ezen.lolketing.util.createIntent
import com.ezen.lolketing.view.main.shop.purchase.ProductAdapter
import com.ezen.lolketing.view.main.shop.purchase.PurchaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BasketActivity : StatusViewModelActivity<ActivityBasketBinding, BasketViewModel>(R.layout.activity_basket) {
    override val viewModel: BasketViewModel by viewModels()
    val adapter = ProductAdapter(
        isBasket = true,
        onClick = { viewModel.updateBasketChecked(it) }
    )

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
                it.putExtra(PurchaseActivity.DATABASE_ID_LIST, viewModel.getIdList())
            }
        )
    }
}