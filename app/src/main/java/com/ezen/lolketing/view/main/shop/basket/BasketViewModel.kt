package com.ezen.lolketing.view.main.shop.basket

import androidx.lifecycle.viewModelScope
import com.ezen.lolketing.StatusViewModel
import com.ezen.lolketing.model.PurchaseInfo
import com.ezen.lolketing.repository.PurchaseRepository
import com.ezen.lolketing.util.priceFormat
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BasketViewModel @Inject constructor(
    private val repository: PurchaseRepository
) : StatusViewModel() {

    private val _list = MutableStateFlow(listOf<PurchaseInfo>())
    val list: StateFlow<List<PurchaseInfo>> = _list
    private val _totalPrice = MutableStateFlow("")
    val totalPrice: StateFlow<String> = _totalPrice

    fun fetchBasketList() {
        repository
            .fetchBasketList()
            .onEach {
                _list.value = it
                getTotalPrice()
            }
            .catch { updateMessage(it.message ?: "오류 발생") }
            .launchIn(viewModelScope)
    }

    fun updateBasketChecked(id: Long) = viewModelScope.launch {
        val index = _list.value.indexOfFirst { it.databaseId == id }
        if (index == -1) {
            return@launch
        }
        val newList = _list.value.also {
            it[index].isChecked = it[index].isChecked.not()
        }
        _list.value = newList

        repository.updateBasketChecked(id, _list.value[index].isChecked)
        getTotalPrice()
    }

    private fun getTotalPrice() {
        _totalPrice.value = _list.value
            .filter { it.isChecked }
            .sumOf { it.price * it.amount }
            .priceFormat()
    }

    fun getIdList() = _list.value.filter { it.isChecked }.map { it.databaseId }.toLongArray()

}