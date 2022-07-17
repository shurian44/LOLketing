package com.ezen.lolketing.view.main.board.search

import androidx.lifecycle.viewModelScope
import com.ezen.lolketing.BaseViewModel
import com.ezen.lolketing.repository.BoardRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: BoardRepository
) : BaseViewModel<SearchViewModel.Event>() {

    fun getBoardList() = viewModelScope.launch {
        repository.getBoardList(
            queryList = listOf(),
            successListener = {

            },
            failureListener = {
                
            }
        )
    }

    sealed class Event {

    }

}