package com.ezen.lolketing.view.main.shop.basket

import androidx.lifecycle.viewModelScope
import com.ezen.database.entity.CartItem
import com.ezen.database.repository.DatabaseRepository
import com.ezen.lolketing.StatusViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.max
import kotlin.math.min

@HiltViewModel
class CartViewModel @Inject constructor(
    private val repository: DatabaseRepository
) : StatusViewModel() {

    private val _item = MutableStateFlow(CartItem.create())
    val item: StateFlow<CartItem> = _item

    fun fetchBasketList() {
        repository
            .fetchCartList()
            .onEach { _item.value = it }
            .catch {
                updateMessage(it.message ?: "오류 발생")
                updateFinish()
            }
            .launchIn(viewModelScope)
    }

    fun updateBasketChecked(id: Int) = viewModelScope.launch {
        val index = _item.value.list.indexOfFirst { it.index == id }
        if (index == -1) return@launch

        repository.updateCheckedStatus(id, _item.value.list[index].isChecked.not())
    }

    fun updateCheckedStatusAll() = viewModelScope.launch {
        val value = _item.value.list.any { it.isChecked }.not()
        repository.updateCheckedStatusAll(value)
    }

    fun deleteItems() = viewModelScope.launch {
        val deleteList = _item.value.list.filter { it.isChecked }
        if (deleteList.isEmpty()) {
            updateMessage("아이템을 선택 후 이용해주세요")
            return@launch
        }
        repository.deleteItems(deleteList)
    }

    fun increaseAmount(id: Int) = viewModelScope.launch {
        val updateIndex = _item.value.list.indexOfFirst { it.index == id }
        if (updateIndex == -1) return@launch

        repository.updateAmount(
            index = id,
            amount = min(10, _item.value.list[updateIndex].amount + 1)
        )
    }

    fun decreaseAmount(id: Int) = viewModelScope.launch {
        val updateIndex = _item.value.list.indexOfFirst { it.index == id }
        if (updateIndex == -1) return@launch

        repository.updateAmount(
            index = id,
            amount = max(1, _item.value.list[updateIndex].amount - 1)
        )
    }

    fun getPurchaseItem(): CartItem? {
        val result = _item.value.copy(
            list = _item.value.list.filter { it.isChecked }
        )

        return if (result.list.isEmpty()) {
            updateMessage("구매할 상품을 선택해 주세요")
            null
        } else result
    }

}