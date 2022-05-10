package com.ezen.lolketing.repository

import com.ezen.lolketing.network.FirebaseClient
import com.ezen.lolketing.util.Constants
import com.google.firebase.firestore.Query
import javax.inject.Inject

class BoardRepository @Inject constructor(
    private val client: FirebaseClient
){

    suspend fun getBoardList(field : String = Constants.TEAM, query : String) : Query? = try {
        client.getBasicQuerySnapshot(
            collection = Constants.BOARD,
            field = field,
            query = query
        )?.query
    } catch (e : Exception) {
        e.printStackTrace()
        null
    }

    suspend fun getBoardList(queryList: List<Pair<String, Any>>) : Query ?= try {
        client.getBasicQuerySnapshot(
            collection = Constants.BOARD,
            queryList = queryList
        )?.query
    } catch (e : Exception) {
        e.printStackTrace()
        null
    }

}