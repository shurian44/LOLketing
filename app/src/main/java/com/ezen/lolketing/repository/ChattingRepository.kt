package com.ezen.lolketing.repository

import com.ezen.lolketing.network.FirebaseClient
import com.ezen.lolketing.util.Constants
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ChattingRepository @Inject constructor(
    private val client: FirebaseClient
){

    suspend fun getUserNickName(): String? =
        client.getUserNickName()

    suspend fun getGameData(
        startDate: String,
        successListener: (QuerySnapshot) -> Unit,
        failureListener: () -> Unit
    ) {
        client
            .getBasicSearchData(
                collection = Constants.GAME,
                field = "date",
                startDate = startDate,
                successListener = successListener,
                failureListener = failureListener
            )
    }

}