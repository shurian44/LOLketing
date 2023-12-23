package com.ezen.lolketing.view.main.chatting

import androidx.lifecycle.viewModelScope
import com.ezen.lolketing.StatusViewModel
import com.ezen.lolketing.model.ChattingInfo
import com.ezen.lolketing.model.ChattingItem
import com.ezen.lolketing.repository.ChattingRepository
import com.ezen.lolketing.util.getToday
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

    private val _list = MutableStateFlow(listOf<ChattingItem>())
    val list: StateFlow<List<ChattingItem>> = _list

    private val _info = MutableStateFlow(ChattingInfo())
    val info: StateFlow<ChattingInfo> = _info

    private val _myChat = MutableStateFlow(ChattingItem())
    val myChat: StateFlow<ChattingItem> = _myChat

    private val today = getToday()

    fun setInfo(info: ChattingInfo) {
        _info.value = info
        fetchChattingList()
        fetchUserInfo()
    }

    private fun fetchChattingList() {
        repository
            .observeChatUpdates(_info.value.documentId)
            .onEach { _list.value = it }
            .catch { updateMessage(it.message ?: "오류 발생")
            it.printStackTrace()}
            .launchIn(viewModelScope)
    }

    private fun fetchUserInfo() {
        repository
            .fetchUserInfo()
            .setLoadingState()
            .onEach { (id, nickname) ->
                _myChat.update {
                    it.copy(
                        id = id,
                        nickname = nickname,
                        gameTime = _info.value.documentId,
                        team = _info.value.selectTeam
                    )
                }
            }
            .catch {
                updateMessage(it.message ?: "오류 발생")
                updateFinish(true)
            }
            .launchIn(viewModelScope)
    }

    fun onRegister() {
        val gameDate = _info.value.documentId.substring(0, 10)
        if (gameDate != today) {
            updateMessage("채팅은 당일에만 작성이 가능합니다")
            return
        }

        repository
            .addChat(_myChat.value)
            .onEach {
                _myChat.update { it.copy(message = "") }
            }
            .catch { updateMessage(it.message ?: "오류 발생") }
            .launchIn(viewModelScope)
    }
}