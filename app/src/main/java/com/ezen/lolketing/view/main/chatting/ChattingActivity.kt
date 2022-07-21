package com.ezen.lolketing.view.main.chatting

import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.ezen.lolketing.BaseActivity
import com.ezen.lolketing.R
import com.ezen.lolketing.adapter.ChattingAdapter
import com.ezen.lolketing.databinding.ActivityChattingBinding
import com.ezen.lolketing.model.ChattingDTO
import com.ezen.lolketing.util.isCurrentDate
import com.ezen.lolketing.util.setTeamLogoImageView
import com.ezen.lolketing.util.toast
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChattingActivity : BaseActivity<ActivityChattingBinding>(R.layout.activity_chatting) {

    private var reference = FirebaseDatabase.getInstance()
    private var comment = ChattingDTO.Comment() // 댓글을 저장할 변수

    private lateinit var adapter : ChattingAdapter // 채팅 리사이클러의 어뎁터
    private lateinit var nickname : String // 유저의 닉네임
    lateinit var time : String  // 채팅 방의 시간
    lateinit var selectTeam : String // 선택한 팀 L or R

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initViews()

    }   // onCreate()

    /** 각종 뷰들 초기화 **/
    private fun initViews() = with(binding) {
        val team = intent.getStringExtra(TEAM) ?: ""
        val teams = team.split(":") // ':'을 기준으로 왼쪽 팀, 오른쪽 팀을 나누어서 저장
        time = intent?.getStringExtra(TIME) ?: ""
        nickname = intent.getStringExtra(NICKNAME) ?: ""
        selectTeam = intent.getStringExtra(SELECT_TEAM) ?: ""

        title = team.replace(":", " vs ")   // ex) T1:DAMWONGAMMING > T1 vs DAMWONGAMMING 으로 수정하여 표시
        layoutTop.btnBack.setOnClickListener { finish() }

        if (time.isEmpty() || nickname.isEmpty() || selectTeam.isEmpty()
            || team.isEmpty() || teams.size < 2
        ){
            toast(R.string.error_unexpected)
            finish()
            return@with
        }

        setTeamLogoImageView(imgTeam1, teams[0])
        setTeamLogoImageView(imgTeam2, teams[1])

        comment.id = nickname
        comment.team = selectTeam

        editChat.setRegisterListener {
            // 이전 날의 채팅방 입장은 가능하지만 당일 제외하고 채팅 입력 불가
            if (isCurrentDate(time)) {
                toast(R.string.guide_write_current_day)
                return@setRegisterListener
            }

            comment.message = it
            comment.timestamp = System.currentTimeMillis()
            // 채팅 내용 RealtimeDatabase 에 추가
            reference.reference.child(CHATTING_ROOM).child(time).child(COMMENTS).push().setValue(comment).addOnCompleteListener {
                editChat.setText(null)
                // 채팅 추가 한 후 스크롤 이동
                chattingRecycler.scrollToPosition(adapter.itemCount-1)
            }
        } // setOnClickListener
    }

    // 채팅방이 생성 여부 검사
    private fun checkChattingRoom(){
        Log.e("ChattingActivity", "checkChattingRoom")
        reference.reference.child(CHATTING_ROOM).orderByChild("chattingTitle").equalTo(time)
                .addListenerForSingleValueEvent(object : ValueEventListener{
                    override fun onCancelled(error : DatabaseError) {
                        Log.e("ChattingActivity", "채팅방 오류")
                    }
                    override fun onDataChange(dataSnapshot : DataSnapshot) {
                        // 채팅방이 있을 경우 기존 채팅방 사용
                        for(item in dataSnapshot.children){
                            Log.e("ChattingActivity", "기존 채팅방 사용")
                            val chattingDTO = item.getValue(ChattingDTO::class.java)!!
                            val usersMap = chattingDTO.users as MutableMap
                            usersMap[nickname] = selectTeam
                            // 들어온 유저에 추가
                            reference.reference.child(CHATTING_ROOM).child(time).child("users").updateChildren(usersMap as Map<String, Any>)
                            // 리사이클러뷰 세팅
                            setRecycler()
                            adapter.startListening()
                            return
                        }
                        // 채팅방이 없을 경우 채팅방 생성
                        if(dataSnapshot.children.count() == 0){
                            Log.e("ChattingActivity", "채팅방 생성")
                            val chattingDTO = ChattingDTO()
                            chattingDTO.chattingTitle = time
                            chattingDTO.users = mapOf(nickname to selectTeam)
                            reference.reference.child(CHATTING_ROOM).child(time).setValue(chattingDTO).addOnSuccessListener {
                                checkChattingRoom() // 함수 재호출
                            }
                        }
                    } // onDataChange()
                }) // addListenerForSingleValueEvent
    } // checkChattingRoom

    // RecyclerView 설정
    fun setRecycler(){
        Log.e("ChattingActivity", "recyclerview create")
        val query = reference.reference.child(CHATTING_ROOM).child(time).child(COMMENTS).orderByChild(TIME_STAMP)
        val options = FirebaseRecyclerOptions.Builder<ChattingDTO.Comment>()
                .setQuery(query, ChattingDTO.Comment::class.java).build()
        adapter = ChattingAdapter(options) {
            // 채팅 방 입장 및 새로운 채팅이 생겼을 때 가장 아래로 이동
            binding.chattingRecycler.scrollToPosition(adapter.itemCount-1)
        }
        binding.chattingRecycler.setHasFixedSize(true)
        binding.chattingRecycler.adapter = adapter
        binding.chattingRecycler.layoutManager = LinearLayoutManager(this)
    } // setRecycler()

    override fun onStart() {
        super.onStart()
        // 채팅방이 생성 여부 검사
        checkChattingRoom()
    }

    override fun onStop() {
        super.onStop()
        if (this::adapter.isInitialized) {
            adapter.stopListening()
            // 채팅 방에서 나가거나 페이지 이동 시에 채팅방 유저 제거
            reference.reference.child(CHATTING_ROOM).child(time).child("users/$nickname").removeValue()
        }
    }

    companion object {
        const val SELECT_TEAM = "selectTeam"
        const val NICKNAME = "nickName"
        const val TIME = "time"
        const val TEAM = "team"
        const val CHATTING_ROOM = "ChattingRoom"
        const val TIME_STAMP = "timestamp"
        const val COMMENTS = "comments"
    }
}