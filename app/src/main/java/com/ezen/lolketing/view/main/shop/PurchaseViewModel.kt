package com.ezen.lolketing.view.main.shop

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ezen.lolketing.database.entity.ShopEntity
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