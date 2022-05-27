package com.ezen.lolketing.repository

import com.ezen.lolketing.model.Game
import com.ezen.lolketing.model.Ticket
import com.ezen.lolketing.network.FirebaseClient
import com.ezen.lolketing.util.*
import com.google.firebase.firestore.FirebaseFirestore
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

    companion object {
        const val DATE = "date"
        const val STATUS = "status"
        const val FIVE_DATE = 1000 * 60 * 60 * 24 * 5L
    }

}