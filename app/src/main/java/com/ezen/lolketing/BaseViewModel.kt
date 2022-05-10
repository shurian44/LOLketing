package com.ezen.lolketing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

abstract class BaseViewModel<T> : ViewModel() {

    protected val _eventFlow = MutableEventFlow<T>()
    val eventFlow = _eventFlow.asEventFlow()

    open fun event(event : T) = viewModelScope.launch {
        _eventFlow.emit(event)
    }

}