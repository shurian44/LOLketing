package com.ezen.lolketing.view.main

import androidx.lifecycle.viewModelScope
import com.ezen.auth.repository.AuthRepository
import com.ezen.lolketing.StatusViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    repository: AuthRepository
) : StatusViewModel() {

    val isLogin = repository.isLogin()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(500),
            initialValue = true
        )

}
