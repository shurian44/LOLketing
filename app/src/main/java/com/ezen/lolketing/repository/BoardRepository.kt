package com.ezen.lolketing.repository

import android.net.Uri
import com.ezen.lolketing.exception.BoardException
import com.ezen.lolketing.model.*
import com.ezen.lolketing.network.FirebaseClient
import com.ezen.lolketing.util.Constants
import com.ezen.lolketing.util.LoginException
import com.google.firebase.firestore.FieldValue
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class BoardRepository @Inject constructor(
    private val client: FirebaseClient
) {

    fun fetchBoardList(
        team: String
    ) = flow {
        emit(
            client
                .getBasicQuerySnapshot(
                    collection = Constants.BOARD,
                    field = Constants.TEAM,
                    query = team,
                    valueType = Board::class.java
                )
                .getOrThrow()
                .mapNotNull { (board, documentId) -> board.boardListItemMapper(documentId) }
        )
    }

    fun fetchBoardList(
        team: String,
        filed: String,
        query: String
    ) = flow {
        emit(
            client
                .getBasicQuerySnapshot(
                    collection = Constants.BOARD,
                    field = Constants.TEAM,
                    query = team,
                    valueType = Board::class.java
                )
                .getOrThrow()
                .filter { (board, _) ->
                    if (filed == Constants.TITLE) {
                        board.title?.contains(query) == true
                    } else {
                        board.nickname?.contains(query) == true
                    }
                }
                .mapNotNull { (board, documentId) -> board.boardListItemMapper(documentId) }
        )
    }

    fun fetchListOfMyWritings(team: String) = flow {
        val email = client.getUserEmail() ?: throw LoginException.EmptyInfo
        emit(
            client
                .getBasicQuerySnapshot(
                    collection = Constants.BOARD,
                    field = Constants.Email,
                    query = email,
                    valueType = Board::class.java
                )
                .getOrThrow()
                .filter { (board, _) -> board.team == team }
                .mapNotNull { (board, documentId) -> board.boardListItemMapper(documentId) }
        )
    }

    /** 수정을 위한 보드 정보 조회 **/
    fun fetchBoardModifyInfo(documentId: String) = flow {
        emit(
            client
                .getBasicSnapshot(
                    collection = Constants.BOARD,
                    document = documentId,
                    valueType = Board::class.java
                )
                .getOrThrow()
                ?.boardWriteInfoMapper()
                ?: throw BoardException.SearchError
        )
    }

    fun fetchEmail() = client.getUserEmail() ?: throw LoginException.EmptyUser

    /** 게시글 정보 조회 **/
    fun fetchBoardInfo(
        documentId: String
    ) = flow {
        val board = fetchBoardDetail(documentId)
        val grade = fetchWriterGrade(board.email ?: throw BoardException.SearchError)
        val commentList = fetchCommentList(documentId)

        updateBoardViews(documentId)
        emit(
            board
                .detailInfoMapper(
                    grade = grade,
                    commentList = commentList
                )
                ?: throw BoardException.SearchError
        )
    }

    /** 게시글 조회 **/
    private suspend fun fetchBoardDetail(documentId: String) = client
        .getBasicSnapshot(
            collection = Constants.BOARD,
            document = documentId,
            valueType = Board::class.java
        )
        .getOrThrow()
        ?: throw BoardException.SearchError

    /** 게시자 등급 조회 **/
    private suspend fun fetchWriterGrade(email: String) = client
        .getBasicSnapshot(
            collection = Constants.USERS,
            document = email,
            valueType = Users::class.java
        )
        .getOrThrow()
        ?.grade
        ?: throw BoardException.SearchError

    /** 댓글 조회 **/
    private suspend fun fetchCommentList(documentId: String) = client
        .getDoubleSnapshot(
            firstCollection = Constants.BOARD,
            firstDocument = documentId,
            secondCollection = Constants.COMMENTS,
            valueType = Board.Comment::class.java
        )
        .getOrThrow()
        .mapNotNull { (comment, documentId) ->
            comment.mapper(documentId)
        }

    /** 게시글 조회수 업데이트 **/
    private suspend fun updateBoardViews(documentId: String) = client
        .basicUpdateData(
            collection = Constants.BOARD,
            documentId = documentId,
            updateData = mapOf("views" to FieldValue.increment(1))
        )

    /** 좋아요 업데이트 **/
    fun updateLikes(
        documentId: String,
        info: BoardDetailInfo,
        email: String
    ) = flow {
        val updatedLike = if (info.like.contains(email)) {
            info.like - email
        } else {
            info.like + mapOf(email to true)
        }

        val board = info.mapper().copy(
            like = updatedLike
        )

        client
            .basicAddData(
                collection = Constants.BOARD,
                document = documentId,
                data = board
            )
            .onSuccess { emit("업데이트 성공") }
            .getOrThrow()
    }

    /** 게시글 업로드 **/
    fun uploadBoard(info: BoardWriteInfo) = flow {
        val email = client.getUserEmail() ?: throw LoginException.EmptyUser
        val nickname = client.getUserNickName().getOrThrow()
        val image = info.image?.let {
            uploadImage(it)
        }?.getOrThrow()
        val data = info.mapper(
            image = image,
            email = email,
            nickname = nickname
        )

        client
            .basicAddData(
                collection = Constants.BOARD,
                data = data
            )
        emit("등록 완료")
    }

    /** 게시글 수정 **/
    fun updateBoard(
        documentId: String,
        info: BoardWriteInfo,
        isImageChange: Boolean
    ) = flow {
        val updateData = mutableMapOf(
            "content" to info.content,
            "title" to info.title,
            "category" to info.category
        )

        if (isImageChange && info.image != null) {
            updateData["image"] = uploadImage(info.image).getOrThrow()
        }

        client
            .basicUpdateData(
                collection = Constants.BOARD,
                documentId = documentId,
                updateData = updateData
            )
            .getOrThrow()

        emit("업데이트 성공")
    }

    /** 이미지 업로드 **/
    private suspend fun uploadImage(uri: Uri) = runCatching {
        client
            .basicFileUpload(
                fileName = "board/${System.currentTimeMillis()}.png",
                uri = uri
            )
            .getOrThrow()
    }

    // 댓글 조회
    fun fetchComments(documentId: String) = flow {
        emit(fetchCommentList(documentId))
    }

    // 게시글 삭제
    fun deleteBoard(
        documentId: String
    ) = flow {
        client
            .basicDeleteData(
                collection = Constants.BOARD,
                documentId = documentId
            )
            .getOrThrow()

        emit("삭제 완료")
    }

    /** 게시글 신고 업데이트 **/
    fun updateBoardReport(
        documentId: String,
        reportList: List<String>
    ) = flow {
        client
            .basicUpdateData(
                collection = Constants.BOARD,
                documentId = documentId,
                updateData = mapOf("reportList" to reportList)
            )
            .getOrThrow()

        emit("신고가 완료되었습니다.")
    }

    /** 댓글 입력 **/
    fun addComment(
        documentId: String,
        comment: String,
        count: Int
    ) = flow {
        val nickname = client.getUserNickName().getOrThrow()
        val email = client.getUserEmail() ?: throw LoginException.EmptyUser

        client
            .doubleAddData(
                firstCollection = Constants.BOARD,
                firstDocument = documentId,
                secondCollection = Constants.COMMENTS,
                data = Board.Comment(
                    email = email,
                    nickname = nickname,
                    comment = comment,
                    timestamp = System.currentTimeMillis()
                )
            )
            .getOrThrow()

        updateBoardCommentCount(
            documentId = documentId,
            count = count
        ).getOrThrow()

        emit("등록 완료")
    }

    // 댓글 수 수정
    private suspend fun updateBoardCommentCount(
        documentId: String,
        count: Int
    ) = client
        .basicUpdateData(
            collection = Constants.BOARD,
            documentId = documentId,
            updateData = mapOf("commentCounts" to count)
        )

    // 댓글 신고 업데이트
    fun updateCommentReport(
        boardDocumentId: String,
        commentDocumentId: String,
        reportList: List<String>,
    ) = flow {
        client
            .doubleUpdateData(
                firstCollection = Constants.BOARD,
                firstDocument = boardDocumentId,
                secondCollection = Constants.COMMENTS,
                secondDocument = commentDocumentId,
                updateData = mapOf("reportList" to reportList)
            )
            .getOrThrow()

        emit("신고 완료")
    }
    // 댓글 삭제
    fun deleteComment(
        boardDocumentId: String,
        commentDocumentId: String,
        count: Int
    ) = flow {
        client
            .doubleDelete(
                firstCollection = Constants.BOARD,
                firstDocument = boardDocumentId,
                secondCollection = Constants.COMMENTS,
                secondDocument = commentDocumentId,
            )
            .getOrThrow()

        updateBoardCommentCount(
            documentId = boardDocumentId,
            count = count
        ).getOrThrow()

        emit("삭제 완료")
    }

}