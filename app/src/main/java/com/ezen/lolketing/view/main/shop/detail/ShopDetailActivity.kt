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
import com.ezen.lolketing.util.repeatOnCreated
import com.ezen.lolketing.util.showErrorMessageAndFinish
import com.ezen.lolketing.util.startActivity
import com.ezen.lolketing.view.dialog.CommonDialogItem
import com.ezen.lolketing.view.dialog.ConfirmDialog
import com.ezen.lolketing.view.main.shop.basket.CartActivity
import com.ezen.lolketing.view.main.shop.purchase.PurchaseActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class ShopDetailActivity :
    StatusViewModelActivity<ActivityShopDetailBinding, ShopDetailViewModel>(R.layout.activity_shop_detail) {

    override val viewModel: ShopDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        intent
            ?.getIntExtra(Constants.ID, 0)
            ?.let(viewModel::fetchShoppingDetail)
            ?: showErrorMessageAndFinish()

        binding.activity = this
        binding.vm = viewModel

        repeatOnCreated {
            viewModel.uiStatus.collectLatest {
                when(it) {
                    ShopDetailUIStatus.Init -> {}
                    ShopDetailUIStatus.BasketInsertSuccess -> {
                        showInsertSuccessDialog()
                        viewModel.updateInitStatus()
                    }
                }
            }
        }

    }

    fun goToPurchase() {
        launcher.launch(
            createIntent(PurchaseActivity::class.java).also {
                it.putExtra(PurchaseActivity.PURCHASE_LIST, viewModel.item.value.toCartItem())
            }
        )
    }

    fun goToBasket() {
        startActivity(CartActivity::class.java)
    }

    private fun showInsertSuccessDialog() {
        ConfirmDialog(
            dialogItem = CommonDialogItem(
                message = "장바구니에 상품을 추가하였습니다.",
                cancelText = "장바구니 보기",
                okText = "계속 쇼핑하기",
                onCancelClick = ::goToBasket,
                onOkClick = { finish() }
            )
        ).show(supportFragmentManager, "")
    }

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            finish()
        }
    }

}