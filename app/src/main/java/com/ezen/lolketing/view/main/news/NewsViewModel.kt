package com.ezen.lolketing.view.main.news

import androidx.lifecycle.viewModelScope
import com.ezen.lolketing.StatusViewModel
import com.ezen.lolketing.model.NewsContents
import com.ezen.lolketing.repository.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val repository: NewsRepository
) : StatusViewModel() {

    private val _list = MutableStateFlow(listOf<NewsContents>())
    val list: StateFlow<List<NewsContents>> = _list

    private var start = 1
    private val display = 20
    private var _isMore = true
    val isMore : Boolean
        get() = _isMore

    init {
        fetchNews()
    }

    /** 뉴스 정보 조회 **/
    fun fetchNews() {
        if (_isMore.not()) return

        repository
            .fetchNews(
                display = display,
                start = start
            )
            .setLoadingState()
            .onEach {
                val temp = _list.value.toMutableList()
                temp.addAll(it.mapper())
                _list.value = temp

                if (it.total <= start * display || start * display >= 1000) {
                    _isMore = false
                } else {
                    start += display
                }
            }
            .catch { updateMessage(it.message ?: "뉴스 조회에 실패하였습니다.") }
            .launchIn(viewModelScope)
    }
}