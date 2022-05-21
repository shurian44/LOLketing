package com.ezen.lolketing.view.login.join

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ezen.lolketing.BaseViewModel
import com.ezen.lolketing.model.Users
import com.ezen.lolketing.repository.LoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JoinDetailViewModel @Inject constructor(
    private val repository: LoginRepository
) : BaseViewModel<JoinDetailViewModel.Event>() {

    private val _user = MutableLiveData<Users>()
    val user : LiveData<Users>
        get() = _user
    private var email = ""

    fun getUserId() {
        repository.getCurrentUser()?.email?.let {
            email = it
        } ?: event(Event.UserInfoLoadError)
    }

    fun getUserInfo() = viewModelScope.launch {
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

    fun updateModifyUserInfo() = viewModelScope.launch {
        _user.value?.let {
            repository.updateModifyUserInfo(
                user = it,
                successListener = {
                    event(Event.UpdateSuccess)
                },
                failureListener = {
                    event(Event.UpdateFailure)
                }
            )
        }
    }

    fun setNewUserCoupon() = viewModelScope.launch {

    }

    sealed class Event {
        data class Error(val msg: String) : Event()
        object UserInfoLoadError : Event()
        object UpdateSuccess : Event()
        object UpdateFailure : Event()
        data class UserInfo(
            var nickname : String ?= null,
            var phone : String ?= null,
            var address : String ?= null
        )
    }

}