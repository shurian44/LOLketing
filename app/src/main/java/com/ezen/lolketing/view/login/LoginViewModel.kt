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
            event(Event.LoginSuccess)
        }
    }

    /** 이메일 로그인 **/
    fun emailLogin(
        id: String,
        pw: String,
    ) = viewModelScope.launch {
        try {
            event(Event.Loading)
            auth.signInWithEmailAndPassword(id, pw)
                .addOnSuccessListener {
                    editPrefId(id)
                    event(Event.LoginSuccess)
                }
                .addOnFailureListener {
                    it.printStackTrace()
                    event(Event.LoginFailure)
                }
                .await()
        } catch (e: Exception) {
            event(Event.LoginFailure)
            e.printStackTrace()
        }

    }

    /** 구글 로그인 **/
    fun googleLogin(
        credential: AuthCredential,
    ) = viewModelScope.launch {
        event(Event.Loading)
        auth.signInWithCredential(credential)
            .addOnSuccessListener {
                val email = auth.currentUser?.email ?: kotlin.run {
                    event(Event.GoogleLoginFailure)
                    return@addOnSuccessListener
                }
                getUserInfo(email)
            }
            .addOnFailureListener {
                event(Event.GoogleLoginFailure)
            }
            .await()
    }

    /** 유저 등록 **/
    private fun registerUser(email: String) = viewModelScope.launch {
        event(Event.Loading)
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
                event(Event.LoginSuccess)
            },
            failureListener = {
                deleteUser()
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
                event(Event.LoginSuccess)
            },
            failureListener = {
                // 구글 로그인 했을 때 유저 정보가 없으면 신규 가입으로 보고 가입 절차 진행
                registerUser(it)
            }
        )
    }

    /** 유저 삭제 **/
    private fun deleteUser() = viewModelScope.launch {
        repository.deleteUser {
            auth.signOut()
            event(Event.RegisterFailure)
        }
    }

    /** 유저 이메일 등록 **/
    private fun editPrefId(id: String) {
        pref.edit().putString(Constants.ID, id).apply()
    }

    sealed class Event {
        object Loading: Event()
        object LoginSuccess : Event()
        object LoginFailure : Event()
        object GoogleLoginFailure : Event()
        object AutoLoginFailure : Event()
        object RegisterFailure : Event()
    }

}