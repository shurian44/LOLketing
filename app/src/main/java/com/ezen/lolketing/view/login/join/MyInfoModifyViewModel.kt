package com.ezen.lolketing.view.login.join

import androidx.lifecycle.viewModelScope
import com.ezen.lolketing.StatusViewModel
import com.ezen.network.model.ModifyInfo
import com.ezen.network.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

// 제거 예정
@HiltViewModel
class MyInfoModifyViewModel @Inject constructor(
    private val repository: MainRepository
) : StatusViewModel() {

    private val _info = MutableStateFlow(ModifyInfo.init())
    val info: StateFlow<ModifyInfo> = _info

    init {
        fetchMyInfo()
    }

    /**
     * 유저 정보 조회
     * **/
    private fun fetchMyInfo() {
        repository
            .fetchMyInfo()
            .setLoadingState()
            .onEach { _info.value = it.toModifyInfo() }
            .catch {
                updateMessage(it.message ?: "회원 정보 호출 중 오류가 발생하였습니다.")
                updateFinish()
            }
            .launchIn(viewModelScope)
    }

    fun updateEditValue(type: String, value: String) {
        _info.value = when (type) {
            ModifyInfo.TypeNickname -> {
                _info.value.copy(nickname = value)
            }
            ModifyInfo.TypeMobile -> {
                _info.value.copy(mobile = value)
            }
            else -> {
                _info.value.copy(address = value)
            }
        }
    }

    fun updateUserInfo() {
        repository
            .updateMyInfo(_info.value)
            .setLoadingState()
            .onEach {
                updateMessage("수정 완료")
                updateFinish()
            }
            .catch { updateMessage(it.message ?: "수정 실패") }
            .launchIn(viewModelScope)
    }

}