package com.ezen.lolketing.view.login.join

import androidx.lifecycle.viewModelScope
import com.ezen.lolketing.StatusViewModel
import com.ezen.lolketing.model.JoinInfo
import com.ezen.lolketing.repository.LoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class JoinViewModel @Inject constructor(
    private val repository: LoginRepository
) : StatusViewModel() {

    private val _info = MutableStateFlow(JoinInfo())
    val info: StateFlow<JoinInfo> = _info

    /** 이메일 회원가입 **/
    fun joinUser() {
        repository
            .joinUser(_info.value)
            .setLoadingState()
            .onEach {
                updateMessage(it)
                updateFinish(true)
            }
            .catch { updateMessage(it.message ?: "회원가입 실패") }
            .launchIn(viewModelScope)
    }

}