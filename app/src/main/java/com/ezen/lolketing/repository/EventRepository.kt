package com.ezen.lolketing.repository

import com.ezen.lolketing.model.Coupon
import com.ezen.lolketing.model.Users
import com.ezen.lolketing.network.FirebaseClient
import com.ezen.lolketing.util.Constants
import com.google.firebase.firestore.FieldValue
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

    suspend fun getRouletteCount(
        successListener: (Int) -> Unit,
        failureListener: () -> Unit
    ) {
        val email = client.getUserEmail()

        if (email == null) {
            failureListener()
            return
        }

        client
            .getBasicSnapshot(
                collection = Constants.USERS,
                document = email,
                successListener = {
                    it.toObject(Users::class.java)
                        ?.rouletteCount
                        ?.let(successListener)
                        ?: failureListener()
                },
                failureListener = failureListener
            )
    }

    suspend fun updateCouponCount(
        successListener: () -> Unit,
        failureListener: () -> Unit
    ) {

        val email = client.getUserEmail()
        if (email == null){
            failureListener()
            return
        }

        client
            .basicUpdateData(
                collection = Constants.USERS,
                documentId = email,
                updateData = mapOf(
                    "rouletteCount" to FieldValue.increment(-1),
                    "point" to FieldValue.increment(-1)
                ),
                successListener= successListener,
                failureListener = failureListener
            )
    }

    suspend fun setCoupon(
        coupon: Coupon,
        successListener: () -> Unit,
        failureListener: () -> Unit
    ) {
        val email = client.getUserEmail()
        if (email == null){
            failureListener()
            return
        }

        coupon.id = email

        client
            .basicAddData(
                collection = Constants.COUPON,
                data = coupon,
                successListener = {
                    successListener()
                },
                failureListener = failureListener
            )
    }

}