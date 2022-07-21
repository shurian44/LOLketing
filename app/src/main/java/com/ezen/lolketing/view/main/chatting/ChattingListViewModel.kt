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
) : BaseViewModel<ChattingListViewModel.Event>() {

    var nickName: String?= null

    /** 유저 닉네임 조회 **/
    fun getUserNickName() = viewModelScope.launch {
        repository.getUserNickName()?.let {
            nickName = it
        } ?: event(Event.UserNickNameError)
    }

    /** 경기 정보 조회 **/
    fun getGameData(startDate: String = "2022.01.01") = viewModelScope.launch {
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

    sealed class Event {
        data class GameData(val list: List<ChattingInfo>) : Event()
        object UserNickNameError : Event()
        object Error : Event()
    }

}