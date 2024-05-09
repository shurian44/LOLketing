package com.ezen.lolketing.view.main.shop.purchase

import android.app.Activity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.ezen.lolketing.R
import com.ezen.lolketing.StatusViewModelActivity
import com.ezen.lolketing.databinding.ActivityPurchaseBinding
import com.ezen.lolketing.util.Constants
import com.ezen.lolketing.util.createIntent
import com.ezen.lolketing.util.repeatOnCreated
import com.ezen.lolketing.util.startActivity
import com.ezen.lolketing.view.login.join.AddressActivity
import com.ezen.lolketing.view.main.my_page.cache.CacheChargingActivity
import com.ezen.lolketing.view.main.shop.history.PurchaseHistoryActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class PurchaseActivity :
    StatusViewModelActivity<ActivityPurchaseBinding, PurchaseViewModel>(R.layout.activity_purchase) {

    private var initFetch = true
    override val viewModel: PurchaseViewModel by viewModels()
    val adapter = ProductAdapter(
        isBasket = false,
        onClick = {}
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.activity = this
        binding.vm = viewModel

        intent?.let { intent ->
            intent
                .getLongArrayExtra(DATABASE_ID_LIST)
                ?.let { viewModel.setDatabaseIdList(it.toList()) }

            val documentId = intent.getStringExtra(Constants.DOCUMENT_ID)
            val amount = intent.getIntExtra(AMOUNT, 0)

            if (documentId != null && amount > 0) {
                viewModel.buyNowSetting(documentId, amount)
            }
            initFetch = false
        }

        repeatOnCreated {
            viewModel.purchaseStatus.collectLatest {
                when(it) {
                    PurchaseViewModel.PurchaseStatus.Init -> {}
                    PurchaseViewModel.PurchaseStatus.PaymentComplete -> {
                        startActivity(PurchaseHistoryActivity::class.java)
                        setResult(RESULT_OK)
                        finish()
                    }
                    PurchaseViewModel.PurchaseStatus.RequiresCharging -> {
                        startActivity(CacheChargingActivity::class.java)
                    }
                    is PurchaseViewModel.PurchaseStatus.InsufficientBalance -> {
                        binding.btnPayment.text = if (it.isInsufficientBalance) {
                            getString(R.string.charging_cash)
                        } else {
                            getString(R.string.payment)
                        }
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()

        if (initFetch.not()) {
            viewModel.fetchPurchaseInfo()
        }
    }

    fun moveAddressSearch() {
        launcher.launch(createIntent(AddressActivity::class.java))
    }

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.getStringExtra(Constants.Address)?.let {
                viewModel.setAddress(it)
            }
        }
    }

    companion object {
        const val DATABASE_ID_LIST = "database id list"
        const val AMOUNT = "amount"
    }
}