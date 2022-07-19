package com.ezen.lolketing.repository

import com.ezen.lolketing.model.*
import com.ezen.lolketing.network.FirebaseClient
import com.ezen.lolketing.util.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class TicketingRepository @Inject constructor(
    private val client: FirebaseClient,
    private val firestore: FirebaseFirestore
) {

    /** 게임 일정 조회 **/
    suspend fun getGameList(
        date: String,
        onSuccessListener: (List<Ticket>) -> Unit,
        onFailureListener: () -> Unit
    ) {
        try {
            firestore
                .collection(Constants.GAME)
                .orderBy(DATE)
                .whereGreaterThan(DATE, date)
                .get()
                .addOnSuccessListener { snapshot ->
                    snapshot
                        .mapNotNull {
                            it.toObject(Game::class.java).mapper()
                        }
                        .let(onSuccessListener)
                }
                .addOnFailureListener {
                    it.printStackTrace()
                    onFailureListener()
                }
                .await()
        } catch (e: Exception) {
            e.printStackTrace()
            onFailureListener()
        }
    }

    /** 유저 정보 조회 : 마스터 등급 여부 **/
    suspend fun isMasterUser(
        listener: (Boolean) -> Unit
    ) = try {
        client
            .getUserInfo(
                successListener = { user ->
                    listener(user.grade == Code.MASTER.code)
                },
                failureListener = {
                    listener(false)
                }
            )
    } catch (e: Exception) {
        e.printStackTrace()
        listener(false)
    }

    /** 경기 일정 추가 **/
    suspend fun addNewGame(
        date: String,
        time: String,
        successListener: () -> Unit,
        failureListener: () -> Unit,
    ) {
        val gameData = Game(
            date = date,
            status = Code.TICKETING_ON.code,
            team = getRandomGame(),
            time = time
        )

        client
            .basicAddData(
                collection = Constants.GAME,
                document = "$date $time",
                data = gameData,
                successListener = successListener,
                failureListener = failureListener
            )

    }

    /** 좌석 추가 **/
    suspend fun setReservedSeat(
        documentId: String,
        successListener: () -> Unit
    ) {
        getSeatList().forEach {
            client
                .doubleAddData(
                    firstCollection = Constants.GAME,
                    firstDocument = documentId,
                    secondCollection = Constants.SEAT,
                    data = it,
                    successListener = {}
                )
        }
        successListener()
    }

    /** 좌석 조회 **/
    suspend fun getReservedSeat(
        documentId: String,
        hall: String,
        onSuccessListener: (List<SeatItem>) -> Unit,
        onFailureListener: () -> Unit
    ) {
        try {
            client
                .getDoubleSnapshot(
                    firstCollection = Constants.GAME,
                    firstDocument = documentId,
                    orderByField = "seatNum",
                    orderByDirection = Query.Direction.ASCENDING,
                    secondCollection = Constants.SEAT,
                    successListener = { querySnapshot ->
                        querySnapshot
                            .map {
                                it.toObject(Seat::class.java)
                                    .also { seat ->
                                        seat.seatNum = seat.seatNum.replace("$hall ", "")
                                    }
                                    .mapper(it.id)
                            }
                            .let(onSuccessListener)
                    },
                    failureListener = onFailureListener
                )
        } catch (e: Exception) {
            e.printStackTrace()
            onFailureListener()
        }
    }

    /** 좌석 리스트 생성 **/
    private fun getSeatList(): List<Seat> {
        val list = mutableListOf<Seat>()
        rowList.forEach {
            for (i in 1..9) {
                list.add(Seat("A홀 $it$i"))
            }
        }

        return list
    }

    companion object {
        const val DATE = "date"
        val rowList = listOf("A", "B", "C", "D", "E", "F", "G", "H")
    }

}