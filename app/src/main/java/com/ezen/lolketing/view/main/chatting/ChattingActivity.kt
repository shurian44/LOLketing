package com.ezen.lolketing.view.main.chatting

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import com.ezen.lolketing.BaseActivity
import com.ezen.lolketing.view.main.MainActivity
import com.ezen.lolketing.R
import com.ezen.lolketing.adapter.ChattingAdapter
import com.ezen.lolketing.databinding.ActivityChattingBinding
import com.ezen.lolketing.model.ChattingDTO
import com.ezen.lolketing.util.*
import com.ezen.lolketing.view.login.LoginActivity
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class ChattingActivity : BaseActivity<ActivityChattingBinding>(R.layout.activity_chatting), ChattingAdapter.moveScrollListener {

    @Inject lateinit var auth : FirebaseAuth
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

    private fun initViews() = with(binding) {
        time = intent?.getStringExtra(TIME) ?: ""
        nickname = intent.getStringExtra(NICKNAME) ?: ""
        selectTeam = intent.getStringExtra(SELECT_TEAM) ?: ""
        val team = intent.getStringExtra(TEAM) ?: ""

        if (time.isEmpty() || nickname.isEmpty() || selectTeam.isEmpty() || team.isEmpty()){
            toast("채팅방 생성에 오류가 발생하였습니다.")
            finish()
            return@with
        }

        chattingTitle.text = team.replace(":", " vs ")   // ex) T1:DAMWONGAMMING > T1 vs DAMWONGAMMING 으로 수정하여 표시

        val teams = team.split(":") // ':'을 기준으로 왼쪽 팀, 오른쪽 팀을 나누어서 저장
        setTeamLogoImageView(imgTeam1, teams[0])
        setTeamLogoImageView(imgTeam2, teams[1])

        comment.id = nickname
        comment.team = selectTeam

        btnChatting.setOnClickListener {
            // 이전 날의 채팅방 입장은 가능하지만 당일 제외하고 채팅 입력 불가
//            if (isCurrentDate(time)) {
//                toast("당일에만 작성 가능합니다.")
//                return@setOnClickListener
//            }

            comment.message = editChatting.text.toString()
            comment.timestamp = System.currentTimeMillis()
            // 채팅 내용 RealtimeDatabase 에 추가
            reference.reference.child("ChattingRoom").child(time).child("comments").push().setValue(comment).addOnCompleteListener {
                editChatting.text = null
                // 채팅 추가 한 후 스크롤 이동
                chattingRecycler.scrollToPosition(adapter.itemCount-1)
            }
        } // setOnClickListener
    }

    // 채팅방이 생성 여부 검사
    private fun checkChattingRoom(){
        Log.e("+++++", "checkChattingRoom")
        reference.reference.child("ChattingRoom").orderByChild("chattingTitle").equalTo(time)
                .addListenerForSingleValueEvent(object : ValueEventListener{
                    override fun onCancelled(error : DatabaseError) {
                        Log.e("채팅액티비티", "채팅방 오류")
                    }
                    override fun onDataChange(dataSnapshot : DataSnapshot) {
                        // 채팅방이 있을 경우 기존 채팅방 사용
                        for(item in dataSnapshot.children){
                            Log.e("+++++", "기존 채팅방 사용")
                            var chattingDTO = item.getValue(ChattingDTO::class.java)!!
                            var usersMap = chattingDTO.users as MutableMap
                            usersMap[nickname] = selectTeam
                            // 들어온 유저에 추가
                            reference.reference.child("ChattingRoom").child(time).child("users").updateChildren(usersMap as Map<String, Any>)
                            // 리사이클러뷰 세팅
                            setRecycler()
                            adapter.startListening()
                            return
                        }
                        // 채팅방이 없을 경우 채팅방 생성
                        if(dataSnapshot.children.count() == 0){
                            Log.e("+++++", "채팅방 생성")
                            var chattingDTO = ChattingDTO()
                            chattingDTO.chattingTitle = time
                            chattingDTO.users = mapOf(nickname to selectTeam)
                            reference.reference.child("ChattingRoom").child(time).setValue(chattingDTO).addOnSuccessListener {
                                checkChattingRoom() // 함수 재호출
                            }
                        }
                    } // onDataChange()
                }) // addListenerForSingleValueEvent
    } // checkChattingRoom

    // RecyclerView 설정
    fun setRecycler(){
        Log.e("++++", "recyclerview create")
        var query = reference.reference.child("ChattingRoom").child(time).child("comments").orderByChild("timestamp")
        var options = FirebaseRecyclerOptions.Builder<ChattingDTO.Comment>()
                .setQuery(query, ChattingDTO.Comment::class.java).build()
        adapter = ChattingAdapter(options, this)
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
            reference.reference.child("ChattingRoom").child(time).child("users/$nickname").removeValue()
        }
    }

    // 채팅 방 입장 및 새로운 채팅이 생겼을 때 가장 아래로 이동
    override fun moveScroll() {
        binding.chattingRecycler.scrollToPosition(adapter.itemCount-1)
    }

    override fun logout(view: View) {
        auth.signOut()
        startActivity(LoginActivity::class.java, Intent.FLAG_ACTIVITY_CLEAR_TOP)
        finish()
    }

    override fun moveHome(view: View) {
        startActivity(MainActivity::class.java, Intent.FLAG_ACTIVITY_CLEAR_TOP)
        finish()
    }

    companion object {
        const val SELECT_TEAM = "selectTeam"
        const val NICKNAME = "nickName"
        const val TIME = "time"
        const val TEAM = "team"
    }
}