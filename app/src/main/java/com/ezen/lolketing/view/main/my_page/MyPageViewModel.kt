package com.ezen.lolketing.view.main.my_page

import androidx.lifecycle.viewModelScope
import com.ezen.auth.repository.AuthRepository
import com.ezen.lolketing.StatusViewModel
import com.ezen.network.model.MyInfo
import com.ezen.network.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val repository: MainRepository,
    private val authRepository: AuthRepository
) : StatusViewModel() {

    private val _info = MutableStateFlow(MyInfo.init())
    val info: StateFlow<MyInfo> = _info

    /** 유저 정보 조회 **/
    fun fetchUserInfo()  {
        repository
            .fetchMyInfo()
            .setLoadingState()
            .onEach { _info.value = it }
            .catch { updateMessage(it.message ?: "오류가 발생하였습니다.") }
            .launchIn(viewModelScope)
    }

    /** 로그아웃 **/
    fun logout() = viewModelScope.launch {
        runCatching {
            authRepository.logout()
            updateFinish()
        }
    }

    /** 회원탈퇴 **/
    fun withdrawal() = viewModelScope.launch {
        runCatching {
            authRepository.withdrawal()
            updateMessage("탈퇴가 완료되었습니다.")
            updateFinish()
        }
    }

    /** 캐시 충전 **/
    fun updateCashCharging(cash: Int) {
        repository
            .updateCashCharging(cash)
            .setLoadingState()
            .onEach { result ->
                _info.update {
                    it.copy(cash = result.cash)
                }
            }
            .catch { updateMessage(it.message ?: "캐시 충전 실패") }
            .launchIn(viewModelScope)
    }

}