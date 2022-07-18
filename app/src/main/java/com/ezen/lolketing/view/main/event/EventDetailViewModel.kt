package com.ezen.lolketing.view.main.event

import android.content.SharedPreferences
import androidx.lifecycle.viewModelScope
import com.ezen.lolketing.BaseViewModel
import com.ezen.lolketing.repository.EventRepository
import com.ezen.lolketing.util.Code
import com.ezen.lolketing.util.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventDetailViewModel @Inject constructor(
    private val repository: EventRepository,
    private val pref: SharedPreferences
) : BaseViewModel<EventDetailViewModel.Event>() {

    /** 유저 닉네임 조회 **/
    fun getUserNickname() = viewModelScope.launch {
        repository.getUserNickname()?.let{
            event(Event.UserNickname(nickname = it))
        } ?: error("유저 정보를 가져오는데 실패하였습니다.")
    }

    /** 신규 가입 쿠폰 정보 조회 **/
    fun getNewUserCouponInfo() = viewModelScope.launch {
        repository.getNewUserCouponInfo(
            successListener = { documentId, use ->
                if (use == Code.NOT_USE.code) {
                    event(Event.NewUserCoupon(isUsed = false, documentId = documentId))
                } else {
                    event(Event.NewUserCoupon(isUsed = true, documentId = documentId))
                }
            },
            failureListener = {
                error("쿠폰 조회에 실패하였습니다.")
            }
        )
    }

    /** 신규 가입 쿠폰 사용 및 포인트 적립 **/
    fun updateCouponUsed(
        documentId: String
    ) = viewModelScope.launch {
        val email = pref.getString(Constants.ID, null) ?: kotlin.run {
            error("유저 정보를 가져오는데 실패하였습니다.")
            return@launch
        }

        repository.updateCoupon(
            documentId = documentId,
            failureListener = {
                error("오류가 발생하였습니다. 잠시후 다시 이용바랍니다.")
                cancel()
            }
        )

        repository.updateUserPoint(
            documentId = email,
            failureListener = {
                error("오류가 발생하였습니다. 잠시후 다시 이용바랍니다.")
                cancel()
            }
        )
        event(Event.UpdateSuccess)
    }

    fun error(msg: String) {
        event(Event.Error(msg))
    }

    sealed class Event {
        data class UserNickname(val nickname: String) : Event()
        data class NewUserCoupon(
            val isUsed: Boolean = false,
            val documentId: String
        ) : Event()
        object UpdateSuccess : Event()
        data class Error(val msg: String) : Event()
    }

}