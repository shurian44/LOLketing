package com.ezen.lolketing.view.main.board.detail

import com.ezen.lolketing.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BoardDetailViewModel @Inject constructor(

) : BaseViewModel<BoardDetailViewModel.Event>(){

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


    sealed class Event() {

    }

}