package com.ezen.lolketing.view.main.my_page

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ezen.lolketing.model.CouponInfo
import com.ezen.lolketing.model.MyPageInfo
import com.ezen.lolketing.repository.MyPageRepository
import com.ezen.lolketing.util.Code
import com.ezen.lolketing.util.Constants
import com.ezen.lolketing.util.getSimpleDateFormatMillSec
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val repository: MyPageRepository,
    private val auth: FirebaseAuth,
    private val pref: SharedPreferences
) : ViewModel() {

    val userInfoState = MutableStateFlow<MyPageInfo?>(null)
    val couponInfoState = MutableStateFlow("0 / 0")
    val couponListState = MutableStateFlow<List<CouponInfo>>(emptyList())
    val deleteUserState = MutableStateFlow<Event>(Event.Init)
    val updateState = MutableStateFlow<Event>(Event.Init)

    fun getUserInfo() = viewModelScope.launch {
        repository.getUserInfo(
            successListener = {
                userInfoState.value = it
                getCouponInfo(id = it.id)
            },
            failureListener = {
                userInfoState.value = null
            }
        )
    }

    private fun getCouponInfo(id: String) = viewModelScope.launch {
        repository.getUserCoupon(
            id = id,
            successListener = {
                val notUseCount = it
                    .filter { info ->
                        info.use == Code.NOT_USE.code
                    }
                    .count { info ->
                        (getSimpleDateFormatMillSec(info.limit, "yyyy-MM-dd HH:mm")
                            ?: 0) >= System.currentTimeMillis()
                    }
                couponInfoState.value = "$notUseCount / ${it.size}"
            },
            failureListener = {
                couponInfoState.value = "0 / 0"
            }
        )
    }

    fun logout() {
        auth.signOut()
        pref.edit().putString(Constants.ID, null).apply()
    }

    fun deleteUser() = viewModelScope.launch {
        deleteUserState.value = Event.Loading

        repository.deleteUser(
            successListener = {
                deleteUserState.value = Event.Success
            },
            failureListener = {
                deleteUserState.value = Event.Failure
            }
        )
    }

    fun getCouponList() = viewModelScope.launch {
        repository.getCouponList(
            successListener = {
                couponListState.value = it
            },
            failureListener = {
                couponListState.value = emptyList()
            }
        )
    }

    fun updateCoupon(
        documentId: String,
        point: Int
    ) = viewModelScope.launch {
        updateState.value = Event.Loading

        repository.updateCoupon(
            documentId = documentId,
            point = point,
            successListener = {
                getCouponList()
                updateState.value = Event.Success
            },
            failureListener = {
                updateState.value = Event.Failure
            }
        )
    }

    sealed class Event {
        object Init : Event()
        object Loading : Event()
        object Success : Event()
        object Failure : Event()
    }

}