package com.ezen.lolketing

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
import org.jetbrains.anko.toast
import java.text.SimpleDateFormat
import java.util.*


class ChattingActivity : AppCompatActivity(), ChattingAdapter.moveScrollListener {

    private var auth = FirebaseAuth.getInstance()
    private var reference = FirebaseDatabase.getInstance()
    private lateinit var adapter : ChattingAdapter
    lateinit var time : String
    lateinit var nickname : String
    lateinit var selectTeam : String
    lateinit var team : String
    var isChattingRoom = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chatting)

        time = intent.getStringExtra("time")
        nickname = intent.getStringExtra("nickName")
        selectTeam = intent.getStringExtra("selectTeam")
        team = intent.getStringExtra("team")
        chatting_title.text = team.replace(":", "vs")

        var teams = team.split(":")
        setImage(img_team1, teams[0])
        setImage(img_team2, teams[1])

        var dateFormat = SimpleDateFormat("yyyyMMdd HH:mm")
        var date = dateFormat.parse(time)
        var mDate = Date()

        btn_chatting.setOnClickListener {
            if(mDate.date != date.date && mDate.month != date.month){
                toast("당일에만 작성 가능합니다.${date} / ${mDate}")
                return@setOnClickListener
            }
            var comment = ChattingDTO.Comment()
            comment.id = nickname
            comment.message = edit_chatting.text.toString()
            comment.team = selectTeam
            comment.timestamp = System.currentTimeMillis()

            reference.reference.child("ChattingRoom").child(time).child("comments").push().setValue(comment).addOnCompleteListener {
                edit_chatting.text = null
                chatting_recycler.scrollToPosition(adapter.itemCount-1)
            }
        }
    }

    private fun checkChattingRoom(){
        reference.reference.child("ChattingRoom").orderByChild("chattingTitle").equalTo(time)
                .addListenerForSingleValueEvent(object : ValueEventListener{
                    override fun onCancelled(error : DatabaseError) {
                        Log.e("채팅액티비티", "에러났당")
                    }
                    override fun onDataChange(dataSnapshot : DataSnapshot) {
                        for(item in dataSnapshot.children){
                            var chattingDTO = item.getValue(ChattingDTO::class.java)!!
                            if(chattingDTO.chattingTitle == time){
                                isChattingRoom = true
                                var usersMap = chattingDTO.users as MutableMap
                                usersMap[nickname] = selectTeam
                                reference.reference.child("ChattingRoom").child(time).child("users").updateChildren(usersMap as Map<String, Any>)
                                setRecycler()

                                adapter.startListening()
                                return
                            }
                        }
                        if(!isChattingRoom){
                            var chattingDTO = ChattingDTO()
                            chattingDTO.chattingTitle = time
                            chattingDTO.users = mapOf(nickname to selectTeam)
                            reference.reference.child("ChattingRoom").child(time).setValue(chattingDTO).addOnSuccessListener {
                                checkChattingRoom()
                            }
                            return
                        }
                    }
                })
    }

    fun setRecycler(){
        var query = reference.reference.child("ChattingRoom").child(time).child("comments").orderByChild("timestamp")
        var options = FirebaseRecyclerOptions.Builder<ChattingDTO.Comment>()
                .setQuery(query, ChattingDTO.Comment::class.java).build()
        adapter = ChattingAdapter(options, this)
        chatting_recycler.setHasFixedSize(true)
        chatting_recycler.adapter = adapter
        chatting_recycler.layoutManager = LinearLayoutManager(this)
//        Handler().postDelayed(Runnable { chatting_recycler.scrollToPosition(adapter.itemCount-1) }, 400)

    }

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
    }

    fun logout(view: View) {
        auth.signOut()
        var intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
    }

    override fun onStart() {
        super.onStart()
        checkChattingRoom()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
        reference.reference.child("ChattingRoom").child(time).child("users/$nickname").removeValue()

    }

    override fun moveScroll() {
        chatting_recycler.scrollToPosition(adapter.itemCount-1)
    }

    fun moveHome(view: View) {
        var intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
    }
}