package com.ezen.lolketing.repository

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
        successListener: () -> Unit,
        failureListener: () -> Unit
    ) {
        client.joinUser(
            email= email,
            pw= pw,
            successListener = successListener,
            failureListener = failureListener
        )
    }

    suspend fun registerUser(
        email: String,
        successListener: () -> Unit,
        failureListener: () -> Unit
    ) {
        client.registerUser(
            email = email,
            successListener= successListener,
            failureListener = failureListener
        )
    }

    suspend fun deleteUser(
        successListener: () -> Unit,
    ) {
        client.deleteUser(
            successListener= successListener
        )
    }

    suspend fun getUserInfo(
        email: String,
        successListener: () -> Unit,
        failureListener: (String) -> Unit
    ) {
        client.getBasicSnapshot(
            collection = Constants.USERS,
            document = email,
            successListener= {
                val user = it.toObject(Users::class.java)
                if (user == null) {
                    failureListener(email)
                } else {
                    successListener()
                }
            },
            failureListener= {
                failureListener(email)
            }
        )
    }

}