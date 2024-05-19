package com.ezen.lolketing.view.main.event

import androidx.lifecycle.viewModelScope
import com.ezen.lolketing.StatusViewModel
import com.ezen.network.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class EventViewModel @Inject constructor(
    private val repository: MainRepository
) : StatusViewModel() {

    private val _uiStatus = MutableStateFlow<EventUIStatus>(EventUIStatus.Init)
    val uiStatus: StateFlow<EventUIStatus> = _uiStatus

    private var isIssued = false

    fun fetchNewUserCoupon() {
        repository
            .fetchNewUserCoupon()
            .setLoadingState()
            .onEach { isIssued = it }
            .catch { updateMessage(it.message ?: "오류가 발생하였습니다.") }
            .launchIn(viewModelScope)
    }

    fun insertNewUserCoupon() {
        if (isIssued) {
            _uiStatus.value = EventUIStatus.IssuedCoupon
            return
        }

        repository
            .insertNewUserCoupon()
            .setLoadingState()
            .onEach {
                _uiStatus.value = EventUIStatus.Success
                isIssued = true
            }
            .catch { updateMessage(it.message ?: "오류가 발생하였습니다.") }
            .launchIn(viewModelScope)
    }

    fun updateInitStatus() {
        _uiStatus.value = EventUIStatus.Init
    }
}

sealed class EventUIStatus {
    object Init: EventUIStatus()

    object IssuedCoupon: EventUIStatus()

    object Success: EventUIStatus()
}