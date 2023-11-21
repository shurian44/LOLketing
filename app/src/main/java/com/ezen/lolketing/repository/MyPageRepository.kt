package com.ezen.lolketing.repository

import com.ezen.lolketing.model.Coupon
import com.ezen.lolketing.model.CouponInfo
import com.ezen.lolketing.model.MyPageCouponInfo
import com.ezen.lolketing.model.MyPageInfo
import com.ezen.lolketing.network.FirebaseClient
import com.ezen.lolketing.util.Code
import com.ezen.lolketing.util.Constants
import com.google.firebase.firestore.FieldValue
import javax.inject.Inject

class MyPageRepository @Inject constructor(
    private val client: FirebaseClient
) {

    /** 유저 정보 조회 **/
    suspend fun getUserInfo(
        successListener: (MyPageInfo) -> Unit,
        failureListener: () -> Unit
    ) = try {
//        client.getUserInfo(
//            successListener = {
//                it.mapperMyPageInfo()?.let(successListener) ?: failureListener()
//            },
//            failureListener = failureListener
//        )
    } catch (e: Exception) {
        e.printStackTrace()
        failureListener()
    }

    /** 쿠폰 조회 : 사용 정보 파악용도 **/
    suspend fun getUserCoupon(
        id: String,
        successListener: (List<MyPageCouponInfo>) -> Unit,
        failureListener: () -> Unit
    ) = try {
//        client
//            .getBasicQuerySnapshot(
//                collection = Constants.COUPON,
//                field = "id",
//                query = id,
//                successListener = { querySnapshot ->
//                    val result = querySnapshot.mapNotNull {
//                        it.toObject(Coupon::class.java).mapperMyPageCouponInfo()
//                    }
//                    successListener(result)
//                },
//                failureListener = failureListener
//            )
    } catch (e: Exception) {
        e.printStackTrace()
        failureListener()
    }

    /** 회원 탈퇴 **/
    suspend fun deleteUser(
        successListener: () -> Unit,
        failureListener: () -> Unit
    ) = try {
//        client.deleteUser(
//            successListener = successListener,
//            failureListener = failureListener,
//        )
    } catch (e: Exception) {
        e.printStackTrace()
    }

    suspend fun getCouponList(
        successListener: (List<CouponInfo>) -> Unit,
        failureListener: () -> Unit
    ) = try {

        val id = client.getUserEmail() ?: throw Exception("email is null")

//        client
//            .getBasicQuerySnapshot(
//                collection = Constants.COUPON,
//                field = "id",
//                query = id,
//                successListener = { snapshot ->
//                    val result = snapshot.mapNotNull {
//                        it.toObject(Coupon::class.java)
//                            .mapperCouponListInfo(it.id)
//                    }
//                    successListener(result)
//                },
//                failureListener = failureListener
//            )
    } catch (e: Exception) {
        e.printStackTrace()
        failureListener()
    }

    /** 쿠폰 업데이트 **/
    suspend fun updateCoupon(
        documentId: String,
        point: Int,
        successListener: () -> Unit,
        failureListener: () -> Unit
    ) = try {

//        val id = client.getUserEmail() ?: throw Exception("email is null")
//
//        client
//            .basicUpdateData(
//                collection = Constants.COUPON,
//                documentId = documentId,
//                updateData = mapOf("use" to Code.USED.code),
//                successListener = {},
//                failureListener = failureListener
//            )
//
//        client
//            .basicUpdateData(
//                collection = Constants.USERS,
//                documentId = id,
//                updateData = mapOf(
//                    "point" to FieldValue.increment(point.toLong())
//                ),
//                successListener = {},
//                failureListener = failureListener
//            )
//
//        successListener()
    } catch (e: Exception) {
        e.printStackTrace()
        failureListener()
    }

}