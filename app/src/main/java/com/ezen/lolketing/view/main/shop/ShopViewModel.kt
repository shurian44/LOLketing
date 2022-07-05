package com.ezen.lolketing.view.main.shop

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ezen.lolketing.model.ShopItem
import com.ezen.lolketing.model.ShopListItem
import com.ezen.lolketing.repository.ShopRepository
import com.ezen.lolketing.util.Code
import com.ezen.lolketing.util.getCurrentDateTime
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShopViewModel @Inject constructor(
    private val repository: ShopRepository
) : ViewModel() {

    val shopListState = MutableStateFlow<List<ShopListItem>>(emptyList())
    val shopItemState = MutableStateFlow<ShopItem?>(null)
    val purchaseCount = MutableStateFlow(1)

    fun getShopList(tabIndex: Int) = viewModelScope.launch {
        val query = when(tabIndex) {
            1 -> { Code.STATUE.code }
            2 -> { Code.FIGURE.code }
            3 -> { Code.ACCESSORY.code }
            4 -> { Code.DOLL.code }
            5 -> { Code.T_SHIRT.code }
            6 -> { Code.JACKET.code }
            7 -> { Code.PAJAMAS.code }
            8 -> { Code.ART.code }
            9 -> { Code.BOARD_GAME.code }
            else -> { "" }
        }

        if (query.isEmpty()) {
            repository.getShoppingList(
                successListener = {
                    shopListState.value = it
                },
                failureListener = {
                    shopListState.value = emptyList()
                }
            )
        } else {
            repository.getShoppingList(
                query = query,
                successListener = {
                    shopListState.value = it
                },
                failureListener = {
                    shopListState.value = emptyList()
                }
            )
        }


    }

    fun getShopItem(documentId: String) = viewModelScope.launch {
        repository.getShoppingItem(
            documentId = documentId,
            successListener = {
                shopItemState.value = it
            },
            failureListener = {
                shopItemState.value = null
            }
        )
    }

    fun purchaseCountIncrease() {
        purchaseCount.value = purchaseCount.value + 1
    }

    fun purchaseCountDecrease() {
        purchaseCount.value = purchaseCount.value - 1
    }

    fun insertShopBasket() = viewModelScope.launch {
        val item = shopItemState.value ?: return@launch
        repository.insertShoppingBasket(
            item = item,
            count = purchaseCount.value,
            timestamp = System.currentTimeMillis(),
            listener = {

            }
        )
    }

    fun getShopBasketList() = viewModelScope.launch {
        repository.selectAllShoppingBasket {
            it.onStart {
            }
            .onEach {
            }
            .onEmpty {
            }
            .catch {
            }
            .launchIn(viewModelScope)
        }
    }

}