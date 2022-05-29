package com.ezen.lolketing.repository

import com.ezen.lolketing.model.Coupon
import com.ezen.lolketing.model.Users
import com.ezen.lolketing.network.FirebaseClient
import com.ezen.lolketing.util.Constants
import com.ezen.lolketing.view.login.join.JoinDetailViewModel
import javax.inject.Inject

class LoginRepository @Inject constructor(
    private val client: FirebaseClient
) {

    fun getCurrentUser() = client.getCurrentUser()

    suspend fun joinUser(
        email: String,
        pw: String,
        successListener: (String?) -> Unit,
        failureListener: () -> Unit
    ) = try {
        client.joinUser(
            email= email,
            pw= pw,
            successListener = successListener,
            failureListener = failureListener
        )
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
        client.registerUser(
            email = email,
            uid = uid,
            successListener= successListener,
            failureListener = failureListener
        )
    } catch (e: Exception) {
        e.printStackTrace()
        failureListener()
    }

    suspend fun registerUser(
        user: Users,
        successListener: () -> Unit,
        failureListener: () -> Unit
    ) = try {
        client.registerUser(
            user = user,
            successListener= successListener,
            failureListener = failureListener
        )
    } catch (e: Exception) {
        e.printStackTrace()
    }

    suspend fun deleteUser(
        successListener: () -> Unit,
    ) = try {
        client.deleteUser(
            successListener= successListener
        )
    } catch (e: Exception) {
        e.printStackTrace()
    }

    suspend fun getUserInfo(
        email: String,
        successListener: (JoinDetailViewModel.Event.UserInfo) -> Unit,
        failureListener: (String) -> Unit
    ) = try {
        client.getBasicSnapshot(
            collection = Constants.USERS,
            document = email,
            successListener= {
                val user = it.toObject(Users::class.java)
                if (user == null) {
                    failureListener(email)
                } else {
                    successListener(user.toUserInfo())
                }
            },
            failureListener= {
                failureListener(email)
            }
        )
    } catch (e: Exception) {
        e.printStackTrace()
        failureListener(email)
    }

    suspend fun updateModifyUserInfo(
        user : Users,
        successListener: () -> Unit,
        failureListener: () -> Unit
    ) {
        try {
            val id = user.id
            if (id == null) {
                failureListener()
                return
            }

            client.basicUpdateData(
                collection = Constants.USERS,
                documentId = id,
                updateData = mapOf("nickname" to user.nickname, "phone" to user.phone, "address" to user.address),
                successListener = successListener,
                failureListener = failureListener
            )
        } catch (e: Exception) {
            e.printStackTrace()
            failureListener()
        }
    }

    private fun Users.toUserInfo() =
        JoinDetailViewModel.Event.UserInfo(
            nickname = nickname,
            phone = phone,
            address = address
        )

    suspend fun setNewUserCoupon(
        coupon: Coupon,
        successListener: () -> Unit,
        failureListener: () -> Unit
    ) = try {
        client.basicAddData(
            collection = Constants.COUPON,
            data = coupon,
            successListener = {
                successListener()
            },
            failureListener = failureListener
        )
    } catch (e: Exception) {
        e.printStackTrace()
        failureListener()
    }

}