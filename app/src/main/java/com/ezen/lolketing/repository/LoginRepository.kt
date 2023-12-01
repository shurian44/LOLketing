package com.ezen.lolketing.repository

import com.ezen.lolketing.model.Coupon
import com.ezen.lolketing.model.JoinInfo
import com.ezen.lolketing.model.LoginInfo
import com.ezen.lolketing.model.Users
import com.ezen.lolketing.network.FirebaseClient
import com.ezen.lolketing.util.Code
import com.ezen.lolketing.util.Constants
import com.ezen.lolketing.util.LoginException
import com.ezen.lolketing.util.checkPasswordFormat
import com.ezen.lolketing.util.getCouponValidityPeriod
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LoginRepository @Inject constructor(
    private val client: FirebaseClient
) {

    fun fetchLoginInfo() = flow {
        emit(
            client
                .getUserInfo()
                .getOrThrow()
        )
    }

    fun joinUser(
        info: JoinInfo
    ) = flow {
        joinValidationCheek(info)

        val uid = client
            .joinUser(info.id, info.password)
            .getOrElse {
                if (it.message?.contains("The email address is already") == true) {
                    throw LoginException.DuplicateJoin
                }
                ""
            }
            .ifEmpty { throw Exception("회원가입 실패") }

        client
            .registerUser(
                email = info.id,
                uid = uid
            )
            .onFailure { deleteUser() }

        emit("회원가입 완료")
    }

    private fun joinValidationCheek(info: JoinInfo) = when {
        android.util.Patterns.EMAIL_ADDRESS.matcher(info.id).matches().not() ->
            throw Exception("아이디는 이메일 형식으로 입력해주세요")

        checkPasswordFormat(info.password) -> throw Exception("비밀번호는 영문, 숫자, 특수 문자를 모두 포함하여 6~20자를 입력하주세요")
        info.password != info.passwordCheck -> throw Exception("비밀번호를 확인해주세요")
        else -> true
    }

    fun emailLogin(loginInfo: LoginInfo) = flow {
        client
            .emailLogin(loginInfo)
            .getOrThrow()

        emit(
            client
                .getUserInfo()
                .getOrThrow()
        )
    }

    private suspend fun deleteUser() {
        client.deleteUser()
    }

    fun fetchUserInfo() = flow {
        val email = client.getUserEmail() ?: throw LoginException.EmptyUser

        client
            .getBasicSnapshot(
                collection = Constants.USERS,
                document = email,
                valueType = Users::class.java
            )
            .getOrThrow()
            ?.let {
                emit(it)
            }
            ?: throw LoginException.EmptyUser
    }

    fun updateUserInfo(
        user: Users
    ) = flow {
        updateValidationCheck(user)

        client
            .basicUpdateData(
                collection = Constants.USERS,
                documentId = user.id ?: client.getUserEmail() ?: throw LoginException.EmptyUser,
                updateData = mapOf(
                    "nickname" to user.nickname,
                    "phone" to user.phone,
                    "address" to user.address
                )
            )
            .onSuccess { emit("업데이트가 완료되었습니다.") }
            .onFailure { throw it }
    }

    private fun updateValidationCheck(user: Users) = when {
        user.nickname.isNullOrEmpty() -> throw Exception("닉네임을 입력해주세요.")
        user.nickname?.length !in 2..10 -> throw Exception("닉네임의 길이는 2~10자 입니다.")
        user.phone != null && user.phone?.length !in 10..11 ->
            throw Exception("전화번호를 확인해주세요")

        else -> true
    }

    fun setNewUserCoupon() = flow {
        val email = client.getUserEmail() ?: throw LoginException.EmptyUser
        val coupon = Coupon(
            id = email,
            title = Code.NEW_USER_COUPON.code,
            use = Code.NOT_USE.code,
            limit = getCouponValidityPeriod(),
            point = 500,
            timestamp = System.currentTimeMillis(),
        )

        client
            .basicAddData(
                collection = Constants.COUPON,
                data = coupon
            )
            .onSuccess { emit("쿠폰 발급 완료") }
            .onFailure { throw it }
    }

    fun logout() = client.signOut()

}