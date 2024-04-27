package com.ezen.lolketing.view.login.join

import android.app.Activity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.ezen.auth.model.JoinInfo
import com.ezen.lolketing.R
import com.ezen.lolketing.StatusViewModelActivity
import com.ezen.lolketing.databinding.ActivityJoinBinding
import com.ezen.lolketing.util.Constants
import com.ezen.lolketing.util.createIntent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class JoinActivity :
    StatusViewModelActivity<ActivityJoinBinding, JoinViewModel>(R.layout.activity_join) {

    override val viewModel: JoinViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.vm = viewModel
        binding.activity = this

        intent.getStringExtra(Constants.JoinType)?.let(viewModel::setType)
    }

    fun moveAddressSearch() {
        launcher.launch(createIntent(AddressActivity::class.java))
    }

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.getStringExtra(JoinDetailActivity.ADDRESS)?.let {
                viewModel.updateEditValue(JoinInfo.TypeAddress, it)
            }
        }
    }

}