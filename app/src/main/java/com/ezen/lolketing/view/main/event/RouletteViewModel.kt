package com.ezen.lolketing.view.main.event

import androidx.lifecycle.viewModelScope
import com.ezen.lolketing.StatusViewModel
import com.ezen.lolketing.repository.EventRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class RouletteViewModel @Inject constructor(
    private val repository: EventRepository
) : StatusViewModel() {

    private val _count = MutableStateFlow(0)
    val count: StateFlow<Int> = _count

    private val _isAnimationStart = MutableStateFlow(false)
    val isAnimationStart: StateFlow<Boolean> = _isAnimationStart

    init {
        getRouletteCount()
    }

    /** 룰렛 카운트 조회 **/
    private fun getRouletteCount() {
        repository
            .getRouletteCount()
            .setLoadingState()
            .onEach { _count.value = it }
            .catch { updateMessage(it.message ?: "쿠폰 조회에 실패하였습니다.") }
            .launchIn(viewModelScope)
    }

    /** 룰렛 돌리기 **/
    fun startRoulette() {
        if (_count.value <= 0) {
            updateMessage("기회를 다 소진하였습니다.")
            return
        }
        _isAnimationStart.value = true
    }

    fun couponIssuance(point: Int) {
        repository
            .couponIssuance(point)
            .onEach {
                updateMessage(it)
                _count.value = _count.value - 1
                _isAnimationStart.value = false
            }
            .catch { updateMessage(it.message ?: "발급 중 오류가 발생하였습니다.") }
            .launchIn(viewModelScope)
    }

}