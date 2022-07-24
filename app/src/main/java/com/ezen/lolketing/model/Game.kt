package com.ezen.lolketing.model

import com.ezen.lolketing.util.Code
import com.ezen.lolketing.util.isCurrentDate
import com.ezen.lolketing.util.isPassedDate
import com.ezen.lolketing.util.isPassedTime

data class Game(
    var date : String ?= null,
    var time : String ?= null,
    var team : String ?= null,
    var result : String ?= null,
    var status : String ?= null
) {

    fun mapper() : Ticket? {
        val date = date ?:return null
        val status = status ?: return null
        val time = time?: return null
        val isCurrentDate = isCurrentDate(date, "yyyy.MM.dd")
        val isPassedDate = isPassedDate(date, 2, "yyyy.MM.dd")
        val currentStatus = if (isCurrentDate) {
            when(isPassedTime(time)) {
                true -> Code.TICKETING_FINISH.code
                false -> status
            }
        } else if(isPassedDate) {
            when(status){
                Code.TICKETING_ON.code -> Code.TICKETING_SCHEDULE_OPEN.code
                else -> status
            }
        }else {
            status
        }

        return Ticket(
            date = date,
            time = time,
            team = team?: return null,
            status = currentStatus
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