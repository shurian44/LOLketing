package com.ezen.lolketing.view.main.news

import androidx.lifecycle.viewModelScope
import com.ezen.lolketing.BaseViewModel
import com.ezen.lolketing.model.NewsContents
import com.ezen.lolketing.repository.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val repository: NewsRepository
) : BaseViewModel<NewsViewModel.Event>() {

    private var start = 1
    private val display = 20
    private var isMore = true

    fun getNews(

    ) = viewModelScope.launch {
        if (isMore.not()) return@launch

        repository.getNews(
            query = "LCK",
            display = display,
            start = start,
            sort = SIMILAR,
            successListener = { result ->
                if(result.total > start) {
                    isMore = true
                    start += display
                }
                event(Event.Success(result.mapper()))
            },
            failureListener = {
                event(Event.Failure)
            }
        )

    }

    sealed class Event {
        data class Success(val result: List<NewsContents>): Event()
        object Failure : Event()
    }

    companion object {
        const val SIMILAR = "sim"
        const val DATE = "date"
    }

}