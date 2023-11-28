package com.ezen.lolketing.view.login

import androidx.lifecycle.viewModelScope
import com.ezen.lolketing.StatusViewModel
import com.ezen.lolketing.model.LoginInfo
import com.ezen.lolketing.repository.LoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: LoginRepository,
) : StatusViewModel() {

    private val _loginSuccess = MutableStateFlow(false)
    val loginSuccess: StateFlow<Boolean> = _loginSuccess

    private val _loginInfo = MutableStateFlow(LoginInfo())
    val loginInfo: StateFlow<LoginInfo> = _loginInfo

    init {
        fetchInitLoginInfo()
    }

    private fun fetchInitLoginInfo() {
        repository.fetchLoginInfo()?.let { _loginSuccess.value = true }
    }

    fun emailLogin() = viewModelScope.launch {
        repository
            .emailLogin(_loginInfo.value)
            .onSuccess { _loginSuccess.value = true }
            .onFailure {
                it.printStackTrace()
                updateMessage("아이디 또는 패스워드를 확인해 주세요.")
            }
    }
}