package com.ezen.lolketing

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.DatePicker
import android.widget.ImageView
import android.widget.Toast
import com.ezen.lolketing.model.ChattingDTO
import com.ezen.lolketing.model.GameDTO
import com.ezen.lolketing.model.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_chattinglist.*
import java.text.SimpleDateFormat
import java.util.*

class ChattingListActivity : AppCompatActivity() {

    private var firestore = FirebaseFirestore.getInstance()
    private var auth = FirebaseAuth.getInstance()
    private var nickName = ""
    var games = mutableListOf<GameDTO>()
    var dateFormat = SimpleDateFormat("yyyy.MM.dd")
    lateinit var teamA : String
    lateinit var teamB : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chattinglist)

        firestore.collection("Users").document(auth.currentUser?.email!!).get().addOnSuccessListener {
            var user = it.toObject(Users::class.java)!!
            nickName = user.nickname!!
        }


        firestore.collection("Game").get().addOnSuccessListener {
            games = it.toObjects(GameDTO::class.java)

            setViews(true, Date())
        }

        var date = Date()
        var calendar = Calendar.getInstance()
        Log.e("test", "${date.year}.${date.month}.${date.day}.${date.date}${date}")
        layout_today.setOnClickListener {
            var dialog = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                var date = "$year."
                date += if(month < 10) "0${month+1}."
                        else "${month+1}."
                date += if(dayOfMonth < 10) "0$dayOfMonth"
                else "$dayOfMonth"
                txt_today.text = date
                setViews(false, dateFormat.parse(txt_today.text.toString()))
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE))
            dialog.show()
        }

    }

    fun logout(view: View) {
        auth.signOut()
        var intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
    }

    fun enterChatting(view: View) {
        var time = txt_today.text.toString()
        var selectTeam = ""
        var intent = Intent(this, ChattingActivity::class.java)

        time = time.replace(".", "")
        when(view.id){
            R.id.img_team1->{
                time += " 17:00"
                selectTeam = "L"
                intent.putExtra("team", teamA)
            }
            R.id.img_team2->{
                time += " 17:00"
                selectTeam = "R"
                intent.putExtra("team", teamA)
            }
            R.id.img_team3->{
                time += " 20:00"
                selectTeam = "L"
                intent.putExtra("team", teamB)
            }
            R.id.img_team4->{
                time += " 20:00"
                selectTeam = "R"
                intent.putExtra("team", teamB)
            }
        }

        var dateFormat = SimpleDateFormat("yyyyMMdd HH:mm")
        var date = dateFormat.parse(time)
        date.minutes = date.minutes - 30
        var mDate = Date()
        if(mDate < date){
            Toast.makeText(this, "아직 입장하실수 없습니다.", Toast.LENGTH_SHORT).show()
            return
        }

        Log.e("Test", "A : ${teamA}, B : ${teamB}!")
        intent.putExtra("selectTeam", selectTeam)
        intent.putExtra("time", time)
        intent.putExtra("nickName", nickName)
        startActivity(intent)
    }

    fun setViews(isCreate : Boolean, mDate : Date){
        for(i in games.indices){
            var date = dateFormat.parse(games[i].date)

            layout_game.visibility = View.VISIBLE
            txt_noGame.visibility = View.GONE

            if(isCreate && mDate <= date){
                teamA = games[i].team!!
                teamB = games[i+1].team!!
                txt_today.text = dateFormat.format(date)
                var team_17 = games[i].team!!.split(":")
                var team_20 = games[i+1].team!!.split(":")
                setImage(img_team1, team_17[0])
                setImage(img_team2, team_17[1])
                setImage(img_team3, team_20[0])
                setImage(img_team4, team_20[1])
                return
            }else if(date == mDate){
                teamA = games[i].team!!
                teamB = games[i+1].team!!
                var team_17 = games[i].team!!.split(":")
                var team_20 = games[i+1].team!!.split(":")
                setImage(img_team1, team_17[0])
                setImage(img_team2, team_17[1])
                setImage(img_team3, team_20[0])
                setImage(img_team4, team_20[1])
                return
            }

            layout_game.visibility = View.INVISIBLE
            txt_noGame.visibility = View.VISIBLE

        }
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

    fun moveHome(view: View) {
        var intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
    }
}