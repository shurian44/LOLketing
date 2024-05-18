package com.ezen.lolketing.view.main.shop.purchase

import android.app.Activity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.ezen.database.entity.CartItem
import com.ezen.lolketing.R
import com.ezen.lolketing.StatusViewModelActivity
import com.ezen.lolketing.databinding.ActivityPurchaseBinding
import com.ezen.lolketing.util.Constants
import com.ezen.lolketing.util.createIntent
import com.ezen.lolketing.util.getParcelableExtraCompat
import com.ezen.lolketing.util.repeatOnCreated
import com.ezen.lolketing.util.toast
import com.ezen.lolketing.view.dialog.CashChargingDialog
import com.ezen.lolketing.view.dialog.CommonDialogItem
import com.ezen.lolketing.view.dialog.ConfirmDialog
import com.ezen.lolketing.view.dialog.PaymentDialog
import com.ezen.lolketing.view.login.join.AddressActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class PurchaseActivity :
    StatusViewModelActivity<ActivityPurchaseBinding, PurchaseViewModel>(R.layout.activity_purchase) {

    override val viewModel: PurchaseViewModel by viewModels()
    val adapter by lazy {
        ProductAdapter(
            isBasket = false,
            onDelete = viewModel::deleteItem,
            onDecreaseAmount = viewModel::decreaseAmount,
            onIncreaseAmount = viewModel::increaseAmount
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.activity = this
        binding.vm = viewModel

        intent
            ?.getParcelableExtraCompat<CartItem>(PURCHASE_LIST)
            ?.let(viewModel::fetchPurchaseInfo)
            ?: run {
                toast("결제 정보 조회 실패")
                finish()
            }

        repeatOnCreated {
            viewModel.item.collectLatest {
                when (it.uiStatus) {
                    PurchaseUiStatus.Init -> {}
                    PurchaseUiStatus.LastItemDelete -> {
                        showLastItemDeleteDialog()
                    }
                    PurchaseUiStatus.MakePayment -> {
                        showPaymentDialog()
                    }
                    PurchaseUiStatus.NeedCashCharging -> {
                        showCashChargingDialog()
                    }
                    PurchaseUiStatus.PaymentSuccess -> {
                        onSuccessFinish()
                    }
                }
            }
        }
    }

    fun moveAddressSearch() {
        launcher.launch(createIntent(AddressActivity::class.java))
    }

    private fun showCashChargingDialog() {
        CashChargingDialog(
            myCash = viewModel.item.value.purchaseInfo.getFormatCash(),
            onOkClick = viewModel::updateCashCharging
        ).show(supportFragmentManager, "")
    }

    private fun showPaymentDialog() {
        val value = viewModel.item.value
        PaymentDialog(
            myCash = value.purchaseInfo.cash,
            totalPrice = value.cartItem.totalPrice,
            onClick = viewModel::makePayment
        ).show(supportFragmentManager, "")
    }

    private fun showLastItemDeleteDialog() {
        ConfirmDialog(
            CommonDialogItem(
                message = "해당 상품을 삭제하면\n결제를 진행하실 수 없습니다.\n그래도 삭제하시겠습니까?",
                okButtonColor = R.color.red,
                onOkClick = { finish() }
            )
        ).show(supportFragmentManager, "")
    }

    private fun onSuccessFinish() {
        setResult(RESULT_OK)
        finish()
    }

    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.getStringExtra(Constants.Address)?.let {
                    viewModel.setAddress(it)
                }
            }
        }

    companion object {
        const val PURCHASE_LIST = "purchase list"
    }
}