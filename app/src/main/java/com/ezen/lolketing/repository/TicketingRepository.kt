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

    suspend fun getGameList(
        date : String,
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
                    val list = mutableListOf<Ticket>()
                    snapshot.forEach {
                        val game = updateGame(it.toObject(Game::class.java))
                        list.add(game.mapper())
                    }
                    onSuccessListener(list)
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

    private fun updateGame(game: Game) : Game {
        // todo firebase 기능 조사해보기
//        FirebaseFirestore.getInstance().collection("Game").document("${model.date} ${model.time}").update("status", "종료")
        val currentDate = getCurrentDateTime().time.timestampToString("yyyy.MM.dd HH:mm")
        val startTime = "${game.date} ${game.time}"
        val openTime = ((getSimpleDateFormatMillSec(startTime) ?: 0) - FIVE_DATE).timestampToString("yyyy.MM.dd HH:mm")

        if (currentDate > startTime) {
            game.status = Code.TICKETING_FINISH.code
        }

        if (openTime > currentDate) {
            game.status = Code.TICKETING_SCHEDULE_OPEN.code
        }

        return game
    }

    suspend fun isMasterUser() : Boolean {
        val email = client.getCurrentUser()?.email ?: return false

        val user = client
            .getUserInfo(
                collectionPath = Constants.USERS,
                documentPath = email
            )
            ?.toObject(Users::class.java)
            ?: return false

        return user.grade == Constants.MASTER
    }

    suspend fun addNewGame(
        date: String,
        time: String,
        successListener : () -> Unit,
        failureListener : () -> Unit,
    ){
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
                    secondDocument = DOCUMENT_SEAT,
                    data = it,
                    successListener = {}
                )
        }
        successListener()
    }

    suspend fun getReservedSeat(
        documentId: String,
        hall : String,
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
                        val list = mutableListOf<Seat>()

                        querySnapshot.forEach { snapshot ->
                            list.add(
                                snapshot.toObject(Seat::class.java).also { seat ->
                                    seat.documentId = snapshot.id
                                }
                            )
                        }

                        if (list.isEmpty()){
                            onFailureListener()
                            return@getDoubleSnapshot
                        }

                        list
                            .filter { it.seatNum.contains(hall) }
                            .map {
                                it.seatNum = it.seatNum.replace("$hall ", "")
                                it
                            }
                            .map { it.mapper() }
                            .let {
                                onSuccessListener(it)
                            }
                    },
                    failureListener = onFailureListener
                )
        } catch (e: Exception) {
            e.printStackTrace()
            onFailureListener()
        }
    }

    private fun getSeatList() : List<Seat> {
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
        const val STATUS = "status"
        const val DOCUMENT_SEAT = "seat"
        const val FIVE_DATE = 1000 * 60 * 60 * 24 * 5L
        val rowList = listOf("A", "B", "C", "D", "E", "F", "G", "H")
    }

}