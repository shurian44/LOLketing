package com.ezen.lolketing.view.main.shop.history

import androidx.lifecycle.viewModelScope
import com.ezen.lolketing.StatusViewModel
import com.ezen.lolketing.model.PurchaseHistory
import com.ezen.lolketing.repository.PurchaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class PurchaseHistoryViewModel @Inject constructor(
    private val repository: PurchaseRepository
) : StatusViewModel() {

    private val _list = MutableStateFlow(listOf<PurchaseHistory>())
    val list: StateFlow<List<PurchaseHistory>> = _list

    fun fetchPurchaseHistory() {
        repository
            .fetchPurchaseHistory()
            .setLoadingState()
            .onEach { _list.value = it }
            .catch { updateMessage(it.message ?: "오류 발생") }
            .launchIn(viewModelScope)
    }

}