package com.ezen.lolketing.view.main.shop.history

import androidx.lifecycle.viewModelScope
import com.ezen.lolketing.StatusViewModel
import com.ezen.network.model.PurchaseHistoryItem
import com.ezen.network.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class PurchaseHistoryViewModel @Inject constructor(
    private val repository: MainRepository
) : StatusViewModel() {

    private val _item = MutableStateFlow(PurchaseHistoryItem())
    val item: StateFlow<PurchaseHistoryItem> = _item

    fun fetchItems() {
        repository
            .fetchPurchaseHistoryInfo()
            .onEach { _item.value = it }
            .catch { updateMessage(it.message ?: "구매 정보를 찾을 수 없습니다.") }
            .launchIn(viewModelScope)
    }

    fun updateIsTicket(value: Boolean) {
        _item.update { it.copy(isTicket = value) }
    }

}