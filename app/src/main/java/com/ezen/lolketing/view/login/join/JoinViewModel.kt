package com.ezen.lolketing.view.login.join

import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.lifecycle.viewModelScope
import com.ezen.lolketing.BaseViewModel
import com.ezen.lolketing.repository.LoginRepository
import com.ezen.lolketing.util.Constants
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JoinViewModel @Inject constructor(
    private val repository: LoginRepository,
    private val auth : FirebaseAuth,
    private val pref : SharedPreferences
) : BaseViewModel<JoinViewModel.Event>() {

    /** 이메일 회원가입 **/
    fun joinUser(
        email: String,
        pw: String
    ) = viewModelScope.launch {
        event(Event.Loading)
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
        event(Event.Loading)
        repository.registerUser(
            email = email,
            uid = uid,
            successListener = {
                pref.edit { putString(Constants.ID, email) }
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
        object Loading: Event()
        object Success: Event()
        data class Error(val code: Int): Event()
    }

    companion object {
        const val JOIN_FAILURE = 1
        const val ALREADY_JOIN = 2
    }

}