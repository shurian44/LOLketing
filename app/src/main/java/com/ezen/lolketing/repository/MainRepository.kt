package com.ezen.lolketing.repository

import com.ezen.lolketing.model.Users
import com.ezen.lolketing.network.FirebaseClient
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val client: FirebaseClient
) {

    suspend fun getUserInfo(
        successListener: (Users) -> Unit,
        failureListener: () -> Unit
    ) {
        client.getUserInfo(
            successListener = successListener,
            failureListener = failureListener
        )
    }

}