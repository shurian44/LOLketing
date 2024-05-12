package com.ezen.lolketing.view.main.shop

import android.app.Activity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.ezen.lolketing.R
import com.ezen.lolketing.StatusViewModelActivity
import com.ezen.lolketing.databinding.ActivityShopBinding
import com.ezen.lolketing.util.Constants
import com.ezen.lolketing.util.createIntent
import com.ezen.lolketing.util.startActivity
import com.ezen.lolketing.view.main.shop.basket.BasketActivity
import com.ezen.lolketing.view.main.shop.detail.ShopDetailActivity
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ShopActivity :
    StatusViewModelActivity<ActivityShopBinding, ShopViewModel>(R.layout.activity_shop) {

    override val viewModel: ShopViewModel by viewModels()
    val adapter = ShopAdapter { goodsId ->
        startActivity(
            createIntent(ShopDetailActivity::class.java).also {
                it.putExtra(Constants.ID, goodsId)
            }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initViews()
    }

    private fun initViews() = with(binding) {
        activity = this@ShopActivity
        vm = viewModel

        viewModel.tabList.forEachIndexed { index, name ->
            tabLayout.addTab(
                tabLayout.newTab().apply {
                    text = name
                    id = index
                }
            )
        }

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let { viewModel.updateIndex(it.id) }
            }
        })
    }

    fun goToBasket() {
        startActivity(BasketActivity::class.java)
    }

}