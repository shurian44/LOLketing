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

    suspend fun getUserNickname(): String? =
        client.getUserNickName().getOrNull()

    fun fetchBoardList(
        query: String
    ) = flow {
        emit(
            client
                .getBasicQuerySnapshot(
                    collection = Constants.BOARD,
                    field = Constants.TEAM,
                    query = query,
                    valueType = Board::class.java
                )
                .getOrThrow()
                .mapNotNull { (board, documentId) -> board.boardListItemMapper(documentId) }
        )
    }

    fun fetchListOfMyWritings() = flow {
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
                .mapNotNull { (board, documentId) -> board.boardListItemMapper(documentId) }
        )
    }

    suspend fun getSearchBoardList(
        field: String,
        data: String,
        team: String,
        successListener: (List<BoardItem.BoardListItem>) -> Unit,
        failureListener: () -> Unit
    ) = try {
//        client.getBasicQuerySnapshot(
//            collection = Constants.BOARD,
//            field = Constants.TEAM,
//            query = team,
//            successListener = { snapshot ->
//                val list = snapshot.mapNotNull {
//                    it.toObject(Board::class.java)
//                        .boardListItemMapper(it.id)
//                }.filter {
//                    if (field == SearchActivity.TITLE) {
//                        it.title.contains(data)
//                    } else {
//                        it.nickname.contains(data)
//                    }
//                }
//                successListener(list)
//            },
//            failureListener = {
//                failureListener()
//            }
//        )
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }

    // 수정을 위한 보드 정보 조회
    suspend fun getBoardModifyInfo(
        documentId: String,
        successListener: (BoardWriteInfo) -> Unit,
        failureListener: () -> Unit
    ) = try {
//        client
//            .getBasicSnapshot(
//                collection = Constants.BOARD,
//                document = documentId,
//                successListener = { snapshot ->
//                    snapshot.toObject(Board::class.java)
//                        ?.boardWriteInfoMapper()
//                        ?.let(successListener)
//                        ?: failureListener()
//                },
//                failureListener = failureListener
//            )
    } catch (e: Exception) {
        e.printStackTrace()
        failureListener()
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

    // 이미지 업로드
    suspend fun uploadImage(
        uri: Uri,
        successListener: (String) -> Unit,
        failureListener: () -> Unit
    ): Any? = try {
//        client
//            .basicFileUpload(
//                fileName = "board/${System.currentTimeMillis()}.png",
//                uri = uri,
//                successListener = successListener,
//                failureListener = failureListener
//            )
    } catch (e: Exception) {
        e.printStackTrace()
        failureListener()
    }


    // 좋아요 업데이트
    suspend fun updateLikes(
        documentId: String,
        board: Board,
        successListener: () -> Unit,
        failureListener: () -> Unit
    ) = try {
        // firebase update 사용 불가, like - key 의 값에 넣은 이메일의 . 때문에 오류 발생
//        client
//            .basicAddData(
//                collection = Constants.BOARD,
//                document = documentId,
//                data = board,
//                successListener = successListener,
//                failureListener = failureListener
//            )
    } catch (e: Exception) {
        e.printStackTrace()
        failureListener()
    }

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

    // 게시글 업로드
    suspend fun uploadBoard(
        data: Board,
        successListener: (String) -> Unit,
        errorListener: () -> Unit
    ) = try {
//        client
//            .basicAddData(
//                collection = Constants.BOARD,
//                data = data,
//                successListener = {
//                    successListener(it.id)
//                },
//                failureListener = {
//                    errorListener()
//                }
//            )
    } catch (e: Exception) {
        e.printStackTrace()
        errorListener()
    }

    // 게시글 수정
    suspend fun updateBoard(
        documentId: String,
        updateData: Map<String, Any>,
        successListener: () -> Unit,
        failureListener: () -> Unit
    ) = try {
//        client.basicUpdateData(
//            collection = Constants.BOARD,
//            documentId = documentId,
//            updateData = updateData,
//            successListener = successListener,
//            failureListener = failureListener
//        )
    } catch (e: Exception) {
        e.printStackTrace()
    }

    // 댓글 조회
    fun fetchComments(documentId: String) = flow {
        emit(fetchCommentList(documentId))
    }

    // 게시글 삭제
    suspend fun deleteBoard(
        documentId: String,
        successListener: () -> Unit,
        failureListener: () -> Unit
    ) = try {
//        client.basicDeleteData(
//            collection = Constants.BOARD,
//            documentId = documentId,
//            successListener = {
//                successListener()
//            },
//            failureListener = {
//                failureListener()
//            }
//        )
    } catch (e: Exception) {
        e.printStackTrace()
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
        val nickname = getUserNickname() ?: throw LoginException.EmptyUser
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