package com.ezen.lolketing.view.main.board.team

import androidx.lifecycle.viewModelScope
import com.ezen.lolketing.BaseViewModel
import com.ezen.lolketing.model.PlayerInfo
import com.ezen.lolketing.model.TeamInfo
import com.ezen.lolketing.repository.TeamRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TeamViewModel @Inject constructor(
    private val repository: TeamRepository
) : BaseViewModel<TeamViewModel.Event>() {

    /** 팀 정보 조회 **/
    fun getTeamInfo(team: String) = viewModelScope.launch {
        repository.getTeamInfo(
            team = team,
            successListener = {
                event(Event.Team(it))
                getPlayerInfo(team)
            },
            failureListener = {
                event(Event.Failure)
            }
        )
    }

    /** 선수 정보 조회 **/
    private fun getPlayerInfo(team: String) = viewModelScope.launch {
        repository.getPlayerInfo(
            team = team,
            successListener = {
                event(Event.PlayerList(it))
            },
            failureListener = {
                event(Event.Failure)
            }
        )
    }

    sealed class Event {
        data class Team(
            val info : TeamInfo
        ): Event()
        data class PlayerList(
            val list : List<PlayerInfo>
        ): Event()
        object Failure: Event()
    }

}