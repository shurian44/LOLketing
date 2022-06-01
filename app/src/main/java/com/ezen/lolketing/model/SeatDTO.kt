package com.ezen.lolketing.model

data class SeatDTO(var seats : Map<String, Boolean> = HashMap())

data class SeatList(
    val list: List<Seat>
)

data class Seat(
    var seatNum: String = "",
    var reserveId: String?= null
)

data class SeatItem(
    var seatNum : String = "",
    var checked: Boolean = false,
    var enabled : Boolean = true
)