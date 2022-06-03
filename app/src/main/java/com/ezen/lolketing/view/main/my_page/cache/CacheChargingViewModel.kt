package com.ezen.lolketing.view.main.my_page.cache

import androidx.lifecycle.viewModelScope
import com.ezen.lolketing.BaseViewModel
import com.ezen.lolketing.model.CacheModifyUser
import com.ezen.lolketing.repository.PaymentRepository
import com.ezen.lolketing.util.Constants
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CacheChargingViewModel @Inject constructor(
    private val firestore : FirebaseFirestore,
    private val repository: PaymentRepository
): BaseViewModel<CacheChargingViewModel.Event>() {

    private lateinit var user : CacheModifyUser

    fun getUserCache() = viewModelScope.launch {
        repository.getUserInfo(
            successListener = { user ->
                this@CacheChargingViewModel.user = user
                val cache = user.cache

                if (cache == null){
                    event(Event.Failure)
                } else{
                    event(Event.RetainedCache(cache))
                }

            },
            failureListener = {
                event(Event.Failure)
            }
        )
    }

    fun chargingCache(chargingCache: Long) {
        val myCache = user.cache ?: 0
        var point = user.point ?: 0
        var accPoint = user.accPoint ?: 0
        var grade = user.grade ?: Constants.BRONZE

        // 캐시가 100만이 넘을 경우 100만으로 고정 : Int 형 값 넘어가는 오류 발생 방지
        if(myCache + chargingCache > 1_000_000){
            point += (1_000_000 - myCache.toInt()) / 10
            accPoint += (1_000_000 - myCache.toInt()) / 10
        }else{ // 그 외는 충전한 금액 만큼 충전
            point += (chargingCache / 10).toInt()
            accPoint += (chargingCache / 10).toInt()
        }

        // 포인트와 누적 포인트도 2억 이상일 경우 2억으로 고정
        if(point > 1_000_000){
            point = 1_000_000
        }

        if(accPoint > 1_000_000){
            accPoint = 1_000_000
        }

        // 마스터 등급을 제외하고 누적 포인트에 따라 등급 설정
        if(grade != Constants.MASTER){
            grade = when(accPoint){
                in 0..2900-> Constants.BRONZE
                in 3000..29999 -> Constants.SILVER
                in 30000..299999 -> Constants.GOLD
                else -> Constants.PLATINUM
            }
        }

        chargingCache(chargingCache, grade, point, accPoint)
    }

    private fun chargingCache(
        addCache : Long,
        grade: String,
        point: Int,
        accPoint: Int
    ) = viewModelScope.launch {
        repository
            .updateChargingCache(
                addCache = addCache,
                grade = grade,
                point = point,
                accPoint = accPoint,
                successListener = {
                    event(Event.Success)
                },
                failureListener = {
                    event(Event.Failure)
                }
            )
    }

    sealed class Event {
        data class RetainedCache(val cache: Long): Event()
        object Success: Event()
        object Failure: Event()
    }

}