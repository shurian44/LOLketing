package com.ezen.lolketing.view.login.join

import android.app.Activity
import android.content.SharedPreferences
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
import com.ezen.lolketing.model.Coupon
import com.ezen.lolketing.model.Users
import com.ezen.lolketing.util.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@AndroidEntryPoint
class JoinDetailActivity : BaseViewModelActivity<ActivityJoinDetailBinding, JoinDetailViewModel>(R.layout.activity_join_detail) {

    override val viewModel: JoinDetailViewModel by viewModels()
    @Inject lateinit var pref : SharedPreferences
    private val coupon = Coupon()

    private var isModify = false

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
        isModify = intent?.getBooleanExtra(MODIFY, false) ?: false

        viewModel.getUserId()

        // 회원정보 수정일 경우
        if(isModify){
            viewModel.getUserInfo()
            layoutCheck.isVisible = false
            layoutRegister.isVisible = true
        }

        // 특수문자 제한 및 길이 제한
        editNickname.filters = arrayOf(setSpecialCharacterRestrictions(), InputFilter.LengthFilter(10))
        editPhone.filters = arrayOf(setSpecialCharacterRestrictions(), InputFilter.LengthFilter(11))

        //길이 체크
        editNickname.addTextChangedListener {
            if(editNickname.length() !in 2..10) {
                txtTip.text = getString(R.string.guide_create_nickname)
                editNickname.setStateError(true)
            } else {
                txtTip.text = ""
                editNickname.setStateError(false)
            }
        }

        editPhone.addTextChangedListener {
            if(editPhone.length() !in 10..11) {
                txtTip.text = getString(R.string.guide_create_phone)
                editPhone.setStateError(true)
            } else {
                txtTip.text = ""
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
        when(event) {
            is JoinDetailViewModel.Event.Error -> {
                toast(event.msg)
            }
            is JoinDetailViewModel.Event.UserInfoLoadError -> {
                toast(getString(R.string.error_user_info_search))
            }
            is JoinDetailViewModel.Event.UpdateSuccess -> {
                toast(getString(R.string.guide_update))
                finish()
            }
            is JoinDetailViewModel.Event.UpdateFailure -> {
                toast(getString(R.string.error_update))
            }
            is JoinDetailViewModel.Event.CouponIssuanceSuccess -> {
                viewModel.setNewUserCoupon(coupon)
            }
            is JoinDetailViewModel.Event.CouponIssuanceFailure -> {
                toast(getString(R.string.user_register_failure))
            }
            is JoinDetailViewModel.Event.JoinDetailSuccess -> {
                toast(getString(R.string.user_register_success))
                finish()
            }
            is JoinDetailViewModel.Event.JoinDetailFailure -> {
                toast(getString(R.string.user_register_failure))
            }
        }
    }

    // 등록하기 버튼 클릭
    fun setUser(view: View) {
        val id = pref.getString(Constants.ID, null)
        if (id == null) {
            toast(getString(R.string.user_register_failure))
            return
        }

        // 회원가입 상세의 경우
        if(isModify.not()){
            // 등급을 브론즈로 설정
            // 신규 가입 쿠폰 지급
            coupon.apply {
                this.id = id
                title = Code.NEW_USER_COUPON.code
                use = Code.NOT_USE.code
                limit = getCouponValidityPeriod()
                point = 200
                timestamp = System.currentTimeMillis()
            }
            viewModel.updateNewUserInfo(id)
        } else {
            viewModel.updateModifyUserInfo()
        }
    } // setUser()

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