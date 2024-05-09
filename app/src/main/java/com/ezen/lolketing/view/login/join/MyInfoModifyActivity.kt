package com.ezen.lolketing.view.login.join

import android.app.Activity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.ezen.lolketing.R
import com.ezen.lolketing.StatusViewModelActivity
import com.ezen.lolketing.databinding.ActivityMyInfoModifyBinding
import com.ezen.lolketing.util.Constants
import com.ezen.lolketing.util.createIntent
import com.ezen.network.model.ModifyInfo
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyInfoModifyActivity :
    StatusViewModelActivity<ActivityMyInfoModifyBinding, MyInfoModifyViewModel>(R.layout.activity_my_info_modify) {

    override val viewModel: MyInfoModifyViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initViews()

    } // onCreate()

    private fun initViews() = with(binding) {
        vm = viewModel
        activity = this@MyInfoModifyActivity
    } // initViews()

    /** 주소 페이지로 이동 **/
    fun moveAddressSearch() {
        launcher.launch(createIntent(AddressActivity::class.java))
    }

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.getStringExtra(Constants.Address)?.let {
                viewModel.updateEditValue(ModifyInfo.TypeAddress, it)
            }
        }
    }

}