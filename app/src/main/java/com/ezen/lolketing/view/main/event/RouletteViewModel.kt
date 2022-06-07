package com.ezen.lolketing.view.main.event

import androidx.lifecycle.viewModelScope
import com.ezen.lolketing.BaseViewModel
import com.ezen.lolketing.model.Coupon
import com.ezen.lolketing.repository.EventRepository
import com.ezen.lolketing.util.Code
import com.ezen.lolketing.util.getCouponValidityPeriod
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class RouletteViewModel @Inject constructor(
    private val repository: EventRepository
) : BaseViewModel<RouletteViewModel.Event>() {

    fun getRouletteCount() = viewModelScope.launch {
        repository.getRouletteCount(
            successListener = {
                event(Event.RouletteCount(it))
            },
            failureListener = {
                event(Event.Failure)
            }
        )
    }

    fun setCoupon(
        point: Int
    ) = viewModelScope.launch {
        val coupon = Coupon(
            limit = getCouponValidityPeriod(),
            title = Code.ROULETTE_COUPON.code,
            use = Code.NOT_USE.code,
            point = point,
            couponNumber = getCouponNumber()
        )
        repository.setCoupon(
            coupon = coupon,
            successListener = {},
            failureListener = {
                event(Event.Failure)
                cancel()
            }
        )

        repository.updateCouponCount(
            successListener = {},
            failureListener = {
                event(Event.Failure)
                cancel()
            }
        )

        event(Event.Success)

    }

    private fun getCouponNumber(): String {
        var couponNumber = ""
        val chooseNum = arrayOf(
            "A", "B", "C", "D", "E", "F",
            "G", "H", "I", "J", "K", "L",
            "M", "N", "O", "P", "Q", "R",
            "S", "T", "U", "V", "W", "X",
            "Y", "Z", "0", "1", "2", "3",
            "4", "5", "6", "7", "8", "9"
        )
        val random = Random()
        for (i in 0..15) {
            couponNumber += chooseNum[random.nextInt(36)]
            if (i % 4 == 3 && i != 15) {
                couponNumber += "-"
            }
        }
        return couponNumber
    }

    sealed class Event {
        data class RouletteCount(val count: Int): Event()
        object Success: Event()
        object Failure: Event()
    }

}