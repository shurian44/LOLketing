package com.ezen.lolketing.repository

import com.ezen.lolketing.model.Coupon
import com.ezen.lolketing.model.CouponInfo
import com.ezen.lolketing.network.FirebaseClient
import com.ezen.lolketing.util.Constants
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MyPageRepository @Inject constructor(
    private val client: FirebaseClient
) {

    fun fetchUserInfo() = flow {
        val info = client
            .getUserInfo()
            .getOrThrow()
            .mapperMyPageInfo()
            ?: throw Throwable("유저 정보 조회 실패")

        val couponInfoList = client
            .getBasicQuerySnapshot(
                collection = Constants.COUPON,
                field = "id",
                query = info.id,
                valueType = Coupon::class.java
            )
            .getOrThrow()
            .mapNotNull { it.mapperMyPageCouponInfo() }

        emit(
            info.copy(
                couponInfo = "${couponInfoList.count { it }} / ${couponInfoList.size}"
            )
        )
    }

    /** 로그아웃 **/
    fun signOut() = client.signOut()

    /** 회원 탈퇴 **/
    suspend fun deleteUser() {
        client.deleteUser()
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