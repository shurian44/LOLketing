package com.ezen.lolketing.view.main.chatting

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.ezen.lolketing.BaseViewModel
import com.ezen.lolketing.model.Game
import com.ezen.lolketing.repository.ChattingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChattingListViewModel @Inject constructor(
    private val repository: ChattingRepository
): BaseViewModel<ChattingListViewModel.Event>() {
    // todo firebase 한번에 하기
//            // 유저의 닉네임 가져오기
//        firestore.collection("Users").document(auth.currentUser?.email!!).get().addOnSuccessListener {
//            var user = it.toObject(Users::class.java)!!
//            nickName = user.nickname!!
//        }
//        // ProgressDialog 설정
////        dialog = progressDialog(message = "잠시만 기다려 주세요...")
//        // 게임의 데이터 가져오기
//        firestore.collection("Game").get().addOnSuccessListener {
//            games = it.toObjects(GameDTO::class.java)
//            // 초기 UI 세팅
////            dialog.show()
//            setViews(true, Date())
////            dialog.dismiss()
//        }

    fun getUserNickName() = viewModelScope.launch {
        val nickName = repository.getUserNickName()
        nickName?.let {
            Log.e("+++++", "nickname : $it")
            event(Event.UserNickName(it))
        } ?: error("보")
    }

    fun getGameData(startDate: String = "2020.02.05") = viewModelScope.launch {
        repository.getGameData(
            startDate = startDate,
            successListener = { snapshot ->
                val list = mutableListOf<Game>()
                snapshot.forEach {
                    list.add(it.toObject(Game::class.java))
                }
                event(Event.GameData(list))
            },
            failureListener = {
                error("경기 정보 조회를 실패하였습니다.")
            }
        )
    }

    private fun error(msg: String) {
        event(Event.Error(msg))
    }

    sealed class Event {
        data class UserNickName(val nickName: String) : Event()
        data class GameData(val list: List<Game>) : Event()
        data class Error(val msg: String) : Event()
    }

}