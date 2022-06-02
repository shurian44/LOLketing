package com.ezen.lolketing.model

data class SeatDTO(var seats : Map<String, Boolean> = HashMap())

data class SeatList(
    val list: List<Seat> = listOf()
)

data class Seat(
    var seatNum: String = "",
    var reserveId: String?= null,
    var documentId: String = ""
) {
    fun mapper() = SeatItem(
        seatNum = seatNum,
        enabled = reserveId.isNullOrEmpty(),
        documentId = documentId
    )
}

data class SeatItem(
    var seatNum : String = "",
    var documentId: String = "",
    var checked: Boolean = false,
    var enabled : Boolean = true
)