package com.ezen.lolketing.view.login.join

import android.app.Activity
import android.os.Bundle
import android.text.InputFilter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import com.ezen.lolketing.R
import com.ezen.lolketing.StatusViewModelActivity
import com.ezen.lolketing.databinding.ActivityJoinDetailBinding
import com.ezen.lolketing.util.createIntent
import com.ezen.lolketing.util.repeatOnStarted
import com.ezen.lolketing.util.setSpecialCharacterRestrictions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class JoinDetailActivity :
    StatusViewModelActivity<ActivityJoinDetailBinding, JoinDetailViewModel>(R.layout.activity_join_detail) {

    override val viewModel: JoinDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initViews()

        repeatOnStarted {
            viewModel.isComplete.collectLatest {
                if (it) {
                    setResult(Activity.RESULT_OK)
                    finish()
                }
            }
        }

    } // onCreate()

    private fun initViews() = with(binding) {
        vm = viewModel
        activity = this@JoinDetailActivity

        viewModel.setIsModify(intent?.getBooleanExtra(MODIFY, false) ?: false)

        // 특수문자 제한 및 길이 제한
        editNickname.filters = arrayOf(setSpecialCharacterRestrictions(), InputFilter.LengthFilter(10))
        editPhone.filters = arrayOf(setSpecialCharacterRestrictions(), InputFilter.LengthFilter(11))

        // 닉네임 길이가 2~10자가 맞는지 체크
        editNickname.addTextChangedListener {
            if(editNickname.length() !in 2..10) {
                txtTipNickname.isVisible = true
                editNickname.setStateError(true)
            } else {
                txtTipNickname.isVisible = false
                editNickname.setStateError(false)
            }
        }

        // 전화번호 길이가 10~11자가 맞는지 체크
        editPhone.addTextChangedListener {
            if(editPhone.length() !in 10..11) {
                txtTipPhone.isVisible = true
                editPhone.setStateError(true)
            } else {
                txtTipPhone.isVisible = false
                editPhone.setStateError(false)
            }
        }
    } // initViews()

    /** 주소 페이지로 이동 **/
    fun moveAddressSearch() {
        launcher.launch(createIntent(AddressActivity::class.java))
    }

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.getStringExtra(ADDRESS)?.let {
                viewModel.setAddress(it)
            }
        }
    }

    companion object {
        const val MODIFY = "modify"
        const val ADDRESS = "address"
    }

}