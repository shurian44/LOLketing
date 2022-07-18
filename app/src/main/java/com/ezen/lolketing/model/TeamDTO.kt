package com.ezen.lolketing.model

data class TeamDTO(
    var team_name: String? = null,
    var team_name_k: String? = null,
    var team_color: String? = null,
    var foundation: String? = null,
    var head_coach: String? = null,
    var coach: ArrayList<String>? = null,
    var captain: String? = null
) {
    data class PlayerDTO(
        var name: String? = null,
        var nickname: String? = null,
        var position: String? = null,
        var img: String? = null
    )
}

data class TeamInfo(
    var teamName: String,
    var teamNameK: String,
    var teamColor: String,
    var foundation: String,
    var headCoach: String,
    var coach: ArrayList<String>,
    var captain: String
)

fun TeamDTO.mapper() : TeamInfo? {
    return TeamInfo(
        teamName = team_name ?: "",
        teamNameK = team_name_k ?: "",
        teamColor = team_color ?: return null,
        foundation = foundation ?: return null,
        headCoach = head_coach ?: return null,
        coach = coach ?: return null,
        captain = captain ?: return null,
    )
}

data class PlayerInfo(
    var name: String,
    var nickname: String,
    var position: String,
    var img: String
)

fun TeamDTO.PlayerDTO.mapper() : PlayerInfo? {
    return PlayerInfo(
        name = name ?: return null,
        nickname = nickname ?: return null,
        position = position ?: return null,
        img = img ?: return null,
    )
}