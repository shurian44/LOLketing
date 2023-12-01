package com.ezen.lolketing.repository

import com.ezen.lolketing.model.Coupon
import com.ezen.lolketing.model.EventDetailItem
import com.ezen.lolketing.model.Users
import com.ezen.lolketing.network.FirebaseClient
import com.ezen.lolketing.util.Code
import com.ezen.lolketing.util.Constants
import com.ezen.lolketing.util.LoginException
import com.ezen.lolketing.util.getCouponValidityPeriod
import com.google.firebase.firestore.FieldValue
import kotlinx.coroutines.flow.flow
import java.util.Random
import javax.inject.Inject

class EventRepository @Inject constructor(
    private val client: FirebaseClient
) {
    /** 신규 회원 화면 정보 조회 **/
    fun fetchNewUserInfo() = flow {
        val nickname = client.getUserNickName().getOrThrow()
        val email = client.getUserEmail() ?: throw LoginException.EmptyUser

        val info = client
            .getBasicSearchData(
                collection = Constants.COUPON,
                startDate = email,
                field = "id",
                valueType = Coupon::class.java
            )
            .getOrThrow()
            .mapNotNull { (coupon, documentId) ->
                coupon.mapperCouponListInfo(documentId)
            }
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
        val email = client.getUserEmail() ?: throw LoginException.EmptyUser

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
    fun getRouletteCount() = flow {
        val email = client.getUserEmail()?: throw LoginException.EmptyInfo

        client
            .getBasicSnapshot(
                collection = Constants.USERS,
                document = email,
                valueType = Users::class.java
            )
            .getOrThrow()
            ?.rouletteCount
            ?.let {
                emit(it)
            }
            ?: throw Exception("쿠폰 조회에 실패하였습니다.")
    }

    /** 쿠폰 발급 **/
    fun couponIssuance(point: Int) = flow {
        val email = client.getUserEmail() ?: throw LoginException.EmptyUser
        val coupon = Coupon(
            id = email,
            limit = getCouponValidityPeriod(),
            title = Code.ROULETTE_COUPON.code,
            use = Code.NOT_USE.code,
            point = point,
            couponNumber = createCouponNumber(),
            timestamp = System.currentTimeMillis()
        )

        client
            .basicAddData(
                collection = Constants.COUPON,
                data = coupon
            )
            .getOrThrow()

        client
            .basicUpdateData(
                collection = Constants.USERS,
                documentId = email,
                updateData = mapOf("rouletteCount" to FieldValue.increment(-1))
            )
            .getOrThrow()

        emit("축하합니다! ${point}RP에 당첨되셨습니다!!")
    }

    /** 쿠폰 번호 성생 **/
    private fun createCouponNumber(): String {
        var couponNumber = ""
        val chooseNum = arrayOf(
            "A", "B", "C", "D", "E", "F",
            "G", "H", "I", "J", "K", "L",
            "M", "N", "O", "P", "Q", "R",
            "S", "T", "U", "V", "W", "X",
            "Y", "Z", "0", "1", "2", "3",
            "4", "5", "6", "7", "8", "9"
        )
        val random = Random()
        for (i in 0..15) {
            couponNumber += chooseNum[random.nextInt(36)]
            if (i % 4 == 3 && i != 15) {
                couponNumber += "-"
            }
        }
        return couponNumber
    }

}