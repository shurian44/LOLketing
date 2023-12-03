package com.ezen.lolketing.model

import android.os.Parcelable
import com.ezen.lolketing.util.Grade
import com.ezen.lolketing.util.findCodeName
import com.ezen.lolketing.util.getGradeImageRes
import kotlinx.parcelize.Parcelize

@Parcelize
data class Board(
    val email: String? = null,
    val nickname: String? = null,
    val team: String? = null,
    val timestamp: Long? = null,
    var category: String? = null,
    val title: String? = null,
    val content: String? = null,
    val image: String? = null,
    val commentCounts: Long? = null,
    val like: Map<String, Boolean>? = null,
    var likeCounts: Long? = null,
    val views: Long? = null,
    val reportList: List<String>? = null
) : Parcelable {
    @Parcelize
    data class Comment(
        val email: String? = null,
        val nickname: String? = null,
        val timestamp: Long? = null,
        var comment: String = "",
        val reportList: List<String>? = null
    ) : Parcelable

    fun boardListItemMapper(documentId: String): BoardItem.BoardListItem? {
        return BoardItem.BoardListItem(
            email = email ?: return null,
            nickname = nickname ?: return null,
            team = team ?: return null,
            title = title ?: return null,
            timestamp = timestamp ?: return null,
            category = category ?: return null,
            commentCounts = commentCounts ?: 0,
            views = views ?: 0,
            documentId = documentId
        )
    }

    fun boardWriteInfoMapper(): BoardWriteInfo? {
        return BoardWriteInfo(
            category = category ?: return null,
            title = title ?: return null,
            content = content ?: return null,
            image = image,
            email = email ?: return null
        )
    }

    fun detailInfoMapper(
        grade: String,
        commentList: List<CommentItem>
    ): BoardDetailInfo? {
        return BoardDetailInfo(
            email = email ?: return null,
            nickname = nickname ?: return null,
            team = team ?: return null,
            timestamp = timestamp ?: return null,
            category = category ?: return null,
            title = title ?: return null,
            content = content ?: return null,
            image = image,
            commentCounts = commentCounts ?: 0,
            like = like ?: mutableMapOf(),
            likeCounts = likeCounts ?: 0,
            views = views ?: 0,
            reportList = reportList ?: emptyList(),
            commentList = commentList,
            grade = grade
        )
    }

}

data class BoardWriteInfo(
    val category: String,
    val title: String,
    val content: String,
    val image: String? = null,
    val email: String
)

sealed class BoardItem(val type: Int) {
    data class TeamImage(
        val team: String
    ) : BoardItem(type = TYPE_TEAM_IMAGE)

    data class BoardListItem(
        val email: String,
        val nickname: String,
        val team: String,
        val timestamp: Long,
        val category: String,
        val title: String,
        val commentCounts: Long,
        val views: Long,
        val documentId: String
    ) : BoardItem(TYPE_TEAM_BOARD)

    companion object {
        const val TYPE_TEAM_IMAGE = 1
        const val TYPE_TEAM_BOARD = 2
    }
}

data class CommentItem(
    val email: String,
    val nickname: String,
    val timestamp: Long,
    val comment: String,
    var documentId: String,
    val reportList: List<String>
)

fun Board.Comment.mapper(documentId: String): CommentItem? {
    return CommentItem(
        email = email ?: return null,
        nickname = nickname ?: return null,
        timestamp = timestamp ?: return null,
        comment = comment,
        documentId = documentId,
        reportList = emptyList()
    )
}

data class BoardDetailInfo(
    val email: String,
    val nickname: String,
    val team: String,
    val timestamp: Long,
    var category: String,
    val title: String,
    val content: String,
    val image: String? = null,
    val commentCounts: Long,
    val like: Map<String, Boolean>,
    var likeCounts: Long,
    val views: Long,
    val reportList: List<String>,
    val commentList: List<CommentItem>,
    val grade: String
) {
    fun getGradeRes() = getGradeImageRes(grade)

    fun getCategoryName() = findCodeName(category)

    fun getIsLike(id: String) = like.containsKey(id)

    fun mapper() = Board(
        email = email,
        nickname = nickname,
        team = team,
        timestamp = timestamp,
        category = category,
        title = title,
        content = content,
        image = image,
        commentCounts = commentCounts,
        like = like,
        likeCounts = likeCounts,
        views = views
    )

    companion object {
        fun create() = BoardDetailInfo(
            email = "",
            nickname = "",
            team = "",
            timestamp = 0,
            category = "",
            title = "",
            content = "",
            image = null,
            commentCounts = 0,
            like = mutableMapOf(),
            likeCounts = 0,
            views = 0,
            reportList = listOf(),
            commentList = listOf(),
            grade = Grade.BRONZE.gradeName
        )
    }
}