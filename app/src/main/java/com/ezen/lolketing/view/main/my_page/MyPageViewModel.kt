package com.ezen.lolketing.view.main.my_page

import androidx.lifecycle.viewModelScope
import com.ezen.lolketing.StatusViewModel
import com.ezen.lolketing.model.MyPageInfo
import com.ezen.lolketing.repository.MyPageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val repository: MyPageRepository
) : StatusViewModel() {

    private val _myPageInfo = MutableStateFlow(MyPageInfo.create())
    val myPageInfo: StateFlow<MyPageInfo> = _myPageInfo

    private val _goToLogin = MutableStateFlow(false)
    val goToLogin: StateFlow<Boolean> = _goToLogin

    /** 유저 정보 조회 **/
    fun fetchUserInfo()  {
        repository
            .fetchUserInfo()
            .setLoadingState()
            .onEach { _myPageInfo.value = it }
            .catch { updateMessage(it.message ?: "오류가 발생하였습니다.") }
            .launchIn(viewModelScope)
    }

    /** 로그아웃 **/
    fun logout() {
        repository.signOut()
        _goToLogin.value = true
    }

    /** 회원탈퇴 **/
    fun deleteUser() = viewModelScope.launch {
        repository.deleteUser()
        updateMessage("탈퇴가 완료되었습니다.")
        _goToLogin.value = true
    }

}