package com.ezen.lolketing.repository

import com.ezen.lolketing.model.ChattingInfo
import com.ezen.lolketing.model.Game
import com.ezen.lolketing.network.FirebaseClient
import com.ezen.lolketing.util.Constants
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ChattingRepository @Inject constructor(
    private val client: FirebaseClient
) {

    /** 유저 닉네임 조회1 **/
    suspend fun getUserNickName(): String? =
        client.getUserNickName()

    /** 게임 데이터 조회 **/
    suspend fun getGameData(
        startDate: String,
        successListener: (List<ChattingInfo>) -> Unit,
        failureListener: () -> Unit
    ) = try {
        client
            .getBasicSearchData(
                collection = Constants.GAME,
                field = "date",
                startDate = startDate,
                successListener = { snapshot ->
                    snapshot
                        .mapNotNull { it.toObject(Game::class.java).mapperChattingInfo() }
                        .let(successListener)
                },
                failureListener = failureListener
            )
    } catch (e: Exception) {
        e.printStackTrace()
        failureListener()
    }

}