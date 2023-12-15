package com.ezen.lolketing.view.main.shop

import androidx.lifecycle.viewModelScope
import com.ezen.lolketing.StatusViewModel
import com.ezen.lolketing.model.ShopListItem
import com.ezen.lolketing.repository.ShopRepository
import com.ezen.lolketing.util.Code
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class ShopViewModel @Inject constructor(
    private val repository: ShopRepository
) : StatusViewModel() {

    private val shoppingCategories = Code.getShoppingItems()
    val shoppingCategoryNames = shoppingCategories.map { it.codeName }

    private var originList = listOf<ShopListItem>()
    private val _list = MutableStateFlow(listOf<ShopListItem>())
    val list: StateFlow<List<ShopListItem>> = _list

    init {
        fetchShopList()
    }

    private fun fetchShopList() {
        repository
            .fetchShoppingList()
            .setLoadingState()
            .onEach {
                originList = it
                setQuery(0)
            }
            .catch { updateMessage(it.message ?: "오류 발생") }
            .launchIn(viewModelScope)
    }

    fun setQuery(index: Int) = runCatching {
        val query = if (index == 0) "" else shoppingCategories[index].code
        _list.value = originList.filter {
            if (query.isEmpty()) true else it.group == query
        }
    }.onFailure {
        _list.value = originList
    }
}