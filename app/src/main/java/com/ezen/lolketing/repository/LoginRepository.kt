package com.ezen.lolketing.repository

import com.ezen.lolketing.model.Coupon
import com.ezen.lolketing.model.JoinInfo
import com.ezen.lolketing.model.LoginInfo
import com.ezen.lolketing.model.UserInfo
import com.ezen.lolketing.model.Users
import com.ezen.lolketing.network.FirebaseClient
import com.ezen.lolketing.util.checkPasswordFormat
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LoginRepository @Inject constructor(
    private val client: FirebaseClient
) {

    fun fetchLoginInfo() = client.getUserEmail()

    suspend fun joinUser(
        email: String,
        pw: String,
        successListener: (String?) -> Unit,
        failureListener: () -> Unit
    ) = try {
//        client.joinUser(
//            email= email,
//            pw= pw,
//            successListener = successListener,
//            failureListener = failureListener
//        )
    } catch (e: Exception) {
        e.printStackTrace()
        failureListener()
    }

    fun joinUser(
        info: JoinInfo
    ) = flow {
        joinValidationCheek(info)

        val uid = client
            .joinUser(info.id, info.password)
            .getOrThrow()
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

    suspend fun registerUser(
        email: String,
        uid: String,
        successListener: () -> Unit,
        failureListener: () -> Unit
    ) = try {
//        client.registerUser(
//            email = email,
//            uid = uid,
//            successListener= successListener,
//            failureListener = failureListener
//        )
    } catch (e: Exception) {
        e.printStackTrace()
        failureListener()
    }

    suspend fun emailLogin(loginInfo: LoginInfo) = client.emailLogin(loginInfo)

    suspend fun deleteUser() {
        client.deleteUser()
    }

    suspend fun getUserInfo(
        email: String,
        successListener: (UserInfo) -> Unit,
        failureListener: (String) -> Unit
    ) = try {
//        client.getBasicSnapshot(
//            collection = Constants.USERS,
//            document = email,
//            successListener= { snapshot ->
//                snapshot.toObject(Users::class.java)
//                    ?.toUserInfo()
//                    ?.let(successListener)
//                    ?:failureListener(email)
//            },
//            failureListener= {
//                failureListener(email)
//            }
//        )
    } catch (e: Exception) {
        e.printStackTrace()
        failureListener(email)
    }

    suspend fun updateUserInfo(
        user: Users,
        successListener: () -> Unit,
        failureListener: () -> Unit
    ) {
        try {
//            val id = user.id
//            if (id == null) {
//                failureListener()
//                return
//            }
//
//            client.basicUpdateData(
//                collection = Constants.USERS,
//                documentId = id,
//                updateData = mapOf("nickname" to user.nickname, "phone" to user.phone, "address" to user.address),
//                successListener = successListener,
//                failureListener = failureListener
//            )
        } catch (e: Exception) {
            e.printStackTrace()
            failureListener()
        }
    }

    private fun Users.toUserInfo() =
        UserInfo(
            nickname = nickname,
            phone = phone,
            address = address
        )

    suspend fun setNewUserCoupon(
        coupon: Coupon,
        successListener: () -> Unit,
        failureListener: () -> Unit
    ) = try {
//        client.basicAddData(
//            collection = Constants.COUPON,
//            data = coupon,
//            successListener = {
//                successListener()
//            },
//            failureListener = failureListener
//        )
    } catch (e: Exception) {
        e.printStackTrace()
        failureListener()
    }

}