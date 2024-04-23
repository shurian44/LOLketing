package com.ezen.network.repository

import com.ezen.database.repository.DatabaseRepository
import com.ezen.network.client.ChattingClient
import com.ezen.network.client.FirebaseClient
import com.ezen.network.model.ChattingModel
import com.ezen.network.model.ChattingRoomInfo
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class ChattingRepositoryImpl @Inject constructor(
    private val client: ChattingClient,
    private val firebaseClient: FirebaseClient,
    private val databaseRepository: DatabaseRepository
): ChattingRepository {

    override fun fetchChattingList(date: String) = flow {
        client.fetchChattingList(date)
            .onSuccess { emit(it) }
            .onFailure { throw it }
    }

    override fun observeChatUpdates(info: ChattingRoomInfo) = callbackFlow {
        val email = databaseRepository.getUserEmail()
        if (email.isEmpty()) throw Exception("유저 정보가 없습니다.")

        val listener = firebaseClient
            .getFireStore(Chat)
            .whereEqualTo(GameId, info.gameId)
            .orderBy(TimeStamp)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                value?.let { snapshot ->
                    snapshot
                        .mapNotNull { it.toObject(ChattingModel::class.java) }
                        .mapNotNull {
                            it.mapper(
                                leftTeam = info.leftTeam,
                                rightTeam = info.rightTeam,
                                myEmail = email
                            )
                        }
                        .let { trySend(it) }
                }
            }

        awaitClose { listener.remove() }
    }

    override fun addChat(
        message: String,
        selectedTeam: String,
        info: ChattingRoomInfo
    ) = flow {
        val email = databaseRepository.getUserEmail()
        val nickname = databaseRepository.getUserNickname()
        if (email.isEmpty() || nickname.isEmpty()) throw Exception("유저 정보가 없습니다.")
        if (info.gameId == 0) throw Exception("게임 정보가 없습니다.")
        if (isToday(info.gameDate).not()) throw Exception("게임 당일에만 채팅을 입력하실 수 있습니다.")

        firebaseClient
            .basicAddData(
                collection = Chat,
                data = ChattingModel(
                    gameId = info.gameId,
                    email = email,
                    nickname = nickname,
                    gameDate = info.gameDate,
                    team = selectedTeam,
                    message = message,
                    timestamp = System.currentTimeMillis()
                )
            )
            .getOrThrow()
            .let { emit(Unit) }
    }

    private fun isToday(date: String): Boolean {
        val sdf = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault())
        val currentDate = Date(System.currentTimeMillis())
        return sdf.format(currentDate) == date
    }

    companion object {
        private const val Chat = "Chat"
        private const val TimeStamp = "timestamp"
        private const val GameId = "gameId"
    }
}