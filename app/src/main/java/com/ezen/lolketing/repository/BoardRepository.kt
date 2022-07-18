package com.ezen.lolketing.repository

import android.net.Uri
import com.ezen.lolketing.model.*
import com.ezen.lolketing.network.FirebaseClient
import com.ezen.lolketing.util.Constants
import com.google.firebase.firestore.FieldValue
import javax.inject.Inject

class BoardRepository @Inject constructor(
    private val client: FirebaseClient
){

    suspend fun getUserNickname() : String? =
        client.getUserNickName()

    suspend fun getUserGrade(
        email : String,
        successListener: (String) -> Unit,
        failureListener: () -> Unit
    ) = try {
        client
            .getBasicSnapshot(
                collection = Constants.USERS,
                document = email,
                successListener = {
                    it.toObject(Users::class.java)?.let { userInfo ->
                        userInfo.grade?.let(successListener) ?: failureListener()
                    }
                },
                failureListener = failureListener
            )
    } catch (e: Exception) {
        e.printStackTrace()
        failureListener()
    }

    suspend fun getBoardList(
        field : String = Constants.TEAM,
        query : String,
        successListener : (List<BoardItem.BoardListItem>) -> Unit,
        failureListener : () -> Unit
    ) = try {
        client.getBasicQuerySnapshot(
            collection = Constants.BOARD,
            field = field,
            query = query,
            successListener = { snapshot ->
                val list = snapshot.mapNotNull {
                    it.toObject(Board::class.java)
                        .boardListItemMapper(it.id)
                }
                successListener(list)
            },
            failureListener = {
                failureListener()
            }
        )
    } catch (e : Exception) {
        e.printStackTrace()
        null
    }

    suspend fun getSearchBoardList(
        field: String,
        data: String,
        successListener : (List<BoardItem.BoardListItem>) -> Unit,
        failureListener : () -> Unit
    ) = try {
        client.getBasicSearchData(
            collection = Constants.BOARD,
            field = field,
            startDate = data,
            successListener = { snapshot ->
                val list = snapshot.mapNotNull {
                    it.toObject(Board::class.java)
                        .boardListItemMapper(it.id)
                }
                successListener(list)
            },
            failureListener = {
                failureListener()
            }
        )
    } catch (e : Exception) {
        e.printStackTrace()
        null
    }

    // 수정을 위한 보드 정보 조회
    suspend fun getBoardModifyInfo(
        documentId: String,
        successListener: (BoardWriteInfo) -> Unit,
        failureListener: () -> Unit
    ) = try {
        client
            .getBasicSnapshot(
                collection = Constants.BOARD,
                document = documentId,
                successListener = { snapshot ->
                    snapshot.toObject(Board::class.java)
                        ?.boardWriteInfoMapper()
                        ?.let(successListener)
                        ?: failureListener()
                },
                failureListener = failureListener
            )
    } catch (e: Exception) {
        e.printStackTrace()
        failureListener()
    }

    // 게시글을 위한 보드 조회
    suspend fun getBoardReadInfo(
        documentId: String,
        successListener: (Board) -> Unit,
        failureListener: () -> Unit
    ) = try {
        client
            .getBasicSnapshot(
                collection = Constants.BOARD,
                document = documentId,
                successListener = { snapshot ->
                    snapshot.toObject(Board::class.java)
                        ?.let(successListener)
                        ?:failureListener()
                },
                failureListener = failureListener
            )
    } catch (e: Exception) {
        e.printStackTrace()
        failureListener()
    }

    suspend fun getComments(
        documentId: String,
        successListener: (List<Board.Comment>) -> Unit,
        failureListener: () -> Unit
    ) = try {
        client
            .getDoubleSnapshot(
                firstCollection = Constants.BOARD,
                firstDocument = documentId,
                secondCollection = Constants.COMMENTS,
                successListener = { querySnapshot ->
                    val list = mutableListOf<Board.Comment>()
                    querySnapshot.forEach {
                        it.toObject(Board.Comment::class.java).let { comment ->
                            list.add(comment)
                        }
                    }
                    successListener(list)
                },
                failureListener = failureListener
            )
    } catch (e: Exception) {
        e.printStackTrace()
        failureListener()
    }

    // 이미지 업로드
    suspend fun uploadImage(
        uri: Uri,
        successListener: (String) -> Unit,
        failureListener: () -> Unit
    ): Any? = try {
        client
            .basicFileUpload(
                fileName = "board/${System.currentTimeMillis()}.png",
                uri = uri,
                successListener = successListener,
                failureListener = failureListener
            )
    } catch (e: Exception) {
        e.printStackTrace()
        failureListener()
    }

    // 조회수 업데이트
    suspend fun updateViews(
        documentId: String
    ) = try {
        client
            .basicUpdateData(
                collection = Constants.BOARD,
                documentId = documentId,
                updateData = mapOf("views" to FieldValue.increment(1)),
                successListener = {},
                failureListener = {}
            )
    } catch (e: Exception) {
        e.printStackTrace()
    }

    // 좋아요 업데이트
    suspend fun updateLikes(
        documentId: String,
        board: Board,
        successListener: () -> Unit,
        failureListener: () -> Unit
    ) = try {
        // firebase update 사용 불가, like - key 의 값에 넣은 이메일의 . 때문에 오류 발생
        client
            .basicAddData(
                collection = Constants.BOARD,
                document = documentId,
                data = board,
                successListener = successListener,
                failureListener = failureListener
            )
    } catch (e: Exception) {
        e.printStackTrace()
        failureListener()
    }

    // 게시글 업로드
    suspend fun uploadBoard(
        data : Board,
        successListener : (String) -> Unit,
        errorListener : () -> Unit
    ) = try {
        client
            .basicAddData(
                collection = Constants.BOARD,
                data = data,
                successListener = {
                    successListener(it.id)
                },
                failureListener = {
                    errorListener()
                }
            )
    } catch (e: Exception) {
        e.printStackTrace()
        errorListener()
    }

    // 게시글 수정
    suspend fun updateBoard(
        documentId: String,
        updateData: Map<String, Any>,
        successListener: () -> Unit,
        failureListener : () -> Unit
    ) = try {
        client.basicUpdateData(
            collection = Constants.BOARD,
            documentId = documentId,
            updateData = updateData,
            successListener = successListener,
            failureListener = failureListener
        )
    } catch (e : Exception) {
        e.printStackTrace()
    }

    // 댓글 조회
    suspend fun getCommentsList(
        documentId: String,
        successListener: (List<CommentItem>) -> Unit,
        failureListener: () -> Unit
    ) = try {
        client
            .getDoubleSnapshot(
                firstCollection = Constants.BOARD,
                firstDocument = documentId,
                secondCollection = Constants.COMMENTS,
                successListener = { snapshot ->
                    val result = snapshot.mapNotNull {
                        it.toObject(Board.Comment::class.java)
                            .mapper(it.id)
                    }
                    successListener(result)
                },
                failureListener = failureListener
            )
    } catch (e: Exception) {
        e.printStackTrace()
        failureListener()
    }

    // 게시글 삭제
    suspend fun deleteBoard(
        documentId: String,
        successListener: () -> Unit,
        failureListener : () -> Unit
    ) = try {
        client.basicDeleteData(
            collection = Constants.BOARD,
            documentId = documentId,
            successListener = {
                successListener()
            },
            failureListener = {
                failureListener()
            }
        )
    } catch (e: Exception) {
        e.printStackTrace()
    }

    // 게시글 신고 업데이트
    suspend fun updateBoardReport(
        documentId: String,
        reportList: List<String>,
        successListener: () -> Unit,
        failureListener: () -> Unit
    ) = try {
        client
            .basicUpdateData(
                collection = Constants.BOARD,
                documentId = documentId,
                updateData = mapOf("reportList" to reportList),
                successListener = successListener,
                failureListener = failureListener
            )
    } catch (e: Exception) {
        e.printStackTrace()
        failureListener()
    }

    // 댓글 입력
    suspend fun addComment(
        documentId: String,
        comment: String,
        successListener: () -> Unit,
        failureListener: () -> Unit
    ) = try {
        val email = client.getUserEmail() ?: throw Exception("email is null")
        val nickname = getUserNickname() ?: throw Exception("nickname is null")

        val commentItem = Board.Comment(
            email = email,
            nickname = nickname,
            timestamp = System.currentTimeMillis(),
            comment = comment
        )

        client
            .doubleAddData(
                firstCollection = Constants.BOARD,
                firstDocument = documentId,
                secondCollection = Constants.COMMENTS,
                data = commentItem,
                successListener = successListener,
                failureListener = failureListener
            )
    } catch (e: Exception) {
        e.printStackTrace()
        failureListener()
    }

    // 댓글 수 수정
    suspend fun updateBoardCommentCount(
        documentId: String,
        count: Int
    ) = try {
        client
            .basicUpdateData(
                collection = Constants.BOARD,
                documentId = documentId,
                updateData = mapOf("commentCounts" to count),
                successListener = {},
                failureListener = {}
            )
    } catch (e: Exception) {
        e.printStackTrace()
    }

    // 댓글 신고 업데이트
    suspend fun updateCommentReport(
        boardDocumentId: String,
        commentDocumentId: String,
        reportList: List<String>,
        successListener: () -> Unit
    ) = try {
        client
            .doubleUpdateData(
                firstCollection = Constants.BOARD,
                firstDocument = boardDocumentId,
                secondCollection = Constants.COMMENTS,
                secondDocument = commentDocumentId,
                updateData = mapOf("reportList" to reportList),
                successListener = successListener,
                failureListener = {}
            )
    } catch (e: Exception) {
        e.printStackTrace()
    }

    // 댓글 삭제
    suspend fun deleteComment(
        boardDocumentId: String,
        commentDocumentId: String,
        successListener: () -> Unit,
        failureListener: () -> Unit
     ) = try {
        client
            .doubleDelete(
                firstCollection = Constants.BOARD,
                firstDocument = boardDocumentId,
                secondCollection = Constants.COMMENTS,
                secondDocument = commentDocumentId,
                successListener = successListener,
                failureListener = failureListener
            )
    } catch (e: Exception) {
        e.printStackTrace()
        failureListener()
    }

}