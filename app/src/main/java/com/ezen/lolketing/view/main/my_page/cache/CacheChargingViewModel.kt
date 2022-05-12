package com.ezen.lolketing.view.main.my_page.cache

import com.ezen.lolketing.BaseViewModel
import com.ezen.lolketing.model.Users
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CacheChargingViewModel @Inject constructor(
    private val firestore : FirebaseFirestore
): BaseViewModel<CacheChargingViewModel.Event>() {

    private var cache : Int = 0

    fun charging(charging: Int) : Int{
        cache += charging

        // 20억이상 불가능하게 막음
        if(cache > 2000000000){
            cache = 2000000000
        }

        return cache
    }

    private fun getUserCache(email: String) {
        // todo firebase 한번에 처리 예정
        firestore.collection("Users").document(email).get().addOnCompleteListener {
            val user = it.result?.toObject(Users::class.java)!!
            var myCache = user.cache!!
            var point = user.point!!
            var grade = user.grade
            var accPoint = user.accPoint!!

            // 캐시가 20억이 넘을 경우 20억으로 고정 : Int 형 값 넘어가는 오류 발생 방지
            if(myCache + cache.toLong() > 2000000000){
                point += (2000000000 - myCache) / 10
                accPoint += (2000000000 - myCache) / 10
                myCache = 2000000000
            }else{ // 그 외는 충전한 금액 만큼 충전
                point += cache / 10
                accPoint += cache / 10
                myCache += cache
            }

            // 포인트와 누적 포인트도 2억 이상일 경우 2억으로 고정
            if(point > 200000000){
                point = 200000000
            }
            if(accPoint > 200000000){
                accPoint = 200000000
            }

            // 마스터 등급을 제외하고 누적 포인트에 따라 등급 설정
            if(grade != "마스터"){
                grade = when(accPoint){
                    in 0..2900-> "브론즈"
                    in 3000..29999 -> "실버"
                    in 30000..299999 -> "골드"
                    else -> "플래티넘"
                }
            }

            chargingCache(email, Users(cache = myCache, point = accPoint, grade = grade, accPoint = accPoint))

        }

    }

    fun chargingCache(email: String, users: Users) {
        getUserCache(email)
        // 유저 데이터베이스 갱신
        firestore
            .collection("Users")
            .document(email)
            .update(
                "cache", users.cache,
                "point", users.point,
                "grade", users.grade,
                "accPoint", users.accPoint
            )
    }

    sealed class Event {

    }

}