package com.ezen.lolketing.view.main.event

import android.content.SharedPreferences
import androidx.lifecycle.viewModelScope
import com.ezen.lolketing.BaseViewModel
import com.ezen.lolketing.model.Coupon
import com.ezen.lolketing.repository.EventRepository
import com.ezen.lolketing.util.Constants
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import okhttp3.internal.wait
import javax.inject.Inject

@HiltViewModel
class EventDetailViewModel @Inject constructor(
    private val repository: EventRepository,
    private val pref: SharedPreferences
) : BaseViewModel<EventDetailViewModel.Event>() {

    fun getUserNickname() = viewModelScope.launch {
        repository.getUserNickname()?.let{
            event(Event.UserNickname(nickname = it))
        } ?: error("유저 정보를 가져오는데 실패하였습니다.")
    }

    fun getCouponList() = viewModelScope.launch {
        repository.getCouponList(
            successListener = {
                it?.let {
                    event(it)
                } ?: event(Event.NewUserCoupon(isUsed = true, documentId = ""))
            },
            failureListener = {
                error("쿠폰 조회에 실패하였습니다.")
            }
        )
    }

    fun updateCouponUsed(
        documentId: String
    ) = viewModelScope.launch {
        val email = pref.getString(Constants.ID, null) ?: kotlin.run {
            error("오류가 발생하였습니다. 잠시후 다시 이용바랍니다.")
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