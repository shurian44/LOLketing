package com.ezen.lolketing.view.main.event

import androidx.lifecycle.viewModelScope
import com.ezen.lolketing.StatusViewModel
import com.ezen.network.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RouletteViewModel @Inject constructor(
    private val repository: MainRepository
) : StatusViewModel() {

    private val _count = MutableStateFlow(0)
    val count: StateFlow<Int> = _count

    private val _uiStatus = MutableStateFlow<RouletteUiStatus>(RouletteUiStatus.Init)
    val uiStatus: StateFlow<RouletteUiStatus> = _uiStatus

    private var deg = 0f

    private val result
        get() = when (deg) {
            in 0f..45f -> 2000
            in 46f..90f -> 300
            in 91f..135f -> 350
            in 136f..180f -> 200
            in 181f..225f -> 1000
            in 226f..270f -> 250
            in 271f..315f -> 450
            else -> 250
        }

    init {
        fetchRouletteCount()
    }

    private fun fetchRouletteCount() {
        repository
            .fetchRouletteCount()
            .setLoadingState()
            .onEach {
                _count.value = it.count
            }
            .catch {
                updateMessage("오류가 발생하였습니다. 잠시 후 이용해주세요")
                updateFinish()
            }
            .launchIn(viewModelScope)
    }

    fun setDeg() {
        if (_uiStatus.value != RouletteUiStatus.Init) return

        deg = (1..359).random().toFloat()
        viewModelScope.launch {
            _uiStatus.value = RouletteUiStatus.RouletteStart(deg)
            delay(5000)
            _uiStatus.value = RouletteUiStatus.Init
        }
    }

    fun insertRouletteCoupon() = viewModelScope.launch {
        repository
            .insertRouletteCoupon(result)
            .onSuccess {
                _count.value = it.count
                updateMessage("${result}RP 쿠폰 당첨!!")
            }
            .onFailure { error ->
                updateMessage(error.message ?: "룰렛에 오류가 발생하였습니다.")
            }
    }

}

sealed class RouletteUiStatus {
    object Init: RouletteUiStatus()

    data class RouletteStart(val deg: Float): RouletteUiStatus()
}