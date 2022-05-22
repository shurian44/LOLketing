package com.ezen.lolketing.view.login

import androidx.lifecycle.viewModelScope
import com.ezen.lolketing.BaseViewModel
import com.ezen.lolketing.repository.LoginRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: LoginRepository,
    private val auth: FirebaseAuth
) : BaseViewModel<LoginViewModel.Event>() {

    /** 유저 등록 **/
    fun registerUser(email: String) = viewModelScope.launch {
        val uid = auth.uid
        if (uid == null) {
            event(Event.RegisterFailure)
            return@launch
        }

        repository.registerUser(
            email= email,
            uid = uid,
            successListener = {
                event(Event.RegisterSuccess)
            },
            failureListener = {
                event(Event.RegisterFailure)
            }
        )
    }

    /** 유저 정보 조회 **/
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

    /** 유저 삭제 **/
    fun deleteUser() = viewModelScope.launch {
        repository.deleteUser {
            auth.signOut()
        }
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