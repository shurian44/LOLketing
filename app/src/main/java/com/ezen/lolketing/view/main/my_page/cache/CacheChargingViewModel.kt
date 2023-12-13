package com.ezen.lolketing.view.main.my_page.cache

import androidx.lifecycle.viewModelScope
import com.ezen.lolketing.StatusViewModel
import com.ezen.lolketing.model.CacheModifyUser
import com.ezen.lolketing.repository.PaymentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import kotlin.math.min

@HiltViewModel
class CacheChargingViewModel @Inject constructor(
    private val repository: PaymentRepository
): StatusViewModel() {

    private val _info = MutableStateFlow(CacheModifyUser())
    val info: StateFlow<CacheModifyUser> = _info

    /** 캐시 조회 **/
    fun fetchCacheDetailInfo() {
        repository
            .fetchCacheDetailInfo()
            .setLoadingState()
            .onEach { _info.value = it }
            .catch {
                updateMessage(it.message ?: "오류 발생")
                updateFinish(true)
            }
            .launchIn(viewModelScope)
    }

    fun updateChargeAmount(amount: Long) {
        _info.update {
            it.copy(chargingCache = min(it.chargingCache + amount, 100_000_000))
        }
    }

    fun updateChargingCache() {
        repository
            .updateChargingCache(info = _info.value.updateInfo())
            .setLoadingState()
            .onEach {
                updateMessage(it)
                updateFinish(true)
            }
            .catch { updateMessage(it.message ?: "오류 발생") }
            .launchIn(viewModelScope)
    }

}