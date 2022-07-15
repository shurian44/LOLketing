package com.ezen.lolketing.view.login.join

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ezen.lolketing.BaseViewModel
import com.ezen.lolketing.model.Coupon
import com.ezen.lolketing.model.Users
import com.ezen.lolketing.repository.LoginRepository
import com.ezen.lolketing.util.Code
import com.ezen.lolketing.util.Constants
import com.ezen.lolketing.util.getCouponValidityPeriod
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JoinDetailViewModel @Inject constructor(
    private val repository: LoginRepository,
    private val pref : SharedPreferences
) : BaseViewModel<JoinDetailViewModel.Event>() {

    var isModify = false
    private val _user = MutableLiveData<Users>()
    val user : LiveData<Users>
        get() = _user
    var email = ""

    init {
        pref.getString(Constants.ID, null)?.let {
            email = it
        } ?: event(Event.UserInfoLoadError)
    }

    /**
     * 유저 정보 조회
     * 신규 회원 가입 : 이메일 정보 수동 셋팅
     * 정보 수정 : 서버 조회
     * **/
    fun getUserInfo(isModify: Boolean) = viewModelScope.launch {
        if (isModify.not()) {
            // 신규 회원가입시
            _user.value = Users(id = email)
            return@launch
        }

        repository.getUserInfo(
            email = email,
            successListener = { info ->
                _user.value = Users(
                    id = email,
                    nickname = info.nickname,
                    phone = info.phone,
                    address = info.address
                )
            },
            failureListener = {
                event(Event.UserInfoLoadError)
            }
        )
    }

    /** 유저 정보 수정 **/
    fun updateModifyUserInfo() = viewModelScope.launch {
        event(Event.Loading)
        _user.value?.let {
            repository.updateUserInfo(
                user = it,
                successListener = {
                    event(Event.UpdateSuccess)
                },
                failureListener = {
                    event(Event.UpdateFailure)
                }
            )
        } ?: event(Event.UpdateFailure)
    }

    /** 신규 회원가입 **/
    fun updateNewUserInfo() = viewModelScope.launch {
        event(Event.Loading)
        _user.value?.let {
            repository.updateUserInfo(
                user = it,
                successListener = {
                    setNewUserCoupon()
                },
                failureListener = {
                    event(Event.JoinDetailFailure)
                }
            )
        } ?: kotlin.run {
            event(Event.JoinDetailFailure)
        }
    }

    /** 신규 회원 쿠폰 지급 **/
    private fun setNewUserCoupon() = viewModelScope.launch {
        // 신규 가입 쿠폰 지급
        val coupon = Coupon().also {
            it.id = email
            it.title = Code.NEW_USER_COUPON.code
            it.use = Code.NOT_USE.code
            it.limit = getCouponValidityPeriod()
            it.point = 200
            it.timestamp = System.currentTimeMillis()
        }
        repository.setNewUserCoupon(
            coupon = coupon,
            successListener = {
                event(Event.CouponIssuanceSuccess)
            },
            failureListener = {
                event(Event.CouponIssuanceFailure)
            }
        )
    }

    sealed class Event {
        data class Error(val msg: String) : Event()
        object Loading: Event()
        object UserInfoLoadError : Event()
        object UpdateSuccess : Event()
        object UpdateFailure : Event()
        object JoinDetailFailure: Event()
        object CouponIssuanceSuccess: Event()
        object CouponIssuanceFailure: Event()
    }

}