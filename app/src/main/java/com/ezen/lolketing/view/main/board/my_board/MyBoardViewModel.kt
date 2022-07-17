package com.ezen.lolketing.view.main.board.my_board

import android.content.SharedPreferences
import androidx.lifecycle.viewModelScope
import com.ezen.lolketing.BaseViewModel
import com.ezen.lolketing.model.BoardItem
import com.ezen.lolketing.repository.BoardRepository
import com.ezen.lolketing.util.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyBoardViewModel @Inject constructor(
    private val repository: BoardRepository,
    private val pref : SharedPreferences
) : BaseViewModel<MyBoardViewModel.Event>() {

    fun getBoardList() = viewModelScope.launch {

        val email = pref.getString(Constants.ID, "") ?: ""

        if (email.isEmpty()) {
            event(Event.Error)
            return@launch
        }

        repository
            .getBoardList(
                field = "email",
                query = email,
                successListener = {
                    event(Event.Success(it))
                },
                failureListener = {
                    event(Event.Error)
                }
            )
    }

    sealed class Event {
        data class Success(
            val list: List<BoardItem.BoardListItem>
        ): Event()
        object Error: Event()
    }

}