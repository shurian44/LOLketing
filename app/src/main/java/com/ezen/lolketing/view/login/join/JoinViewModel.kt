package com.ezen.lolketing.view.login.join

import androidx.lifecycle.viewModelScope
import com.ezen.auth.model.JoinInfo
import com.ezen.auth.repository.AuthRepository
import com.ezen.lolketing.StatusViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class JoinViewModel @Inject constructor(
    private val repository: AuthRepository
) : StatusViewModel() {

    private val _info = MutableStateFlow(JoinInfo.create())
    val info: StateFlow<JoinInfo> = _info

    @OptIn(ExperimentalCoroutinesApi::class)
    val errorMessage: StateFlow<String> = _info.flatMapLatest {
        MutableStateFlow(it.checkValidation())
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(500L),
        initialValue = ""
    )

    fun setType(type: String) {
        _info.value = _info.value.copy(type = type)
    }

    fun updateEditValue(type: String, value: String) {
        _info.value = when (type) {
            JoinInfo.TypeId -> {
                _info.value.copy(id = value)
            }

            JoinInfo.TypePassword -> {
                _info.value.copy(password = value)
            }

            JoinInfo.TypePasswordCheck -> {
                _info.value.copy(passwordCheck = value)
            }

            JoinInfo.TypeNickname -> {
                _info.value.copy(nickname = value)
            }

            JoinInfo.TypeMobile -> {
                _info.value.copy(mobile = value)
            }

            else -> {
                _info.value.copy(address = value)
            }
        }
    }

    /** 이메일 회원가입 **/
    fun joinUser() {
        repository
            .join(_info.value)
            .setLoadingState()
            .onEach {
                updateMessage(it)
                updateFinish()
            }
            .catch { updateMessage(it.message ?: "회원가입 실패") }
            .launchIn(viewModelScope)
    }

}