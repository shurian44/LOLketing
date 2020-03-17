package com.ezen.lolketing

import android.app.DatePickerDialog
import android.app.ProgressDialog
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
import org.jetbrains.anko.progressDialog
import java.text.SimpleDateFormat
import java.util.*

class ChattingListActivity : AppCompatActivity() {

    private var firestore = FirebaseFirestore.getInstance()
    private var auth = FirebaseAuth.getInstance()
    private var nickName = ""   // 사용자의 닉네임
    var games = mutableListOf<GameDTO>() // 게임 데이터 배열
    var dateFormat = SimpleDateFormat("yyyy.MM.dd") // 데이트 포맷
    lateinit var teamA : String // 17:00 게임 팀
    lateinit var teamB : String // 20:00 게임 팀
    lateinit var dialog : ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chattinglist)

        // 유저의 닉네임 가져오기
        firestore.collection("Users").document(auth.currentUser?.email!!).get().addOnSuccessListener {
            var user = it.toObject(Users::class.java)!!
            nickName = user.nickname!!
        }
        // ProgressDialog 설정
        dialog = progressDialog(message = "잠시만 기다려 주세요...")
        // 게임의 데이터 가져오기
        firestore.collection("Game").get().addOnSuccessListener {
            games = it.toObjects(GameDTO::class.java)
            // 초기 UI 세팅
            dialog.show()
            setViews(true, Date())
            dialog.dismiss()
        }

        var calendar = Calendar.getInstance() // DatePickerDialog 에 사용할 날짜
        layout_today.setOnClickListener {
            var dialog = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                var date = "$year."  // ex) 2020.02.02 양식 만들기
                date += if(month < 10) "0${month+1}."
                        else "${month+1}."
                date += if(dayOfMonth < 10) "0$dayOfMonth"
                        else "$dayOfMonth"

                // 뷰 세팅
                dialog.show()
                setViews(false, dateFormat.parse(date))
                dialog.dismiss()
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE))
            dialog.show()
        }

    } // onCreate()

    // 원하는 팀을 선택해서 채팅방 입장
    fun enterChatting(view: View) {
        var time = txt_today.text.toString()
        var selectTeam = "" // 선택한 팀의 위치 저장 L or R : DB에 입장한 유저 따로 저장하기 위함
        var intent = Intent(this, ChattingActivity::class.java)
        // ex) 2020.02.05 -> 20200205 수정 : realtimeDatabase 에서 .은 하위 노드로 판단하기 때문에 제거
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

        // 경기 시작 30분 전 까지는 채팅방 입장 불가
        var dateFormat = SimpleDateFormat("yyyyMMdd HH:mm")
        var date = dateFormat.parse(time)
        date.minutes = date.minutes - 30
        var mDate = Date()
        if(mDate < date){
            Toast.makeText(this, "아직 입장하실수 없습니다.", Toast.LENGTH_SHORT).show()
            return
        }

        intent.putExtra("selectTeam", selectTeam)
        intent.putExtra("time", time)
        intent.putExtra("nickName", nickName)
        startActivity(intent)
    } // enterChatting()

    // UI 세팅 : 초기 세팅 or 날짜 선택 시 세팅
    fun setViews(isCreate : Boolean, mDate : Date){
        txt_today.text = dateFormat.format(mDate)  // 날짜 표시
        for(i in games.indices){
            // 게임 데이터의 날짜를 Date 타입으로 파싱
            var date = dateFormat.parse(games[i].date)
            // 오늘 날짜와 게임의 날짜가 동일 할 경우
            if(dateFormat.format(mDate) == games[i].date){
                // 경기 데이터 받아오기
                teamA = games[i].team!!
                teamB = games[i+1].team!!
                // T1:DAMWONGAMMING > T1 DAMWONGAMMING 으로 분리하여 List 에 저장한 뒤 이미지 세팅
                var team_17 = games[i].team!!.split(":")
                var team_20 = games[i+1].team!!.split(":")
                setImage(img_team1, team_17[0])
                setImage(img_team2, team_17[1])
                setImage(img_team3, team_20[0])
                setImage(img_team4, team_20[1])
                layout_game.visibility = View.VISIBLE
                txt_noGame.visibility = View.GONE
                return
            }
        } // for
        // 게임이 없는 날일 경우 UI 표시
        layout_game.visibility = View.INVISIBLE
        txt_noGame.visibility = View.VISIBLE
    } // setViews()

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

    fun moveHome(view: View) {
        var intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
    }

    fun logout(view: View) {
        auth.signOut()
        var intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
    }
}