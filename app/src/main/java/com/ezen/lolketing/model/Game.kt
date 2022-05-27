package com.ezen.lolketing.model

data class Game(
    var date : String ?= null,
    var time : String ?= null,
    var team : String ?= null,
    var result : String ?= null,
    var status : String ?= null
) {
    fun mapper() = Ticket(
        date = date,
        time = time,
        team = team,
        status = status
    )
}

data class Ticket(
    var date : String ?= null,
    var time : String ?= null,
    var team : String ?= null,
    var status : String ?= null
)