package com.ezen.lolketing.view.login.find

import androidx.lifecycle.viewModelScope
import com.ezen.lolketing.StatusViewModel
import com.ezen.lolketing.repository.FindRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class FindPasswordViewModel @Inject constructor(
    private val repository: FindRepository
) : StatusViewModel(){

    val email = MutableStateFlow("")

    fun findPassword() {
        repository
            .findPassword(email.value)
            .setLoadingState()
            .onEach { updateMessage(it) }
            .catch { updateMessage(it.message ?: "비밀번호 찾기를 실패하였습니다.") }
            .launchIn(viewModelScope)
    }

}