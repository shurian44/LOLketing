package com.ezen.lolketing.view.main.board.detail

import android.content.SharedPreferences
import androidx.lifecycle.viewModelScope
import com.ezen.lolketing.BaseViewModel
import com.ezen.lolketing.model.Board
import com.ezen.lolketing.repository.BoardRepository
import com.ezen.lolketing.util.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BoardDetailViewModel @Inject constructor(
    private val repository: BoardRepository,
    private val pref : SharedPreferences
) : BaseViewModel<BoardDetailViewModel.Event>(){

    private lateinit var board: Board

    fun getBoard(
        documentId : String
    ) = viewModelScope.launch {
        repository.getBoardReadInfo(
            documentId = documentId,
            successListener = {
                board = it
                event(Event.BoardInfoSuccess(it))
            },
            failureListener = {
                event(Event.Failure)
            }
        )
    }

    fun getUserGrade() = viewModelScope.launch {
        val email = board.email ?: return@launch
        repository
            .getUserGrade(
                email = email,
                successListener = {
                    event(Event.UserGrade(it))
                },
                failureListener = {}
            )
    }

    fun updateViews(
        documentId: String
    ) = viewModelScope.launch {
        val userId = pref.getString(Constants.ID, "")
        if (userId == board.email) return@launch

        repository
            .updateViews(
                documentId = documentId
            )
    }

    fun updateLike(
        documentId: String,
    ) = viewModelScope.launch {
        val userId = pref.getString(Constants.ID, "") ?: return@launch
        val isLike = board.like?.get(userId) ?: false
        board.like?.set(userId, isLike.not())
        board.likeCounts = board.like?.filter { it.value }?.size?.toLong()

        repository
            .updateLikes(
                documentId = documentId,
                board = board,
                successListener = {
                    event(Event.LikeUpdateSuccess(isLike.not(), board))
                },
                failureListener = {
                    error(Event.Failure)
                }
            )

    }

    fun getCommentsList(
        documentId: String
    ) = viewModelScope.launch {
        repository
            .getCommentsList(
                documentId = documentId,
                successListener = {
                    event(Event.CommentsListSuccess(it))
                },
                failureListener = {
                    event(Event.Failure)
                }
            )
    }

    //
    private fun getCommentList() {
//        // 댓글 시간 순으로 내림차순 정렬
//        query = firestore.collection("Board").document(documentID!!).collection("Comment")
//            .orderBy("timestamp", Query.Direction.DESCENDING)
//        val options: FirestoreRecyclerOptions<Comment> =
//            FirestoreRecyclerOptions.Builder<Comment>()
//                .setQuery(query!!, Comment::class.java)
//                .build()
//        adapter = CommentAdapter(options, documentID)
//        binding.recyclerViewComment.adapter = adapter
//
//        // 조회수 증가
//        firestore.collection("Board").document(intent.getStringExtra("documentID")!!)
//            .update("views", FieldValue.increment(1))
    }


    sealed class Event {
        data class BoardInfoSuccess(
            val board: Board
        ) : Event()
        data class UserGrade(
            val grade: String
        ) : Event()
        data class CommentsListSuccess(
            val comments : List<Board.Comment>
        ) : Event()
        data class LikeUpdateSuccess(
            val like: Boolean,
            val board: Board
        ) : Event()
        object LikeUpdateFailure : Event()
        object Failure : Event()
    }

}