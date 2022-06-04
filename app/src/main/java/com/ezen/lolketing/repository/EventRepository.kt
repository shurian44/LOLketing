package com.ezen.lolketing.repository

import com.ezen.lolketing.network.FirebaseClient
import com.ezen.lolketing.util.Constants
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import javax.inject.Inject

class EventRepository @Inject constructor(
    private val client: FirebaseClient
) {

    suspend fun getUserNickname(): String? {
        return client.getUserNickName()
    }

//    firestore.collection("Coupon").whereEqualTo("title", "신규 가입 쿠폰").whereEqualTo("id", id).addSnapshotListener { querySnapshot, firebaseFirestoreException ->
//            for (snapshot in querySnapshot!!) {
//                coupon = snapshot.toObject(Coupon::class.java)
//                documentID = snapshot.id
//            }
//        }
    // todo Event database 수정 필정
    suspend fun getCouponList(
        successListener : (QuerySnapshot) -> Unit,
        failureListener : () -> Unit
    ) {
        val email = client.getCurrentUser()?.email ?: throw Exception("회원 정보 오류")

        client.getBasicQuerySnapshot(
            collection = Constants.COUPON,
            field = "id",
            query = "kmj94102@gmail.com",
            orderByField = "title",
            orderByDirection = Query.Direction.ASCENDING,
            successListener = successListener,
            failureListener = failureListener
        )
    }

}