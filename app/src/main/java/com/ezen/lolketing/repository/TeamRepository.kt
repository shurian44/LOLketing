package com.ezen.lolketing.repository

import android.util.Log
import com.ezen.lolketing.model.PlayerInfo
import com.ezen.lolketing.model.TeamDTO
import com.ezen.lolketing.model.TeamInfo
import com.ezen.lolketing.model.mapper
import com.ezen.lolketing.network.FirebaseClient
import com.ezen.lolketing.util.Constants
import javax.inject.Inject

class TeamRepository @Inject constructor(
    private val client: FirebaseClient
) {

    /** 팀 정보 조회 **/
    suspend fun getTeamInfo(
        team: String,
        successListener: (TeamInfo) -> Unit,
        failureListener: () -> Unit
    ) {
        client
            .getBasicSnapshot(
                collection = Constants.TeamCollection,
                document = team,
                successListener = { snapshot ->
                    snapshot.toObject(TeamDTO::class.java)
                        ?.mapper()
                        ?.let(successListener)
                        ?: failureListener()
                },
                failureListener = failureListener
            )
    }

    /** 선수 정보 조회 **/
    suspend fun getPlayerInfo(
        team: String,
        successListener: (List<PlayerInfo>) -> Unit,
        failureListener: () -> Unit
    ) {
        client
            .getDoubleSnapshot(
                firstCollection = Constants.TeamCollection,
                firstDocument = team,
                secondCollection = Constants.PLAYER,
                orderByField = "name",
                successListener = { snapshot ->
                    snapshot.mapNotNull {
                        it.toObject(TeamDTO.PlayerDTO::class.java)
                            .mapper()
                    }.let(successListener)
                },
                failureListener = failureListener
            )
    }

}