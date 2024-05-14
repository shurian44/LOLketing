package com.ezen.lolketing.view.main.shop.detail

import androidx.lifecycle.viewModelScope
import com.ezen.database.repository.DatabaseRepository
import com.ezen.lolketing.StatusViewModel
import com.ezen.network.model.GoodsDetail
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
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.max
import kotlin.math.min

@HiltViewModel
class ShopDetailViewModel @Inject constructor(
    private val repository: PurchaseRepository,
    private val databaseRepository: DatabaseRepository
) : StatusViewModel() {

    val cartCount = databaseRepository
        .fetchCartCount()
        .map { if (it >= 9) "9+" else if (it <= 0) "" else "$it" }
        .stateIn(viewModelScope, SharingStarted.Lazily, "")

    private val _item = MutableStateFlow(GoodsDetail.init())
    val item: StateFlow<GoodsDetail> = _item

    private val _uiStatus = MutableStateFlow<ShopDetailUIStatus>(ShopDetailUIStatus.Init)
    val uiStatus: StateFlow<ShopDetailUIStatus> = _uiStatus

    fun fetchShoppingDetail(goodsId: Int) {
        repository
            .fetchGoodsItemDetail(goodsId)
            .setLoadingState()
            .onEach { _item.value = it }
            .catch {
                updateMessage(it.message ?: "오류 발생")
            }
            .launchIn(viewModelScope)
    }

    fun insertShoppingBasket() = viewModelScope.launch {
        databaseRepository
            .insertGoods(_item.value.toEntity())
            .onSuccess { _uiStatus.value = ShopDetailUIStatus.BasketInsertSuccess }
            .onFailure { updateMessage(it.message ?: "오류 발생") }
    }

    fun updateInitStatus() {
        _uiStatus.value = ShopDetailUIStatus.Init
    }

    fun increaseAmount() {
        _item.update {
            it.copy(amount = min(10, it.amount + 1))
        }
    }

    fun decreaseAmount() {
        _item.update {
            it.copy(amount = max(1, it.amount - 1))
        }
    }

}

sealed class ShopDetailUIStatus {
    object Init: ShopDetailUIStatus()

    object BasketInsertSuccess: ShopDetailUIStatus()
}