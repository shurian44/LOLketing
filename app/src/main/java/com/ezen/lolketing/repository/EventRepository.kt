package com.ezen.lolketing.repository

import android.content.SharedPreferences
import android.util.Log
import com.ezen.lolketing.model.Coupon
import com.ezen.lolketing.model.Users
import com.ezen.lolketing.network.FirebaseClient
import com.ezen.lolketing.util.Code
import com.ezen.lolketing.util.Constants
import com.ezen.lolketing.view.main.event.EventDetailViewModel
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import javax.inject.Inject

class EventRepository @Inject constructor(
    private val client: FirebaseClient,
    private val pref: SharedPreferences
) {

    suspend fun getUserNickname(): String? {
        return client.getUserNickName()
    }

    suspend fun getCouponList(
        successListener : (EventDetailViewModel.Event.NewUserCoupon?) -> Unit,
        failureListener : () -> Unit
    ) {
        val email = pref.getString(Constants.ID, null) ?: kotlin.run {
            failureListener()
            return
        }

        client.getBasicSearchData(
            collection = Constants.COUPON,
            startDate = email,
            field = "id",
            successListener = { snapshot ->
                snapshot
                    .map {
                        it.toObject(Coupon::class.java).also { coupon ->
                            coupon.documentId = it.id
                        }
                    }
                    .filter {
                        (it.title ?: "") == Code.NEW_USER_COUPON.code
                    }
                    .sortedBy { it.limit }
                    .reversed()
                    .firstOrNull { it.use == Code.NOT_USE.code }
                    ?.let {
                        it.documentId?.let { documentId ->
                            successListener(
                                EventDetailViewModel.Event.NewUserCoupon(documentId = documentId)
                            )
                        } ?: successListener(null)
                    }
                    ?: successListener(null)
            },
            failureListener = failureListener
        )
    }

    suspend fun updateCoupon(
        documentId: String,
        failureListener: () -> Unit
    ) {
        client
            .basicUpdateData(
                collection = Constants.COUPON,
                documentId = documentId,
                updateData = mapOf("use" to Code.USED.code),
                successListener = { },
                failureListener = failureListener
            )
    }

    suspend fun updateUserPoint(
        documentId: String,
        failureListener: () -> Unit
    ) {
        client
            .basicUpdateData(
                collection = Constants.USERS,
                documentId = documentId,
                updateData = mapOf("point" to FieldValue.increment(500)),
                successListener = { },
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