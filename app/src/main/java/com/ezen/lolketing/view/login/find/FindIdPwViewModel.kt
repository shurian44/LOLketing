package com.ezen.lolketing.view.login.find

import androidx.lifecycle.viewModelScope
import com.ezen.lolketing.BaseViewModel
import com.ezen.lolketing.repository.FindRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class FindIdPwViewModel @Inject constructor(
    private val repository: FindRepository,
    private val auth: FirebaseAuth
) : BaseViewModel<FindIdPwViewModel.Event>(){

    fun getFindUser(email: String) = viewModelScope.launch {
        repository.getFindUser(
            email = email,
            successListener = {
                event(Event.FindSuccess)
            },
            failureListener = {
                event(Event.FindFailure)
            }
        )
    }

    fun sendPasswordResetEmail(email: String) = viewModelScope.launch {
        auth
            .sendPasswordResetEmail(email)
            .addOnSuccessListener {
                event(Event.SendEmailSuccess)
            }
            .addOnFailureListener {
                event(Event.SendEmailFailure)
                it.printStackTrace()
            }
            .await()
    }

    sealed class Event {
        object FindSuccess: Event()
        object FindFailure: Event()
        object SendEmailSuccess: Event()
        object SendEmailFailure: Event()
    }

}