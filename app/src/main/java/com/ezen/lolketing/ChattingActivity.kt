package com.ezen.lolketing

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.ezen.lolketing.adapter.ChattingAdapter
import com.ezen.lolketing.model.ChattingDTO
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_chatting.*
import org.jetbrains.anko.progressDialog
import org.jetbrains.anko.toast
import java.text.SimpleDateFormat
import java.util.*


class ChattingActivity : AppCompatActivity(), ChattingAdapter.moveScrollListener {

    private var auth = FirebaseAuth.getInstance()
    private var reference = FirebaseDatabase.getInstance()
    private var comment = ChattingDTO.Comment() // 댓글을 저장할 변수
    private lateinit var adapter : ChattingAdapter // 채팅 리사이클러의 어뎁터
    private lateinit var nickname : String // 유저의 닉네임
    lateinit var time : String  // 채팅 방의 시간
    lateinit var selectTeam : String // 선택한 팀 L or R
    lateinit var team : String  // 게임의 팀들

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chatting)

        time = intent.getStringExtra("time")
        nickname = intent.getStringExtra("nickName")
        selectTeam = intent.getStringExtra("selectTeam")
        team = intent.getStringExtra("team")
        chatting_title.text = team.replace(":", "vs")   // ex) T1:DAMWONGAMMING > T1 vs DAMWONGAMMING 으로 수정하여 표시

        var teams = team.split(":") // ':'을 기준으로 왼쪽 팀, 오른쪽 팀을 나누어서 저장
        setImage(img_team1, teams[0])
        setImage(img_team2, teams[1])

        var dateFormat = SimpleDateFormat("yyyyMMdd")
        var mDate = Date()

        comment.id = nickname
        comment.team = selectTeam

        btn_chatting.setOnClickListener {
            // 이전 날의 채팅방 입장은 가능하지만 당일 제외하고 채팅 입력 불가
            if(time.substring(0, 8) != dateFormat.format(mDate)){ // ex) 20200205 17:00 > 20200205로 변환하여 날짜 비교
                toast("당일에만 작성 가능합니다.")
                return@setOnClickListener
            }
            comment.message = edit_chatting.text.toString()
            comment.timestamp = System.currentTimeMillis()
            // 채팅 내용 RealtimeDatabase 에 추가
            reference.reference.child("ChattingRoom").child(time).child("comments").push().setValue(comment).addOnCompleteListener {
                edit_chatting.text = null
                // 채팅 추가 한 후 스크롤 이동
                chatting_recycler.scrollToPosition(adapter.itemCount-1)
            }
        } // setOnClickListener
    }   // onCreate()

    // 채팅방이 생성 여부 검사
    private fun checkChattingRoom(){
        reference.reference.child("ChattingRoom").orderByChild("chattingTitle").equalTo(time)
                .addListenerForSingleValueEvent(object : ValueEventListener{
                    override fun onCancelled(error : DatabaseError) {
                        Log.e("채팅액티비티", "채팅방 오류")
                    }
                    override fun onDataChange(dataSnapshot : DataSnapshot) {
                        // 채팅방이 있을 경우 기존 채팅방 사용
                        for(item in dataSnapshot.children){
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
        var query = reference.reference.child("ChattingRoom").child(time).child("comments").orderByChild("timestamp")
        var options = FirebaseRecyclerOptions.Builder<ChattingDTO.Comment>()
                .setQuery(query, ChattingDTO.Comment::class.java).build()
        adapter = ChattingAdapter(options, this)
        chatting_recycler.setHasFixedSize(true)
        chatting_recycler.adapter = adapter
        chatting_recycler.layoutManager = LinearLayoutManager(this)
    } // setRecycler()

    // 팀에 맞게 이미지 세팅
    fun setImage(image : ImageView, team : String? = ""){
        when(team){
            "T1"-> image.setImageResource(R.drawable.logo_t1)
            "Griffin"-> image.setImageResource(R.drawable.logo_griffin)
            "DAMWON Gaming"-> image.setImageResource(R.drawable.icon_damwon)
            "SANDBOX Gaming"-> image.setImageResource(R.drawable.icon_sandbox)
            "Afreeca Freecs"-> image.setImageResource(R.drawable.icon_afreeca)
            "Gen.G Esports"-> image.setImageResource(R.drawable.icon_geng)
            "DragonX"-> image.setImageResource(R.drawable.icon_dragonx)
            "KT Rolster"-> image.setImageResource(R.drawable.icon_rolster)
            "APK PRINCE"-> image.setImageResource(R.drawable.icon_apk_prince)
            "Hanwha Life Esports"-> image.setImageResource(R.drawable.icon_hanwha)
        }
    } // setImage()

    override fun onStart() {
        super.onStart()
        // 채팅방이 생성 여부 검사
        checkChattingRoom()
    }

    override fun onStop() {
        super.onStop()
        if(adapter != null)
            adapter.stopListening()
        // 채팅 방에서 나가거나 페이지 이동 시에 채팅방 유저 제거
        reference.reference.child("ChattingRoom").child(time).child("users/$nickname").removeValue()
    }

    // 채팅 방 입장 및 새로운 채팅이 생겼을 때 가장 아래로 이동
    override fun moveScroll() {
        chatting_recycler.scrollToPosition(adapter.itemCount-1)
    }

    fun logout(view: View) {
        auth.signOut()
        var intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
    }

    fun moveHome(view: View) {
        var intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
    }
}