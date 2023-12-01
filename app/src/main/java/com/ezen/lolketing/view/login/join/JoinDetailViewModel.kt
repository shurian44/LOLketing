package com.ezen.lolketing.view.login.join

import androidx.lifecycle.viewModelScope
import com.ezen.lolketing.StatusViewModel
import com.ezen.lolketing.model.Users
import com.ezen.lolketing.repository.LoginRepository
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
class JoinDetailViewModel @Inject constructor(
    private val repository: LoginRepository
) : StatusViewModel() {

    private val _isModify = MutableStateFlow(false)
    val isModify: StateFlow<Boolean> = _isModify

    private val _info = MutableStateFlow(Users())
    val info: StateFlow<Users> = _info

    private val _isTrimVisible = MutableStateFlow(true)
    val isTrimVisible: StateFlow<Boolean> = _isTrimVisible

    private val _isComplete = MutableStateFlow(false)
    val isComplete: StateFlow<Boolean> = _isComplete

    fun agreeToTermsOfUse() {
        _isTrimVisible.value = false
    }

    fun setIsModify(isModify: Boolean) {
        _isModify.value = isModify
        if (isModify) {
            agreeToTermsOfUse()
            fetchUserInfo()
        }
    }

    /**
     * 유저 정보 조회
     * **/
    private fun fetchUserInfo() {
        repository
            .fetchUserInfo()
            .setLoadingState()
            .onEach { _info.value = it }
            .catch { updateMessage(it.message ?: "회원 정보 호출 중 오류가 발생하였습니다.") }
            .launchIn(viewModelScope)
    }

    fun setAddress(address: String) {
        _info.update { it.copy(address = address) }
    }

    fun updateUserInfo() {
        repository
            .updateUserInfo(_info.value)
            .setLoadingState()
            .onEach {
                if (_isModify.value) {
                    updateMessage("회원 정보 수정을 완료하였습니다.")
                    _isComplete.value = true
                } else {
                    setNewUserCoupon()
                }
            }
            .catch { updateMessage(it.message ?: "정보 등록을 실패하였습니다.") }
            .launchIn(viewModelScope)
    }

    /** 신규 회원 쿠폰 지급 **/
    private fun setNewUserCoupon() = viewModelScope.launch {
        repository
            .setNewUserCoupon()
            .setLoadingState()
            .onEach {
                updateMessage("회원 정보를 등록하였습니다.")
                _isComplete.value = true
            }
            .catch {
                updateMessage("회원 정보를 등록하였습니다. 신규 회원 쿠폰 발급 문의를 해주세요")
                _isComplete.value = true
            }
            .launchIn(viewModelScope)
    }

}