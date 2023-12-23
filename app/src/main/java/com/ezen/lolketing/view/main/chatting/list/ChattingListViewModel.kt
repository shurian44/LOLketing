package com.ezen.lolketing.view.main.chatting.list

import androidx.lifecycle.viewModelScope
import com.ezen.lolketing.StatusViewModel
import com.ezen.lolketing.model.ChattingInfo
import com.ezen.lolketing.repository.ChattingRepository
import com.ezen.lolketing.util.formatDate
import com.ezen.lolketing.util.getNextDay
import com.ezen.lolketing.util.getPreviousDay
import com.ezen.lolketing.util.getToday
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class ChattingListViewModel @Inject constructor(
    private val repository: ChattingRepository
) : StatusViewModel() {

    private val _list = MutableStateFlow(listOf<ChattingInfo>())
    val list: StateFlow<List<ChattingInfo>> = _list

    private val _date = MutableStateFlow(getToday())
    val date: StateFlow<String> = _date

    val year
        get() = runCatching { _date.value.substring(0, 4).toInt() }.getOrElse { 2023 }
    val month
        get() = runCatching { _date.value.substring(5, 7).toInt() }.getOrElse { 1 }
    val day
        get() = runCatching { _date.value.substring(8, 10).toInt() }.getOrElse { 1 }

    init {
        getGameData()
    }

    fun setDate(year: Int, month: Int, dayOfMonth: Int) {
        _date.value = formatDate(year, month, dayOfMonth)
        getGameData()
    }

    /** 이전 날짜 **/
    fun previousDay() {
        _date.value = getPreviousDay(_date.value)
        getGameData()
    }

    /** 다음 날짜 **/
    fun nextDay() {
        _date.value = getNextDay(_date.value)
        getGameData()
    }

    /** 경기 정보 조회 **/
    private fun getGameData() {
        repository
            .fetchGameDate(startDate = _date.value)
            .setLoadingState()
            .onEach { _list.value = it }
            .catch { updateMessage(it.message ?: "오류 발생") }
            .launchIn(viewModelScope)
    }

}