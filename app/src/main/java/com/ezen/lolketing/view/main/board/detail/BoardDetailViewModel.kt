package com.ezen.lolketing.view.main.board.detail

import androidx.lifecycle.viewModelScope
import com.ezen.lolketing.BaseViewModel
import com.ezen.lolketing.model.Board
import com.ezen.lolketing.repository.BoardRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BoardDetailViewModel @Inject constructor(
    private val repository: BoardRepository
) : BaseViewModel<BoardDetailViewModel.Event>(){

    fun getBoard(
        documentId : String
    ) = viewModelScope.launch {
        repository.getBoardReadInfo(
            documentId = documentId,
            successListener = {
                event(Event.BoardInfoSuccess(it))
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
        object Failure : Event()
    }

}