package com.ezen.lolketing.repository

import com.ezen.lolketing.model.Board
import com.ezen.lolketing.network.FirebaseClient
import com.ezen.lolketing.util.Constants
import com.ezen.lolketing.view.main.board.adapter.BoardListAdapter
import javax.inject.Inject

class BoardRepository @Inject constructor(
    private val client: FirebaseClient
){

    suspend fun getUserNickname() : String? =
        client.getUserNickName()

    suspend fun getBoardList(
        field : String = Constants.TEAM,
        query : String,
        successListener : (List<Board.BoardItem.BoardListItem>) -> Unit,
        failureListener : () -> Unit
    ) = try {
        client.getBasicQuerySnapshot(
            collection = Constants.BOARD,
            field = field,
            query = query,
            successListener = { snapshot ->
                val list = mutableListOf<Board.BoardItem.BoardListItem>()
                snapshot.forEach{
                    list.add(
                        it.toObject(Board::class.java).also { board ->
                            board.documentId = it.id
                        }.mapper()
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
        successListener : (List<Board.BoardItem.BoardListItem>) -> Unit,
        failureListener : () -> Unit
    ) = try {
        client.getBasicQuerySnapshot(
            collection = Constants.BOARD,
            queryList = queryList,
            successListener = { snapshot ->
                val list = mutableListOf<Board.BoardItem.BoardListItem>()
                snapshot.forEach{
                    list.add(
                        it.toObject(Board::class.java).also { board ->
                            board.documentId = it.id
                        }.mapper()
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