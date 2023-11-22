package com.ezen.lolketing.repository

import android.content.SharedPreferences
import com.ezen.lolketing.model.Coupon
import com.ezen.lolketing.model.EventDetailItem
import com.ezen.lolketing.network.FirebaseClient
import com.ezen.lolketing.util.Code
import com.ezen.lolketing.util.Constants
import com.ezen.lolketing.util.LoginException
import com.google.firebase.firestore.FieldValue
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class EventRepository @Inject constructor(
    private val client: FirebaseClient,
    private val pref: SharedPreferences
) {
    /** 신규 회원 화면 정보 조회 **/
    fun fetchNewUserInfo() = flow {
        val nickname = client.getUserNickName().getOrThrow()
        val email = pref.getString(Constants.ID, null) ?: throw LoginException.EmptyUser

        val info = client
            .getBasicSearchData(
                collection = Constants.COUPON,
                startDate = email,
                field = "id"
            )
            .getOrThrow()
            .mapNotNull { it.toObject(Coupon::class.java).mapperCouponListInfo(it.id) }
            .filter { it.title == Code.NEW_USER_COUPON.code }
            .maxByOrNull { it.limit }
            ?: throw LoginException.EmptyInfo

        emit(
            EventDetailItem(
                nickname = nickname,
                isUsed = info.use == Code.NOT_USE.code,
                documentId = info.documentId
            )
        )
    }

    /** 신규 쿠폰 사용 및 포인트 적립 **/
    fun updateUseNewUserCoupon(documentId: String) = flow {
        val email = pref.getString(Constants.ID, null) ?: throw LoginException.EmptyUser

        client
            .basicUpdateData(
                collection = Constants.COUPON,
                documentId = documentId,
                updateData = mapOf("use" to Code.USED.code)
            )
            .onFailure { throw Exception("업데이트를 실패하였습니다") }

        client
            .basicUpdateData(
                collection = Constants.USERS,
                documentId = email,
                updateData = mapOf("point" to FieldValue.increment(500)),
            )
            .onFailure { throw Exception("업데이트를 실패하였습니다") }

        emit("500 포인트가 지급되었습니다.")
    }

    /** 룰렛 카운트 조회 **/
    suspend fun getRouletteCount(
        successListener: (Int) -> Unit,
        failureListener: () -> Unit
    ) {
        val email = client.getUserEmail()

        if (email == null) {
            failureListener()
            return
        }

//        client
//            .getBasicSnapshot(
//                collection = Constants.USERS,
//                document = email,
//                successListener = {
//                    it.toObject(Users::class.java)
//                        ?.rouletteCount
//                        ?.let(successListener)
//                        ?: failureListener()
//                },
//                failureListener = failureListener
//            )
    }

    /** 룰렛 사용 업데이트 **/
    suspend fun updateCouponCount(
        successListener: () -> Unit,
        failureListener: () -> Unit
    ) {

        val email = client.getUserEmail()
        if (email == null){
            failureListener()
            return
        }

//        client
//            .basicUpdateData(
//                collection = Constants.USERS,
//                documentId = email,
//                updateData = mapOf(
//                    "rouletteCount" to FieldValue.increment(-1)
//                ),
//                successListener= successListener,
//                failureListener = failureListener
//            )
    }

    /** 쿠폰 발급 **/
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

//        client
//            .basicAddData(
//                collection = Constants.COUPON,
//                data = coupon,
//                successListener = {
//                    successListener()
//                },
//                failureListener = failureListener
//            )
    }

}