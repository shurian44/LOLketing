package com.ezen.lolketing.model

import android.os.Parcelable
import com.ezen.lolketing.R
import com.ezen.lolketing.util.Code
import com.ezen.lolketing.util.Team
import com.ezen.lolketing.util.getTeamLogoImageRes
import com.ezen.lolketing.util.getRandomGame
import com.ezen.lolketing.util.isCurrentDate
import com.ezen.lolketing.util.isPassedDate
import com.ezen.lolketing.util.isPassedTime
import kotlinx.parcelize.Parcelize

data class Game(
    var date: String? = null,
    var time: String? = null,
    var team: String? = null,
    var result: String? = null,
    var status: String? = null
) {

    fun mapper(): Ticket? {
        val date = date ?: return null
        val status = status ?: return null
        val time = time ?: return null
        val isCurrentDate = isCurrentDate(date, "yyyy.MM.dd")
        val isPassedDate = isPassedDate(date, 2, "yyyy.MM.dd")
        val currentStatus = if (isCurrentDate) {
            when (isPassedTime(time)) {
                true -> Code.TICKETING_FINISH.code
                false -> status
            }
        } else if (isPassedDate) {
            when (status) {
                Code.TICKETING_ON.code -> Code.TICKETING_SCHEDULE_OPEN.code
                else -> status
            }
        } else {
            status
        }

        return Ticket(
            date = date,
            time = time,
            team = team ?: return null,
            status = currentStatus
        )
    }

    fun mapperChattingInfo(): ChattingInfo? {
        val teamList = team?.split(":") ?: return null
        return runCatching {
            ChattingInfo(
                documentId = getDocumentId(),
                time = time ?: return null,
                leftTeam = teamList[0],
                rightTeam = teamList[1]
            )
        }.getOrNull()
    }

    fun getDocumentId() = "$date $time"

    companion object {
        fun createRandomGame(date: String, time: String) = Game(
            date = date,
            status = Code.TICKETING_ON.code,
            team = getRandomGame(),
            time = time
        )
    }
}

data class Ticket(
    var date: String,
    var time: String,
    var team: String,
    var status: String
) {
    fun getTicketTime() = "$date $time"

    fun getBackgroundColorRes() = when (status) {
        Code.TICKETING_ON.code -> R.color.sub_color
        Code.TICKETING_SOLD_OUT.code -> R.color.orange
        Code.TICKETING_FINISH.code -> R.color.yellow
        else -> R.color.yellow
    }

    fun getStatusText() = when (status) {
        Code.TICKETING_ON.code -> Code.TICKETING_ON.codeName
        Code.TICKETING_SOLD_OUT.code -> Code.TICKETING_SOLD_OUT.codeName
        Code.TICKETING_FINISH.code -> Code.TICKETING_FINISH.codeName
        else -> Code.TICKETING_SCHEDULE_OPEN.codeName
    }

    fun getMessage() = when(status) {
        Code.TICKETING_ON.code -> "매진되었습니다"
        Code.TICKETING_SOLD_OUT.code -> "경기가 종료되었습니다"
        Code.TICKETING_FINISH.code -> "오픈 예정입니다"
        else -> ""
    }

    private fun getImageRes(item: String): Int {
        return Team
            .values()
            .find { it.teamName.uppercase() == item.uppercase() }
            ?.image
            ?: Team.T1.image
    }

    fun getFirstImageRes() = runCatching {
        getImageRes(team.split(":")[0])
    }.getOrElse { Team.T1.image }

    fun getSecondImageRes() = runCatching {
        getImageRes(team.split(":")[1])
    }.getOrElse { Team.T1.image }
}

@Parcelize
data class ChattingInfo(
    val documentId: String = "",
    val time: String = "",
    val leftTeam: String = "",
    val rightTeam: String = "",
    val selectTeam: String = ""
) : Parcelable {
    fun getLeftTeamImageRes() = getTeamLogoImageRes(leftTeam)

    fun getRightTeamImageRes() = getTeamLogoImageRes(rightTeam)

    fun getTitle() = "$leftTeam vs $rightTeam"
}