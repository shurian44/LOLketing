package com.ezen.lolketing.model

data class Game(
    var date : String ?= null,
    var time : String ?= null,
    var team : String ?= null,
    var result : String ?= null,
    var status : String ?= null
) {

    fun mapper() : Ticket? {
        return Ticket(
            date = date?: return null,
            time = time?: return null,
            team = team?: return null,
            status = status?: return null
        )
    }

    fun mapperChattingInfo() : ChattingInfo? {
        return ChattingInfo(
            time = time?: return null,
            team = team?: return null
        )
    }
}

data class Ticket(
    var date : String,
    var time : String,
    var team : String,
    var status : String
)

data class ChattingInfo(
    val time : String,
    val team : String,
)