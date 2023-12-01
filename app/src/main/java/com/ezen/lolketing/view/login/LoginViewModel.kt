package com.ezen.lolketing.view.login

import androidx.lifecycle.viewModelScope
import com.ezen.lolketing.StatusViewModel
import com.ezen.lolketing.model.LoginInfo
import com.ezen.lolketing.model.Users
import com.ezen.lolketing.repository.LoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: LoginRepository,
) : StatusViewModel() {

    private val _userInfo = MutableStateFlow<Users?>(null)
    val userInfo: StateFlow<Users?> = _userInfo

    private val _loginInfo = MutableStateFlow(LoginInfo())
    val loginInfo: StateFlow<LoginInfo> = _loginInfo

    init {
        fetchInitLoginInfo()
    }

    private fun fetchInitLoginInfo() {
        repository
            .fetchLoginInfo()
            .onEach { _userInfo.value = it }
            .catch { _userInfo.value = null }
            .launchIn(viewModelScope)
    }

    fun emailLogin() {
        repository
            .emailLogin(_loginInfo.value)
            .setLoadingState()
            .onEach { _userInfo.value = it }
            .catch {
                it.printStackTrace()
                updateMessage("아이디 또는 패스워드를 확인해 주세요.")
            }
            .launchIn(viewModelScope)
    }

    fun logout() {
        repository.logout()
    }
}