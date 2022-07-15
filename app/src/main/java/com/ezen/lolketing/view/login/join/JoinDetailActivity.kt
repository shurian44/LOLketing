package com.ezen.lolketing.view.login.join

import android.app.Activity
import android.os.Bundle
import android.text.InputFilter
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import com.ezen.lolketing.BaseViewModelActivity
import com.ezen.lolketing.R
import com.ezen.lolketing.databinding.ActivityJoinDetailBinding
import com.ezen.lolketing.util.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class JoinDetailActivity : BaseViewModelActivity<ActivityJoinDetailBinding, JoinDetailViewModel>(R.layout.activity_join_detail) {

    override val viewModel: JoinDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        repeatOnStarted {
            viewModel.eventFlow.collect { event -> eventHandler(event) }
        }

        initViews()

    } // onCreate()

    private fun initViews() = with(binding) {
        vm = viewModel
        activity = this@JoinDetailActivity
        viewModel.isModify = intent?.getBooleanExtra(MODIFY, false) ?: false

        viewModel.getUserInfo(viewModel.isModify)

        // 회원정보 수정일 경우
        if(viewModel.isModify){
            layoutCheck.isVisible = false
            layoutRegister.isVisible = true
        }

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
                txtTipNickname.isVisible = false
                editPhone.setStateError(false)
            }
        }

        // 이용약관 체크박스 채크
        ckAcceptTerms.setOnClickListener {
            ckAcceptTerms.toggle()
            layoutCheck.isVisible = false
            layoutRegister.isVisible = true
        }
    }

    private fun eventHandler(event: JoinDetailViewModel.Event) {
        dismissDialog()
        when(event) {
            is JoinDetailViewModel.Event.Error -> {
                toast(event.msg)
            }
            is JoinDetailViewModel.Event.Loading -> {
                showDialog()
            }
            is JoinDetailViewModel.Event.UserInfoLoadError -> {
                toast(getString(R.string.error_user_info_search))
                finish()
            }
            is JoinDetailViewModel.Event.UpdateSuccess -> {
                toast(getString(R.string.guide_update))
                finish()
            }
            is JoinDetailViewModel.Event.UpdateFailure -> {
                toast(getString(R.string.error_update))
            }
            is JoinDetailViewModel.Event.CouponIssuanceSuccess -> {
                toast(getString(R.string.user_register_success))
                finish()
            }
            is JoinDetailViewModel.Event.CouponIssuanceFailure -> {
                toast(getString(R.string.user_register_failure))
            }
            is JoinDetailViewModel.Event.JoinDetailFailure -> {
                toast(getString(R.string.user_register_failure))
            }
        }
    }

    // 등록하기 버튼 클릭
    fun setUser(view: View) {
        // 회원가입 상세의 경우
        if(viewModel.isModify.not()){
            // 등급을 브론즈로 설정
            viewModel.updateNewUserInfo()
        } else {
            viewModel.updateModifyUserInfo()
        }
    } // setUser()

    /** 주소 페이지로 이동 **/
    fun moveAddressSearch(view: View) {
        launcher.launch(createIntent(AddressActivity::class.java))
    }

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.getStringExtra(ADDRESS)?.let {
                binding.editAddress.setText(it)
            }
        }
    }

    companion object {
        const val MODIFY = "modify"
        const val ADDRESS = "address"
    }

}