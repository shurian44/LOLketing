package com.ezen.lolketing

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.DatePicker
import com.ezen.lolketing.model.ChattingDTO
import com.ezen.lolketing.model.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_chatting.*
import kotlinx.android.synthetic.main.activity_chattinglist.*
import java.util.*

class ChattingListActivity : AppCompatActivity() {

    private var database = FirebaseDatabase.getInstance()
    private var firestore = FirebaseFirestore.getInstance()
    private var auth = FirebaseAuth.getInstance()
    private var nickName = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chattinglist)

        firestore.collection("Users").document(auth.currentUser?.email!!).get().addOnSuccessListener {
            var user = it.toObject(Users::class.java)!!
            nickName = user.nickname!!
        }

        var date = Date()
        var calendar = Calendar.getInstance()
        Log.e("test", "${date.year}.${date.month}.${date.day}.${date.date}${date}")
        layout_today.setOnClickListener {
            var dialog = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                txt_today.text = "$year.${month+1}.$dayOfMonth"
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE))
            dialog.show()
        }

    }

    fun logout(view: View) {}
    fun enterChatting(view: View) {
        var time = txt_today.text.toString()
        var selectTeam = ""

        time = time.replace(".", "")
        when(view.id){
            R.id.img_team1->{
                time += " 17:00"
                selectTeam = "L"
            }
            R.id.img_team2->{
                time += " 17:00"
                selectTeam = "R"
            }
            R.id.img_team3->{
                time += " 20:00"
                selectTeam = "L"
            }
            R.id.img_team4->{
                time += " 20:00"
                selectTeam = "R"
            }
        }
        var intent = Intent(this, ChattingActivity::class.java)
        intent.putExtra("time", time)
        intent.putExtra("nickName", nickName)
        intent.putExtra("selectTeam", selectTeam)
        startActivity(intent)
    }
}