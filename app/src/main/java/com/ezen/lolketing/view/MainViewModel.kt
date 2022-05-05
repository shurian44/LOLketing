package com.ezen.lolketing.view

import androidx.lifecycle.viewModelScope
import com.ezen.lolketing.BaseViewModel
import com.ezen.lolketing.R
import com.ezen.lolketing.repository.MainRepository
import com.ezen.lolketing.util.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: MainRepository
) : BaseViewModel<MainViewModel.MainEvent>() {

    fun getUserInfo() = viewModelScope.launch {
        val email = repository.getCurrentUser()?.email ?: return@launch
        val userInfo = repository.getUserInfo(Constants.USERS, email)

        if (userInfo == null) {
            event(MainEvent.Error("유저 정보 조회 중 오류가 발생하였습니다."))
            return@launch
        }

        event(
            MainEvent.CheckDetailJoin(
                isNotJoinComplete = userInfo.nickname.isNullOrBlank(),
                isMaster = userInfo.grade ?: "" == "마스터"
            )
        )
    }

    fun getEventBannerList() {
        val images = listOf(R.drawable.banner1, R.drawable.banner2, R.drawable.banner3)

        event(MainEvent.EventBannerList(images))

    }

    override fun event(event: MainEvent) = viewModelScope.launch {
        _eventFlow.emit(event)
    }

    sealed class MainEvent {
        data class CheckDetailJoin(val isNotJoinComplete : Boolean, val isMaster : Boolean) : MainEvent()
        data class EventBannerList(val list: List<Int>) : MainEvent()
        data class Error(val msg: String) : MainEvent()
    }

}
