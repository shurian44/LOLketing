package com.ezen.lolketing.view.main.shop.purchase

import androidx.lifecycle.viewModelScope
import com.ezen.database.entity.CartItem
import com.ezen.lolketing.StatusViewModel
import com.ezen.network.model.PurchaseInfo
import com.ezen.network.repository.MainRepository
import com.ezen.network.repository.PurchaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.max
import kotlin.math.min

@HiltViewModel
class PurchaseViewModel @Inject constructor(
    private val repository: PurchaseRepository,
    private val mainRepository: MainRepository
) : StatusViewModel() {

    private val _item = MutableStateFlow(PurchaseItem())
    val item: StateFlow<PurchaseItem> = _item

    fun fetchPurchaseInfo(cartItem: CartItem) {
        repository
            .fetchPurchaseInfo()
            .setLoadingState()
            .onEach { info ->
                _item.update {
                    it.copy(cartItem = cartItem, purchaseInfo = info)
                }
            }
            .catch { updateMessage(it.message ?: "유저 정보 조회 실패") }
            .launchIn(viewModelScope)
    }

    fun setAddress(address: String) {
        _item.update {
            it.copy(
                purchaseInfo = it.purchaseInfo.copy(address = address)
            )
        }
    }

    fun increaseAmount(id: Int) {
        val tempList = _item.value.cartItem.list.toMutableList()
        val updateIndex = tempList.indexOfFirst { it.index == id }
        if (updateIndex == -1) return

        tempList[updateIndex] = tempList[updateIndex].copy(
            amount = min(10, tempList[updateIndex].amount + 1)
        )
        val totalPrice = tempList.map { it.price * it.amount.toLong() }.reduce { acc, l -> acc + l }

        _item.update {
            it.copy(
                cartItem = it.cartItem.copy(
                    list = tempList,
                    totalPrice = totalPrice
                )
            )
        }
    }

    fun decreaseAmount(id: Int) {
        val tempList = _item.value.cartItem.list.toMutableList()
        val updateIndex = tempList.indexOfFirst { it.index == id }
        if (updateIndex == -1) return

        tempList[updateIndex] = tempList[updateIndex].copy(
            amount = max(1, tempList[updateIndex].amount - 1),
        )
        val totalPrice = tempList.map { it.price * it.amount.toLong() }.reduce { acc, l -> acc + l }

        _item.update {
            it.copy(
                cartItem = it.cartItem.copy(
                    list = tempList,
                    totalPrice = totalPrice
                )
            )
        }
    }

    fun deleteItem(index: Int) {
        val tempList = _item.value.cartItem.list.toMutableList()
        if (tempList.size <= 1) {
            _item.update { it.copy(uiStatus = PurchaseUiStatus.LastItemDelete) }
            return
        }

        val selectIndex = tempList.indexOfFirst { it.index == index }
        if (selectIndex == -1) return
        tempList.removeAt(selectIndex)

        _item.update {
            it.copy(cartItem = _item.value.cartItem.copy(list = tempList))
        }
    }

    fun updateCashCharging(cash: Int) {
        mainRepository
            .updateCashCharging(cash)
            .setLoadingState()
            .onEach { result ->
                _item.update {
                    it.copy(
                        purchaseInfo = it.purchaseInfo.copy(cash = result.cash)
                    )
                }
            }
            .catch { updateMessage(it.message ?: "캐시 충전 실패") }
            .launchIn(viewModelScope)
    }

    fun checkPurchase() = viewModelScope.launch {
        val value = _item.value
        when {
            value.purchaseInfo.checkValidation().not() ->
                updateMessage("배송 정보를 입력해주세요")

            value.purchaseInfo.cash < value.cartItem.totalPrice ->
                updateUiStatus(PurchaseUiStatus.NeedCashCharging)

            else -> updateUiStatus(PurchaseUiStatus.MakePayment)
        }

        delay(500)
        updateUiStatus(PurchaseUiStatus.Init)
    }

    private fun updateUiStatus(status: PurchaseUiStatus) {
        _item.update {
            it.copy(uiStatus = status)
        }
    }

    fun makePayment() {
        repository
            .insertProductPurchase(_item.value.cartItem.list)
            .setLoadingState()
            .onEach {
                updateMessage(it)
                _item.update { item -> item.copy(uiStatus = PurchaseUiStatus.PaymentSuccess) }
            }
            .catch { updateMessage(it.message ?: "결제 실패") }
            .launchIn(viewModelScope)
    }
}

data class PurchaseItem(
    val cartItem: CartItem = CartItem.create(),
    val purchaseInfo: PurchaseInfo = PurchaseInfo.init(),
    val uiStatus: PurchaseUiStatus = PurchaseUiStatus.Init
)

sealed class PurchaseUiStatus {
    object Init : PurchaseUiStatus()

    object NeedCashCharging : PurchaseUiStatus()

    object LastItemDelete : PurchaseUiStatus()

    object MakePayment : PurchaseUiStatus()

    object PaymentSuccess : PurchaseUiStatus()
}