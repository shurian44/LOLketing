package com.ezen.lolketing.view.login.find

import android.os.Bundle
import androidx.activity.viewModels
import com.ezen.lolketing.R
import com.ezen.lolketing.StatusViewModelActivity
import com.ezen.lolketing.databinding.ActivityFindPasswordBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FindPasswordActivity :
    StatusViewModelActivity<ActivityFindPasswordBinding, FindPasswordViewModel>(R.layout.activity_find_password) {

    override val viewModel: FindPasswordViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.vm = viewModel
    }

}