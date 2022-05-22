package com.ezen.lolketing.view.login.join

import androidx.lifecycle.viewModelScope
import com.ezen.lolketing.BaseViewModel
import com.ezen.lolketing.repository.LoginRepository
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JoinViewModel @Inject constructor(
    private val repository: LoginRepository,
    private val auth : FirebaseAuth
) : BaseViewModel<JoinViewModel.Event>() {

    /** 이메일 회원가입 **/
    fun joinUser(
        email: String,
        pw: String
    ) = viewModelScope.launch {
        repository.joinUser(
            email= email,
            pw= pw,
            successListener = {
                if (it == null) {
                    auth.signOut()
                    error(JOIN_FAILURE)
                } else {
                    registerUser(email, it)
                }
            },
            failureListener = {
                error(ALREADY_JOIN)
            }
        )
    }

    /** 유저 등록 **/
    private fun registerUser(
        email: String,
        uid: String
    ) = viewModelScope.launch {
        repository.registerUser(
            email = email,
            uid = uid,
            successListener = {
                event(Event.Success)
            },
            failureListener = {
                deleteUser()
                error(JOIN_FAILURE)
            }
        )
    }

    /** 유저 삭제 **/
    private fun deleteUser() = viewModelScope.launch {
        repository.deleteUser {
            auth.signOut()
        }
    }

    private fun error(msg: Int) {
        event(Event.Error(msg))
    }

    sealed class Event {
        object Success: Event()
        data class Error(val code: Int): Event()
    }

    companion object {
        const val JOIN_FAILURE = 1
        const val ALREADY_JOIN = 2
    }

}