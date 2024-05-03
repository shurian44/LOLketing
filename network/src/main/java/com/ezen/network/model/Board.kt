package com.ezen.network.model

import android.net.Uri
import com.google.gson.annotations.SerializedName

data class Board(
    val id: Int,
    val contents: String,
    val image: String,
    val timestamp: String,
    @SerializedName("name")
    val category: String,
    val userId: Int = 0,
    val nickname: String,
    val likeCount: Int,
    val isLike: Boolean,
    val commentCount: Int,
    val isAuthor: Boolean
)

data class BoardWrite(
    val contents: String,
    val image: String,
    val teamId: Int,
    val userId: Int,
    val boardId: Int
)

data class BoardWriteInfo(
    var contents: String,
    val image: Uri?,
    val teamId: Int,
    val teamName: String,
    val boardId: Int
) {
    fun checkValidation() = when {
        contents.isEmpty() -> throw Exception("내용을 입력해 주세요.")
        teamId == 0 -> throw Exception("팀을 선택해 주세요.")
        else -> true
    }

    fun toModify(image: String, userId: Int) = BoardModify(
        id = boardId,
        contents = contents,
        image = image,
        timestamp = "2024-04-12T10:26:10.925Z",
        teamId = teamId,
        userId = userId
    )

    companion object {
        fun init() = BoardWriteInfo(
            contents = "",
            image = null,
            teamId = 0,
            teamName = "",
            boardId = 0
        )
    }
}

data class BoardSearch(
    val skip: Int,
    val limit: Int,
    val userId: Int
)

data class BoardIdInfoParam(
    val boardId: Int,
    val userId: Int,
)

data class BoardDetail(
    val id: Int,
    val contents: String,
    val image: String,
    val timestamp: String,
    @SerializedName("name")
    val category: String,
    val nickname: String,
    val likeCount: Int,
    val isLike: Boolean,
    val commentList: List<Comment>,
    val isAuthor: Boolean
) {
    fun toBoardWriteInfo() = BoardWriteInfo(
        contents = contents,
        image = if (image.trim().isEmpty()) null else Uri.parse(image),
        teamId = Team.getTeamId(category),
        teamName = category,
        boardId = id
    )

    companion object {
        fun init() = BoardDetail(
            id = 0,
            contents = "",
            image = "",
            timestamp = "",
            category = "",
            nickname = "",
            likeCount = 0,
            isLike = false,
            commentList = listOf(),
            isAuthor = false
        )
    }
}

data class BoardModify(
    val id: Int,
    val contents: String,
    val image: String,
    val timestamp: String,
    val teamId: Int,
    val userId: Int
)

data class Comment(
    val commentId: Int,
    val contents: String,
    val timestamp: String,
    val userId: Int,
    val nickname: String,
    val boardId: Int,
    val isAuthor: Boolean
) {
    companion object {
        fun createCommentModifyInfo(
            commentId: Int,
            contents: String,
            userId: Int,
            boardId: Int
        ) = Comment(
            commentId = commentId,
            contents = contents,
            timestamp = "",
            userId = userId,
            nickname = "",
            boardId = boardId,
            isAuthor = false
        )
    }
}

data class CommentWrite(
    val contents: String,
    val userId: Int,
    val boardId: Int
)

data class CommentDelete(
    val commentId: Int,
    val boardId: Int,
    val userId: Int
)

data class LikeUpdate(
    val boardId: Int,
    val userId: Int
)