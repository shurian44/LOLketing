package com.ezen.lolketing.repository

import com.ezen.lolketing.model.ChattingDTO
import com.ezen.lolketing.model.ChattingItem
import com.ezen.lolketing.model.Game
import com.ezen.lolketing.network.FirebaseClient
import com.ezen.lolketing.util.Constants
import com.ezen.lolketing.util.LoginException
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ChattingRepository @Inject constructor(
    private val client: FirebaseClient,
) {

    /** 게임 데이터 조회 **/
    fun fetchGameDate(startDate: String) = flow {
        client
            .getBasicSearchData(
                collection = Constants.GAME,
                field = "date",
                startDate = startDate,
                valueType = Game::class.java
            )
            .getOrThrow()
            .mapNotNull { (game, _) -> game.mapperChattingInfo() }
            .let { emit(it) }
    }

    fun fetchUserInfo() = flow {
        val id = client.getUserEmail() ?: throw LoginException.EmptyInfo
        val nickname = client.getUserNickName().getOrNull() ?: throw LoginException.EmptyInfo

        emit(Pair(id, nickname))
    }

    fun addChat(chattingItem: ChattingItem) = flow {
        client
            .basicAddData(
                collection = Constants.CHAT,
                data = chattingItem.mapper()
            )
            .getOrThrow()
            .let { emit("success") }
    }

    fun observeChatUpdates(
        time: String
    ) = callbackFlow {
        val listener = client
            .getFireStore(Constants.CHAT)
            .whereEqualTo("gameTime", time)
            .orderBy(Constants.TIMESTAMP)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                value?.let { snapshot ->
                    snapshot
                        .mapNotNull { it.toObject(ChattingDTO::class.java) }
                        .mapNotNull { it.mapper() }
                        .let { trySend(it) }
                }
            }

        awaitClose { listener.remove() }
    }

}