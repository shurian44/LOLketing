package com.ezen.lolketing.view.login

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.ezen.auth.model.LoginInfo
import com.ezen.auth.repository.AuthRepository
import com.ezen.lolketing.StatusViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: AuthRepository,
) : StatusViewModel() {

    val loginInfo = MutableStateFlow(LoginInfo("", ""))

    fun emailLogin() {
        repository
            .emailLogin(loginInfo.value)
            .setLoadingState()
            .onEach { updateFinish() }
            .catch { updateMessage(it.message ?: "로그인 실패") }
            .launchIn(viewModelScope)
    }

    fun naverLogin(context: Context) {
        repository
            .naverLogin(context)
            .onEach { updateFinish() }
            .catch {
                it.printStackTrace()
                updateMessage(it.message ?: "로그인 실패")
            }
            .launchIn(viewModelScope)
    }

    fun kakaoLogin(context: Context) {
        repository
            .kakaoLogin(context)
            .onEach { updateFinish() }
            .catch {
                it.printStackTrace()
                updateMessage(it.message ?: "로그인 실패")
            }
            .launchIn(viewModelScope)
    }
}