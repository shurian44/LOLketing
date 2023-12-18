package com.ezen.lolketing.view.main.shop.purchase

import androidx.lifecycle.viewModelScope
import com.ezen.lolketing.StatusViewModel
import com.ezen.lolketing.model.PurchaseInfo
import com.ezen.lolketing.model.ShoppingInfo
import com.ezen.lolketing.repository.PurchaseRepository
import com.ezen.lolketing.util.priceFormat
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

@HiltViewModel
class PurchaseViewModel @Inject constructor(
    private val repository: PurchaseRepository
) : StatusViewModel() {

    private var documentId = ""
    private var amount = 0
    private var databaseIdList = mutableListOf<Long>()

    private val _purchaseStatus = MutableStateFlow<PurchaseStatus>(PurchaseStatus.Init)
    val purchaseStatus: StateFlow<PurchaseStatus> = _purchaseStatus

    private val _count = MutableStateFlow(0L)
    val count: StateFlow<String> = _count
        .map { if (_count.value >= 9) "9+" else "${_count.value}" }
        .stateIn(viewModelScope, SharingStarted.Lazily, "")

    private val _shoppingInfo = MutableStateFlow(ShoppingInfo.create())
    val shoppingInfo: StateFlow<ShoppingInfo> = _shoppingInfo

    private val _list = MutableStateFlow(listOf<PurchaseInfo>())
    val list: StateFlow<List<PurchaseInfo>> = _list
    val totalPrice: StateFlow<String> = _list
        .map {
            runCatching {
                it.sumOf { item -> item.price * item.amount }.priceFormat()
            }.getOrElse { "0원" }
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, "")


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

    fun fetchPurchaseInfo() {
        repository
            .fetchPurchaseInfo(
                databaseIdList = databaseIdList,
                documentId = documentId,
                amount = amount
            )
            .setLoadingState()
            .onEach { (shoppingInfo, list) ->
                _shoppingInfo.value = shoppingInfo
                _list.value = list
                _purchaseStatus.value = PurchaseStatus.InsufficientBalance(
                    isInsufficientBalance = isInsufficientBalance()
                )
            }
            .catch { updateMessage(it.message ?: "오류 발생") }
            .launchIn(viewModelScope)
    }

    fun setDatabaseIdList(list: List<Long>) {
        databaseIdList.addAll(list)
        if (databaseIdList.isEmpty()) {
            updateMessage("오류 발생")
            updateFinish(true)
        }

        fetchPurchaseInfo()
    }

    fun buyNowSetting(documentId: String, amount: Int) {
        this.documentId = documentId
        this.amount = amount
        if (documentId.isEmpty() && amount <= 0) {
            updateMessage("오류 발생")
            updateFinish(true)
        }

        fetchPurchaseInfo()
    }

    fun setAddress(address: String) {
        _shoppingInfo.update {
            it.copy(address = address)
        }
    }

    fun doPayment() {
        if (isInsufficientBalance()) {
            _purchaseStatus.value = PurchaseStatus.RequiresCharging
        } else {
            repository
                .setPurchaseItems(
                    list = _list.value,
                    userInfo = _shoppingInfo.value
                )
                .setLoadingState()
                .onEach {
                    _purchaseStatus.value = PurchaseStatus.PaymentComplete
                }
                .catch { updateMessage(it.message ?: "오류 발생") }
                .launchIn(viewModelScope)
        }
    }

    private fun isInsufficientBalance(): Boolean {
        val totalPrice = runCatching {
            _list.value.sumOf { item -> item.price * item.amount }
        }.getOrElse { 0 }

        return _shoppingInfo.value.cache < totalPrice
    }

    sealed class PurchaseStatus {
        object Init: PurchaseStatus()

        data class InsufficientBalance(
            val isInsufficientBalance: Boolean
        ): PurchaseStatus()

        object RequiresCharging: PurchaseStatus()

        object PaymentComplete: PurchaseStatus()
    }
}