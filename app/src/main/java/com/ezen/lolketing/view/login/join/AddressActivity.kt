package com.ezen.lolketing.view.login.join

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import com.ezen.lolketing.R
import com.ezen.lolketing.StatusViewModelActivity
import com.ezen.lolketing.databinding.ActivityAddressBinding
import com.ezen.lolketing.util.Constants
import com.ezen.lolketing.util.addOnScrollListener
import com.ezen.lolketing.util.toast
import com.ezen.lolketing.view.login.adater.AddressAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddressActivity :
    StatusViewModelActivity<ActivityAddressBinding, AddressViewModel>(R.layout.activity_address) {

    override val viewModel: AddressViewModel by viewModels()
    val adapter = AddressAdapter { viewModel.setAddress(it) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initViews()
    }

    /** 각종 뷰들 초기화 **/
    private fun initViews() = with(binding) {
        activity = this@AddressActivity
        vm = viewModel

        // 리사이클러뷰의 마지막까지 스크롤 했을 때 다음 페이지 조회
        recyclerView.addOnScrollListener {
            if (!recyclerView.canScrollVertically(1)) {
                viewModel.fetchAddressList()
            }
        }

    }

    fun addressRegister() {
        var address = ""
        viewModel.info.value.getFullAddress()
            .onSuccess { address = it }
            .onFailure {
                toast(it.message ?: getString(R.string.guide_input_address))
                return
            }

        setResult(
            Activity.RESULT_OK,
            Intent().also {
                it.putExtra(Constants.Address, address)
            }
        )
        finish()
    }

}