package com.ezen.lolketing.repository

import com.ezen.lolketing.network.FirebaseClient
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val client: FirebaseClient
) {

    suspend fun getUserInfo(
        successListener: (Boolean) -> Unit,
        failureListener: () -> Unit
    ) = try {
        client.getUserInfo(
            successListener = {
                successListener(it.nickname.isNullOrEmpty())
            },
            failureListener = failureListener
        )
    } catch (e: Exception) {
        e.printStackTrace()
        failureListener()
    }

}