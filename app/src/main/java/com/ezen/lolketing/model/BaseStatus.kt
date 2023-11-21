package com.ezen.lolketing.model

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class BaseStatus {
    private val _message = MutableStateFlow("")
    val message: StateFlow<String> = _message

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _isNetworkError = MutableStateFlow(false)
    val isNetworkError: StateFlow<Boolean> = _isNetworkError

    private val _isFinish = MutableStateFlow(false)
    val isFinish: StateFlow<Boolean> = _isFinish

    fun updateMessage(message: String) {
        _message.value = message
    }

    fun startLoading() {
        _isLoading.value = true
    }

    fun endLoading() {
        _isLoading.value = false
    }

    fun updateNetworkErrorState(value: Boolean) {
        _isNetworkError.value = value
    }

    fun updateFinish(value: Boolean) {
        _isFinish.value = value
    }
}