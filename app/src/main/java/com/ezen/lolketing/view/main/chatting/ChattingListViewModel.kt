package com.ezen.lolketing.view.main.chatting

import androidx.lifecycle.viewModelScope
import com.ezen.lolketing.BaseViewModel
import com.ezen.lolketing.model.ChattingInfo
import com.ezen.lolketing.repository.ChattingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChattingListViewModel @Inject constructor(
    private val repository: ChattingRepository
): BaseViewModel<ChattingListViewModel.Event>() {

    fun getUserNickName() = viewModelScope.launch {
        repository.getUserNickName()?.let {
            event(Event.UserNickName(it))
        } ?: error("유저 정보 조회에 실패하였습니다")
    }

    fun getGameData(startDate: String = "2020.02.05") = viewModelScope.launch {
        repository.getGameData(
            startDate = startDate,
            successListener = { list ->
                event(Event.GameData(list))
            },
            failureListener = {
                error("경기 정보 조회를 실패하였습니다.")
            }
        )
    }

    private fun error(msg: String) {
        event(Event.Error(msg))
    }

    sealed class Event {
        data class UserNickName(val nickName: String) : Event()
        data class GameData(val list: List<ChattingInfo>) : Event()
        data class Error(val msg: String) : Event()
    }

}