package com.ezen.lolketing.view.main.event

import androidx.lifecycle.viewModelScope
import com.ezen.lolketing.BaseViewModel
import com.ezen.lolketing.model.Coupon
import com.ezen.lolketing.repository.EventRepository
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventDetailViewModel @Inject constructor(
    private val repository: EventRepository
) : BaseViewModel<EventDetailViewModel.Event>() {

    fun getUserNickname() = viewModelScope.launch {
        repository.getUserNickname()?.let{
            event(Event.UserNickname(nickname = it))
        } ?: error("유저 정보를 가져오는데 실패하였습니다.")
    }

//        // 사용자의 신규 회원 쿠폰 가져오기
//        firestore.collection("Coupon").whereEqualTo("title", "신규 가입 쿠폰").whereEqualTo("id", id).addSnapshotListener { querySnapshot, firebaseFirestoreException ->
//            for (snapshot in querySnapshot!!) {
//                coupon = snapshot.toObject(Coupon::class.java)
//                documentID = snapshot.id
//            }
//        }

    fun getCouponList() = viewModelScope.launch {
        repository.getCouponList(
            successListener = { snapshot ->
                val list = mutableListOf<Coupon>()
                snapshot.forEach {
                    list.add(
                        it.toObject(Coupon::class.java).also { coupon ->
                            coupon.documentId = it.id
                        }
                    )
                }

                event(Event.CouponList(list))
            },
            failureListener = {
                error("쿠폰 조회에 실패하였습니다.")
            }
        )
    }

//        // 신규 회원 쿠폰 사용 버튼 클릭
//        binding.btnCoupon.setOnClickListener {
//            if (coupon.use == "사용 안함") { // 신규 회원 쿠폰을 사용하지 않았을 경우
//                // 신규 회원 쿠폰 업데이트
//                firestore.collection("Coupon").document(documentID).update("use", "사용함")
//                // 포인트 지급
//                firestore.collection("Users").document(id).update("point", FieldValue.increment(500)).addOnCompleteListener {
//                    while (!it.isComplete){} // 중복 지급 방지
//                }
//                toast("500 포인트가 지급되었습니다.")
//            } else { // 신규 회원 쿠폰을 이미 사용했을 경우
//                toast("이미 사용하셨습니다.")
//            }
//        } // setOnClickListener()

    fun error(msg: String) {
        event(Event.Error(msg))
    }

    sealed class Event {
        data class UserNickname(val nickname: String) : Event()
        data class CouponList(val list : List<Coupon>) : Event()
        data class Error(val msg: String) : Event()
    }

}