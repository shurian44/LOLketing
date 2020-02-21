package com.ezen.lolketing

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_chatting.*

class ChattingListActivity : AppCompatActivity() {

    var database = FirebaseDatabase.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chattinglist)
    }

    fun logout(view: View) {}
    fun enterChatting(view: View) {
        when(view.id){
            R.id.img_team1->{
                database.reference.child("chatrooms").child("chat").setValue("테스트")
            }
            R.id.img_team2->{

            }
            R.id.img_team3->{

            }
            R.id.img_team4->{
                
            }
        }
        startActivity(Intent(this, ChattingActivity::class.java))
    }
}