package com.ezen.lolketing.view.login

import androidx.lifecycle.viewModelScope
import com.ezen.lolketing.BaseViewModel
import com.ezen.lolketing.repository.LoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: LoginRepository
) : BaseViewModel<LoginViewModel.Event>() {

    fun registerUser(email: String) = viewModelScope.launch {
        repository.registerUser(
            email= email,
            successListener = {
                event(Event.RegisterSuccess)
            },
            failureListener = {
                event(Event.RegisterFailure)
            }
        )
    }

    fun getUserInfo(
        email: String,
    ) = viewModelScope.launch {
        repository.getUserInfo(
            email= email,
            successListener = {
                event(Event.UserInfoSuccess)
            },
            failureListener = {
                event(Event.UserInfoFailure(it))
            }
        )
    }

    fun deleteUser() = viewModelScope.launch {
        repository.deleteUser {  }
    }

    sealed class Event {
        object UserInfoSuccess : Event()
        data class UserInfoFailure(
            val email : String
        ) : Event()
        object RegisterSuccess : Event()
        object RegisterFailure : Event()
    }

}