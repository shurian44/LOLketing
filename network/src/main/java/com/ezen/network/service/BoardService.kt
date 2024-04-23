package com.ezen.network.service

import com.ezen.network.model.Board
import com.ezen.network.model.BoardDetail
import com.ezen.network.model.BoardIdInfoParam
import com.ezen.network.model.BoardModify
import com.ezen.network.model.BoardSearch
import com.ezen.network.model.BoardWrite
import com.ezen.network.model.Comment
import com.ezen.network.model.CommentDelete
import com.ezen.network.model.CommentWrite
import com.ezen.network.model.LikeUpdate
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface BoardService {
    @POST("/board/insert/board")
    suspend fun insertBoard(@Body item: BoardWrite): Response<String>

    @POST("/board/select/boards")
    suspend fun fetchBoardList(@Body item: BoardSearch): Response<List<Board>>

    @POST("/board/select/boardDetail")
    suspend fun fetchBoardDetail(@Body item: BoardIdInfoParam): Response<BoardDetail>

    @POST("/board/update/board")
    suspend fun updateBoard(@Body item: BoardModify): Response<String>

    @POST("/board/delete/board")
    suspend fun deleteBoard(@Body item: BoardIdInfoParam): Response<String>

    @POST("/board/insert/comment")
    suspend fun insertComment(@Body item: CommentWrite): Response<List<Comment>>

    @POST("/board/update/comment")
    suspend fun updateComment(@Body item: Comment): Response<String>

    @POST("/board/delete/comment")
    suspend fun deleteComment(@Body item: CommentDelete): Response<List<Comment>>

    @POST("/board/update/boardLike")
    suspend fun updateBoardLike(@Body item: LikeUpdate): Response<Board>
}