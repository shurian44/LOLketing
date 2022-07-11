package com.ezen.lolketing.repository

import com.ezen.lolketing.model.Coupon
import com.ezen.lolketing.model.MyPageCouponInfo
import com.ezen.lolketing.model.MyPageInfo
import com.ezen.lolketing.network.FirebaseClient
import com.ezen.lolketing.util.Constants
import javax.inject.Inject

class MyPageRepository @Inject constructor(
    private val client: FirebaseClient
) {

    suspend fun getUserInfo(
        successListener: (MyPageInfo) -> Unit,
        failureListener: () -> Unit
    ) = try {
        client.getUserInfo(
            successListener = {
                it.mapperMyPageInfo()?.let(successListener) ?: failureListener()
            },
            failureListener = failureListener
        )
    } catch (e: Exception) {
        e.printStackTrace()
        failureListener()
    }

    suspend fun getUserCoupon(
        id: String,
        successListener: (List<MyPageCouponInfo>) -> Unit,
        failureListener: () -> Unit
    ) = try {
        client
            .getBasicQuerySnapshot(
                collection = Constants.COUPON,
                field = "id",
                query = id,
                successListener = { querySnapshot ->
                    val result = querySnapshot.mapNotNull {
                        it.toObject(Coupon::class.java).mapperMyPageCouponInfo()
                    }
                    successListener(result)
                },
                failureListener = failureListener
            )
    } catch (e: Exception) {
        e.printStackTrace()
        failureListener()
    }

    suspend fun deleteUser(
        successListener: () -> Unit,
        failureListener: () -> Unit
    ) = try {
        client.deleteUser(
            successListener = successListener,
            failureListener = failureListener,
        )
    } catch (e: Exception) {
        e.printStackTrace()
    }

}