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
    private var _isMore = true
    val isMore : Boolean
        get() = _isMore

    /** 뉴스 정보 조회 **/
    fun getNews() = viewModelScope.launch {
        if (_isMore.not()) return@launch

        repository.getNews(
            query = "LCK",
            display = display,
            start = start,
            sort = SIMILAR,
            successListener = { list, total ->
                if(total > start) {
                    _isMore = true
                    start += display
                }
                event(Event.Success(list))
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
    }

}