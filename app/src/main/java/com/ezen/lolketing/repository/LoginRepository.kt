package com.ezen.lolketing.repository

import com.ezen.lolketing.model.Coupon
import com.ezen.lolketing.model.UserInfo
import com.ezen.lolketing.model.Users
import com.ezen.lolketing.network.FirebaseClient
import com.ezen.lolketing.util.Constants
import javax.inject.Inject

class LoginRepository @Inject constructor(
    private val client: FirebaseClient
) {

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

    suspend fun deleteUser(
        successListener: () -> Unit,
    ) = try {
//        client.deleteUser(
//            successListener= successListener
//        )
    } catch (e: Exception) {
        e.printStackTrace()
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
        user : Users,
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