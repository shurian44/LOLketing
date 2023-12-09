package com.ezen.lolketing.repository

import com.ezen.lolketing.model.Game
import com.ezen.lolketing.model.Seat
import com.ezen.lolketing.model.SeatItem
import com.ezen.lolketing.network.FirebaseClient
import com.ezen.lolketing.util.Constants
import com.ezen.lolketing.util.getCurrentDateTime
import com.ezen.lolketing.util.timestampToString
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class TicketingRepository @Inject constructor(
    private val client: FirebaseClient
) {

    private fun getYesterdayDateTime() : String =
        (System.currentTimeMillis() - (1000 * 60 * 60 * 24)).timestampToString("yyyy.MM.dd")

    /** 게임 일정 조회 **/
    fun fetchGameList() = flow {
        val date = getYesterdayDateTime()
        val list = client
            .fetchTicketList(date)
            .getOrThrow()
            .toMutableList()

        if (list.isEmpty()) {
            val today = getCurrentDateTime().time.timestampToString("yyyy.MM.dd")
            addNewGame(Game.createRandomGame(today, "17:00"))
            addNewGame(Game.createRandomGame(today, "20:00"))

            list.addAll(
                client
                    .fetchTicketList(date)
                    .getOrThrow()
                    .toMutableList()
            )
        }

        emit(list.filter { it.team.split(":").size == 2 })
    }

    /** 경기 일정 추가 **/
    private suspend fun addNewGame(game: Game) {
        client
            .basicAddData(
                collection = Constants.GAME,
                document = game.getDocumentId(),
                data = game
            )
            .getOrThrow()

        getSeatList().forEach {
            client
                .doubleAddData(
                    firstCollection = Constants.GAME,
                    firstDocument = game.getDocumentId(),
                    secondCollection = Constants.SEAT,
                    data = it
                )
        }
    }

    /** 좌석 조회 **/
    suspend fun getReservedSeat(
        documentId: String,
        hall: String,
        onSuccessListener: (List<SeatItem>) -> Unit,
        onFailureListener: () -> Unit
    ) {
        try {
//            client
//                .getDoubleSnapshot(
//                    firstCollection = Constants.GAME,
//                    firstDocument = documentId,
//                    orderByField = "seatNum",
//                    orderByDirection = Query.Direction.ASCENDING,
//                    secondCollection = Constants.SEAT,
//                    successListener = { querySnapshot ->
//                        querySnapshot
//                            .map {
//                                it.toObject(Seat::class.java)
//                                    .also { seat ->
//                                        seat.seatNum = seat.seatNum.replace("$hall ", "")
//                                    }
//                                    .mapper(it.id)
//                            }
//                            .let(onSuccessListener)
//                    },
//                    failureListener = onFailureListener
//                )
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