package com.ezen.lolketing.view.main.shop.detail

import androidx.lifecycle.viewModelScope
import com.ezen.lolketing.StatusViewModel
import com.ezen.lolketing.model.ShopItem
import com.ezen.lolketing.repository.PurchaseRepository
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
import javax.inject.Inject
import kotlin.math.max
import kotlin.math.min

@HiltViewModel
class ShopDetailViewModel @Inject constructor(
    private val repository: PurchaseRepository
) : StatusViewModel() {

    private val _count = MutableStateFlow(0L)
    val count: StateFlow<String> = _count
        .map { if (_count.value >= 9) "9+" else "${_count.value}" }
        .stateIn(viewModelScope, SharingStarted.Lazily, "")

    private val _item = MutableStateFlow(ShopItem.create())
    val item: StateFlow<ShopItem> = _item

    private var documentId = ""

    init {
        selectBasketCount()
    }

    private fun selectBasketCount() {
        repository
            .selectBasketCount()
            .onEach { _count.value = it }
            .catch { _count.value = 0 }
            .launchIn(viewModelScope)
    }

    fun setDocumentId(documentId: String) {
        this.documentId = documentId
        fetchShoppingDetail()
    }

    fun getDocumentId() = documentId

    private fun fetchShoppingDetail() {
        repository
            .fetchShoppingDetail(documentId)
            .setLoadingState()
            .onEach { _item.value = it }
            .catch {
                updateMessage(it.message ?: "오류 발생")
                updateFinish(true)
            }
            .launchIn(viewModelScope)
    }

    fun insertShoppingBasket() {
        repository
            .insertShoppingBasket(
                item = _item.value,
                documentId = documentId
            )
            .setLoadingState()
            .onEach { updateMessage("장바구니에 상품을 등록하였습니다") }
            .catch { updateMessage(it.message ?: "오류 발생") }
            .launchIn(viewModelScope)
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