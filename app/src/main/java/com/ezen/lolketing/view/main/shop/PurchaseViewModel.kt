package com.ezen.lolketing.view.main.shop

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ezen.lolketing.database.entity.ShopEntity
import com.ezen.lolketing.model.PurchaseHistory
import com.ezen.lolketing.model.ShippingInfo
import com.ezen.lolketing.repository.PurchaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PurchaseViewModel @Inject constructor(
    val repository: PurchaseRepository
) : ViewModel() {

    val purchaseState = MutableStateFlow<Event>(Event.Init)
    val userInfoState = MutableStateFlow<ShippingInfo?>(null)
    val purchaseHistoryState = MutableStateFlow<List<PurchaseHistory>>(emptyList())
    var isDeleteDialogState = mutableStateOf(false)
    var isOutOfCacheDialogState = mutableStateOf(false)
    var isPurchaseDialogState = mutableStateOf(false)

    fun selectAllShoppingBasket() {
        repository.selectAllShoppingBasket()
            .onStart { purchaseState.value = Event.PurchaseLoading(true) }
            .onEach { purchaseState.value = Event.PurchaseItems(it) }
            .onEmpty { purchaseState.value = Event.PurchaseItems(emptyList()) }
            .catch { purchaseState.value = Event.PurchaseLoading(false) }
            .launchIn(viewModelScope)
    }

    fun selectShoppingBasket(list: List<Long>) {
        repository.selectShoppingBasketList(list)
            .onStart { purchaseState.value = Event.PurchaseLoading(true) }
            .onEach { purchaseState.value = Event.PurchaseItems(it) }
            .onEmpty { purchaseState.value = Event.PurchaseItems(emptyList()) }
            .catch { purchaseState.value = Event.PurchaseLoading(false) }
            .launchIn(viewModelScope)
    }

    fun updateBasketChecked(id: Long, isChecked: Boolean) = viewModelScope.launch {
        repository.updateBasketChecked(id, isChecked)
    }

    fun deleteBasketItems(idList: List<Long>) = viewModelScope.launch {
        repository.deleteBasketItems(idList)
    }

    fun getUserInfo() = viewModelScope.launch {
        repository.getUserInfo(
            successListener = {
                userInfoState.value = it
            },
            failureListener = {
                userInfoState.value = null
            }
        )
    }

    fun setPurchaseItems(
        list: List<ShopEntity>,
        userInfo: ShippingInfo,
        message: String,
        successListener: () -> Unit,
        failureListener: () -> Unit
    ) = viewModelScope.launch {
        repository.setPurchaseItems(
            list = list,
            userInfo = userInfo,
            message = message,
            successListener = {
                deletePurchase(list.map { it.id })
                successListener()
            },
            failureListener = failureListener
        )
    }

    private fun deletePurchase(idList: List<Long>) = viewModelScope.launch {
        repository.deleteBasketItems(idList = idList)
    }

    fun getPurchaseHistoryList() = viewModelScope.launch {
        repository.getPurchaseHistoryList(
            successListener = {
                purchaseHistoryState.value = it
            },
            failureListener = {
                purchaseHistoryState.value = emptyList()
            }
        )
    }

    sealed class Event {
        object Init : Event()
        data class PurchaseLoading(
            val isLoading: Boolean
        ) : Event()

        data class PurchaseItems(
            val list: List<ShopEntity>
        ) : Event()
    }

}