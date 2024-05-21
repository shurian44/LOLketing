package com.ezen.lolketing.view.main.chatting

import androidx.lifecycle.viewModelScope
import com.ezen.lolketing.StatusViewModel
import com.ezen.network.model.ChattingItem
import com.ezen.network.model.ChattingRoomInfo
import com.ezen.network.repository.ChattingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ChattingViewModel @Inject constructor(
    private val repository: ChattingRepository
) : StatusViewModel() {

    private var selectTeam = ""

    private val _info = MutableStateFlow(Chatting())
    val info: StateFlow<Chatting> = _info

    fun setInfo(info: ChattingRoomInfo?, selectTeam: String?) {
        if (info == null || selectTeam == null) {
            updateMessage("채팅 조회 실패")
            updateFinish()
            return
        }

        _info.update { it.copy(roomInfo = info) }
        this.selectTeam = selectTeam
        fetchChattingList()
    }

    private fun fetchChattingList() {
        repository
            .observeChatUpdates(info.value.roomInfo)
            .onEach { newList ->
                _info.update { _info.value.updateList(newList) }
            }
            .catch { updateMessage(it.message ?: "채팅 조회 실패") }
            .launchIn(viewModelScope)
    }

    fun addChat() {
        repository
            .addChat(
                message = _info.value.message,
                selectedTeam = selectTeam,
                info = _info.value.roomInfo
            )
            .onEach {
                _info.update { it.copy(message = "") }
            }
            .catch { updateMessage(it.message ?: "채팅 실패") }
            .launchIn(viewModelScope)
    }
}

data class Chatting(
    val roomInfo: ChattingRoomInfo = ChattingRoomInfo.init(),
    val list: List<ChattingItem> = listOf(),
    var message: String = ""
) {
    fun updateList(newList: List<ChattingItem>) = this.copy(
        list = newList
    )
}