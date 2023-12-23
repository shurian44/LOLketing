package com.ezen.lolketing.model

data class ChattingDTO(
    var id: String ?= null,
    var nickname: String ?= null,
    var gameTime: String ?= null,
    var team: String ?= null,
    var message: String ?= null,
    var timestamp: Long = 0,
) {
    fun mapper() : ChattingItem? {
        return ChattingItem(
            id = id ?: return null,
            nickname = nickname ?: return null,
            gameTime = gameTime ?: return null,
            team = team ?: return null,
            message = message ?: return null,
            timestamp = timestamp
        )
    }
}

data class ChattingItem(
    val id: String = "",
    val nickname: String = "",
    val gameTime: String = "",
    val team: String = "",
    var message: String = "",
    val timestamp: Long = 0
) {
    fun mapper() = ChattingDTO(
        id = id,
        nickname = nickname,
        gameTime = gameTime,
        team = team,
        message = message,
        timestamp = System.currentTimeMillis()
    )
}