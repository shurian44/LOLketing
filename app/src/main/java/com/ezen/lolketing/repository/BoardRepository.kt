package com.ezen.lolketing.repository

import android.net.Uri
import com.ezen.lolketing.model.Board
import com.ezen.lolketing.model.BoardItem
import com.ezen.lolketing.model.BoardWriteInfo
import com.ezen.lolketing.network.FirebaseClient
import com.ezen.lolketing.util.Constants
import javax.inject.Inject

class BoardRepository @Inject constructor(
    private val client: FirebaseClient
){

    suspend fun getUserNickname() : String? =
        client.getUserNickName()

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
                val list = mutableListOf<BoardItem.BoardListItem>()
                snapshot.forEach{
                    list.add(
                        it.toObject(Board::class.java).also { board ->
                            board.documentId = it.id
                        }.boardListItemMapper()
                    )
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

    suspend fun getBoardList(
        queryList: List<Pair<String, Any>>,
        successListener : (List<BoardItem.BoardListItem>) -> Unit,
        failureListener : () -> Unit
    ) = try {
        client.getBasicQuerySnapshot(
            collection = Constants.BOARD,
            queryList = queryList,
            successListener = { snapshot ->
                val list = mutableListOf<BoardItem.BoardListItem>()
                snapshot.forEach{
                    list.add(
                        it.toObject(Board::class.java).also { board ->
                            board.documentId = it.id
                        }.boardListItemMapper()
                    )
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
                        ?.let{
                            successListener(it)
                        }
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
                        ?.let {
                            // todo code값 수정 필요
                            it.category
                        }
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

    suspend fun uploadImage(
        uri: Uri,
        successListener: (String) -> Unit,
        failureListener: () -> Unit
    ) {
        client
            .basicFileUpload(
                fileName = "board/${System.currentTimeMillis()}.png",
                uri = uri,
                successListener = successListener,
                failureListener = failureListener
            )
    }

    suspend fun uploadBoard(
        data : Board,
        successListener : () -> Unit,
        errorListener : () -> Unit
    ) = try {
        client
            .basicAddData(
                collection = Constants.BOARD,
                data = data,
                successListener = {
                    successListener()
                },
                failureListener = {
                    errorListener()
                }
            )
    } catch (e: Exception) {
        e.printStackTrace()
        errorListener()
    }

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
}