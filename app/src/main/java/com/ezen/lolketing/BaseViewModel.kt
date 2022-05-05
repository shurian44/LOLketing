package com.ezen.lolketing

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Job

abstract class BaseViewModel<T> : ViewModel() {

    protected val _eventFlow = MutableEventFlow<T>()
    val eventFlow = _eventFlow.asEventFlow()

    abstract fun event(event : T) : Job

}