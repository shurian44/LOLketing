package com.ezen.network.repository

import com.ezen.network.model.Board
import com.ezen.network.model.BoardDetail
import com.ezen.network.model.BoardWriteInfo
import com.ezen.network.model.Comment
import kotlinx.coroutines.flow.Flow

interface BoardRepository {
    fun insertBoard(info: BoardWriteInfo): Flow<String>

    fun fetchBoardList(
        skip: Int,
        limit: Int,
        teamId: Int
    ): Flow<List<Board>>

    fun fetchBoardDetail(boardId: Int): Flow<BoardDetail>

    fun updateBoard(info: BoardWriteInfo, isImageChanged: Boolean): Flow<String>

    fun deleteBoard(boardId: Int): Flow<String>

    fun insertComment(contents: String, boardId: Int): Flow<List<Comment>>

    fun deleteComment(commentId: Int, boardId: Int): Flow<List<Comment>>

    fun updateBoardLike(boardId: Int): Flow<Board>
}