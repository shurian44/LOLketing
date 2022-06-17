package com.ezen.lolketing.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Board(
    val email : String?= null,
    val nickname: String?= null,
    val team : String?= null,
    val timestamp: Long?= null,
    var category: String?= null,
    val title : String?= null,
    val content: String?= null,
    val image : String?= null,
    val commentCounts : Long?= null,
    val like : MutableMap<String, Boolean>?= null,
    var likeCounts : Long?= null,
    val views : Long?= null,
    var documentId : String?= null
) : Parcelable {
    @Parcelize
    data class Comment(
        val email : String?= null,
        val nickname : String?= null,
        val timestamp : Long?= null,
        val comment : String?= null
    ) : Parcelable

    fun boardListItemMapper() = BoardItem.BoardListItem(
        email = email,
        nickname = nickname,
        team = team,
        title = title,
        timestamp = timestamp,
        category = category,
        commentCounts = commentCounts,
        views = views,
        documentId = documentId
    )

    fun boardWriteInfoMapper() = BoardWriteInfo(
        category = category,
        title = title,
        content = content,
        image = image,
        email = email
    )

}

data class BoardWriteInfo(
    val category: String?= null,
    val title: String?= null,
    val content: String?= null,
    val image: String?= null,
    val email: String?= null
)

open class BoardItem(val type: Int) {
    data class TeamImage(
        val team: String
    ) : BoardItem(type = TYPE_TEAM_IMAGE)

    data class BoardListItem(
        val email: String?= null,
        val nickname: String? = null,
        val team: String?= null,
        val timestamp: Long?= null,
        val category: String?= null,
        val title: String?= null,
        val commentCounts: Long?= null,
        val views: Long?= null,
        val documentId: String?= null
    ) : BoardItem(TYPE_TEAM_BOARD)

    companion object {
        const val TYPE_TEAM_IMAGE = 1
        const val TYPE_TEAM_BOARD = 2
    }
}

