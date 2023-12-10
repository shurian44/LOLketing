package com.ezen.lolketing.model

data class Seat(
    var seatNum: String = "",
    var reserveId: String?= null,
) {
    fun mapper(documentId: String, hall: String) = SeatItem(
        seatNum = seatNum.replace("$hall ", ""),
        enabled = reserveId.isNullOrEmpty(),
        documentId = documentId
    )
}

data class SeatItem(
    val seatNum : String = "",
    val documentId: String = "",
    var checked: Boolean = false,
    val enabled : Boolean = true
)