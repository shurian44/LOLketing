package com.ezen.lolketing.view.main.board.write

import androidx.lifecycle.viewModelScope
import com.ezen.lolketing.BaseViewModel
import com.ezen.lolketing.model.Board
import com.ezen.lolketing.repository.BoardRepository
import com.ezen.lolketing.view.main.board.BoardListViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BoardWriteViewModel @Inject constructor(
    private val repository: BoardRepository
) : BaseViewModel<BoardWriteViewModel.Event>() {

    // 유저 닉네임 표시
    fun getUserNickName() = viewModelScope.launch {
        repository.getUserNickname()?.let {
            event(Event.UserNickName(nickName = it))
        } ?: error("회원 정보를 찾지 못했습니다.")
    }

    fun updateBoard(documentId: String, updateData: Map<String, Any>) = viewModelScope.launch {

        repository.updateBoard(
            documentId = documentId,
            updateData = updateData,
            successListener = {
                event(Event.UpdateSuccess)
            },
            failureListener = {
                error("게시글 수정 중 오류가 발생하였습니다.")
            }
        )
    }

    fun uploadBoard(board: Board?) = viewModelScope.launch {
        board?.let {
            repository.uploadBoard(
                it,
                successListener = {
                    event(Event.UploadSuccess)
                },
                errorListener = {
                    error("파일 업로드 과정 중 오류가 발생하였습니다.")
                }
            )
        } ?: error("파일 업로드 과정 중 오류가 발생하였습니다.")
    }

    private fun error(message : String) {
        event(Event.Error(message))
    }

//    // 상태가 글 수정일 시 기존 시간 불러오기
//        if (statement != null && (statement == "modify")) {
//        } else {
//        }

    //        val storageRef = storage.reference.child("board/$filename")
//        // 업로드 진행 Dialog
//        progressDialog = ProgressDialog(this)
//        progressDialog!!.setTitle("업로드중...")
//        progressDialog!!.show()
//
//        // 파일명 지정
//        val formatter = SimpleDateFormat("yyyyMMdd_HHmmss")
//        val now = Date()
//        val filename = formatter.format(now) + ".png"
//        //storage 주소와 폴더 파일명을 지정
//        val storageRef = storage.reference.child("board/$filename")
//        storageRef.putFile((filePath)!!) //진행중
//            .addOnProgressListener(object : OnProgressListener<UploadTask.TaskSnapshot> {
//                override fun onProgress(taskSnapshot: UploadTask.TaskSnapshot) {
//                    val progress =
//                        ((100 * taskSnapshot.bytesTransferred) / taskSnapshot.totalByteCount).toDouble()
//                    // 다이얼로그에 진행률을 퍼센트로 출력
//                    progressDialog!!.setMessage("Uploaded " + (progress.toInt()) + "% ...")
//                }
//            }) //성공 시
//            .addOnSuccessListener(object : OnSuccessListener<UploadTask.TaskSnapshot?> {
//                override fun onSuccess(taskSnapshot: UploadTask.TaskSnapshot?) {
//                    progressDialog!!.dismiss() // 업로드 진행 Dialog 상자 닫기
//                    Toast.makeText(applicationContext, "업로드 완료!", Toast.LENGTH_SHORT).show()
//                    storageRef.downloadUrl.addOnCompleteListener(object : OnCompleteListener<Uri> {
//                        override fun onComplete(task: Task<Uri>) {
//                            uploadBoard(task.result.toString())
//                        }
//                    })
//                }
//            }) //실패 시
//            .addOnFailureListener(object : OnFailureListener {
//                override fun onFailure(e: Exception) {
//                    progressDialog!!.dismiss()
//                    Toast.makeText(applicationContext, "업로드 실패!", Toast.LENGTH_SHORT).show()
//                }
//            })

    sealed class Event {
        data class UserNickName(val nickName: String) : Event()
        data class Error(val msg: String) : Event()
        object UploadSuccess : Event()
        object UpdateSuccess : Event()
    }

}