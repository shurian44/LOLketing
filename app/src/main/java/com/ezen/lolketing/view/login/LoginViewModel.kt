package com.ezen.lolketing.view.login

import android.content.SharedPreferences
import androidx.lifecycle.viewModelScope
import com.ezen.lolketing.BaseViewModel
import com.ezen.lolketing.repository.LoginRepository
import com.ezen.lolketing.util.Constants
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: LoginRepository,
    private val auth: FirebaseAuth,
    private val pref: SharedPreferences
) : BaseViewModel<LoginViewModel.Event>() {

    /** 자동 로그인 : 로그인 상태일 시 바로 메인 페이지로 이동 **/
    fun autoLogin() {
        val id = pref.getString(Constants.ID, "")

        if (id.isNullOrEmpty()) {
            event(Event.AutoLoginFailure)
        } else {
            event(Event.AutoLoginSuccess)
        }
    }

    fun emailLogin(
        id: String,
        pw: String,
        failureMsg: String
    ) = viewModelScope.launch {
        auth.signInWithEmailAndPassword(id, pw)
            .addOnSuccessListener {
                editPrefId(id)
                event(Event.LoginSuccess)
            }
            .addOnFailureListener {
                it.printStackTrace()
                event(Event.LoginFailure(failureMsg))
            }
            .await()
    }

    fun googleLogin(
        credential: AuthCredential,
        failureMsg: String
    ) = viewModelScope.launch {
        auth.signInWithCredential(credential)
            .addOnSuccessListener {
                val email = auth.currentUser?.email ?: kotlin.run {
                    event(Event.LoginFailure(failureMsg))
                    return@addOnSuccessListener
                }
                getUserInfo(email)
            }
            .addOnFailureListener {
                event(Event.LoginFailure(failureMsg))
            }
            .await()
    }

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
                editPrefId(email)
                event(Event.RegisterSuccess)
            },
            failureListener = {
                event(Event.RegisterFailure)
            }
        )
    }

    /** 유저 정보 조회 **/
    private fun getUserInfo(
        email: String,
    ) = viewModelScope.launch {
        repository.getUserInfo(
            email= email,
            successListener = {
                editPrefId(email)
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

    private fun editPrefId(id: String) {
        pref.edit().putString(Constants.ID, id).apply()
    }

    sealed class Event {
        object AutoLoginSuccess : Event()
        object AutoLoginFailure : Event()
        object LoginSuccess : Event()
        data class LoginFailure(
            val msg : String
        ) : Event()
        object UserInfoSuccess : Event()
        data class UserInfoFailure(
            val email : String
        ) : Event()
        object RegisterSuccess : Event()
        object RegisterFailure : Event()
    }

}