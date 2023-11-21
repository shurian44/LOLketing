package com.ezen.lolketing.view.main

import androidx.lifecycle.viewModelScope
import com.ezen.lolketing.BaseViewModel
import com.ezen.lolketing.R
import com.ezen.lolketing.StatusViewModel
import com.ezen.lolketing.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: MainRepository
) : StatusViewModel() {

    private val _bannerImage = MutableStateFlow(
        listOf(R.drawable.banner1, R.drawable.banner2, R.drawable.banner3)
    )
    val bannerImage: StateFlow<List<Int>> = _bannerImage

    private val _isNotJoinComplete = MutableStateFlow(true)
    val isNotJoinComplete: StateFlow<Boolean> = _isNotJoinComplete

    init {
        getUserInfo()
    }

    // 유저 정보 조회
    private fun getUserInfo() = viewModelScope.launch {
        repository
            .getUserInfo()
            .setLoadingState()
            .onEach {
                _isNotJoinComplete.value = it
            }
            .catch {
                updateMessage("유저 정보 조회 중 오류가 발생하였습니다.")
            }
            .launchIn(viewModelScope)
    }

}
