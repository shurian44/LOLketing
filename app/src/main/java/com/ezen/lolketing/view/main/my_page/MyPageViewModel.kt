package com.ezen.lolketing.view.main.my_page

import com.ezen.lolketing.BaseViewModel
import com.ezen.lolketing.model.Users
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(

) : BaseViewModel<MyPageViewModel.Event>() {

    // todo firebase 한번에 처리
//            firestore.collection("Coupon").whereEqualTo("id", id).get().addOnCompleteListener {
//            var coupon = it.result!!.toObjects(Coupon::class.java)
//            var couponCount = 0
//            for(i in coupon.indices){
//                if(coupon[i].use == "사용 안함"){
//                    couponCount++
//                }
//            }
//        }

    //// 유저 정보 불러오기
    //        firestore.collection("Users").document(id).get().addOnCompleteListener {
    //            var user = it.result?.toObject(Users::class.java)!!
    //        }

    sealed class Event {
        data class CouponCounter(
            val numberOfCoupon : Int
        ) : Event()
        data class UserInfo(
            val user : Users
        ) : Event()
    }

}