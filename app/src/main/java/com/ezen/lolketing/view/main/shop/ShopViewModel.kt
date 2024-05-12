package com.ezen.lolketing.view.main.shop

import androidx.lifecycle.viewModelScope
import com.ezen.database.repository.DatabaseRepository
import com.ezen.lolketing.StatusViewModel
import com.ezen.network.model.Goods
import com.ezen.network.repository.PurchaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ShopViewModel @Inject constructor(
    private val repository: PurchaseRepository,
    databaseRepository: DatabaseRepository
) : StatusViewModel() {

    val cartCount = databaseRepository
        .fetchCartCount()
        .map { if (it >= 9) "9+" else if (it <= 0) "" else "$it" }
        .stateIn(viewModelScope, SharingStarted.Lazily, "")

    val tabList = listOf("전체", "스태츄", "피규어", "인형", "액세서리", "의류")
    private var originList = mutableListOf<Goods>()
    private val _list = MutableStateFlow<List<Goods>>(listOf())
    val list: StateFlow<List<Goods>> = _list

    init {
        fetchShopList()
    }

    private fun fetchShopList() {
        repository
            .fetchGoodsItems()
            .setLoadingState()
            .onEach {
                originList.addAll(it)
                updateIndex(0)
            }
            .catch {
                updateMessage(it.message ?: "오류 발생")
                updateFinish()
            }
            .launchIn(viewModelScope)
    }

    fun updateIndex(index: Int) {
        _list.value = when (index) {
            0 -> originList
            else -> originList.filter { it.category == tabList[index] }
        }
    }
}