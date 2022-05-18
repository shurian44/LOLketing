package com.ezen.lolketing.view.login.join

import androidx.lifecycle.viewModelScope
import com.ezen.lolketing.BaseViewModel
import com.ezen.lolketing.repository.LoginRepository
import com.google.android.gms.tasks.OnSuccessListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JoinViewModel @Inject constructor(
    private val repository: LoginRepository
) : BaseViewModel<JoinViewModel.Event>() {

    fun joinUser(
        email: String,
        pw: String
    ) = viewModelScope.launch {
        repository.joinUser(
            email= email,
            pw= pw,
            successListener = {
                registerUser(email)
            },
            failureListener = {
                error(ALREADY_JOIN)
            }
        )
    }

    private fun registerUser(
        email: String
    ) = viewModelScope.launch {
        repository.registerUser(
            email = email,
            successListener = {
                event(Event.Success)
            },
            failureListener = {
                deleteUser()
                error(JOIN_FAILURE)
            }
        )
    }

    private fun deleteUser() = viewModelScope.launch {
        repository.deleteUser {}
    }

    private fun error(msg: Int) {
        event(Event.Error(msg))
    }

    sealed class Event {
        object Success: Event()
        data class Error(val code: Int): Event()
    }

    companion object {
        const val JOIN_FAILURE = 1
        const val ALREADY_JOIN = 2
    }

}