package com.ezen.lolketing.view.main.manager

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.ezen.lolketing.BaseActivity
import com.ezen.lolketing.view.main.MainActivity
import com.ezen.lolketing.R
import com.ezen.lolketing.databinding.ActivityManagerBinding
import com.ezen.lolketing.model.Game
import com.ezen.lolketing.model.TeamDTO
import com.ezen.lolketing.view.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.jsoup.Jsoup

class ManagerActivity : BaseActivity<ActivityManagerBinding>(R.layout.activity_manager) {

    var firestore = FirebaseFirestore.getInstance()
    var auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    // 팀 정보 수정 버튼 클릭 > 사이트에 올려둔 JSON 파싱해서 데이터베이스에 저장
    fun updateTeam(view: View) {
        // 등록해 둔 팀정보 JSON URL
        var url = "https://api.myjson.com/bins/16qcj0"
        // JSON 파싱
        var request = object : JsonObjectRequest(Request.Method.GET, url, null,
                Response.Listener {
                    var teams = it.getJSONArray("Team")
                    for(i in 1..teams.length()){
                        var team = teams.getJSONObject(i-1)
                        var coach = team.getJSONArray("coach")
                        var coachArray = ArrayList<String>()

                        for(j in 1..coach.length()){
                            coachArray.add(coach.getString(j-1))
                        }

                        var teamDTO = TeamDTO()
                        teamDTO.foundation = team.getString("foundation")
                        teamDTO.team_name = team.getString("team_name")
                        teamDTO.team_name_k = team.getString("team_name_k")
                        teamDTO.team_color = team.getString("team_color")
                        teamDTO.head_coach = team.getString("head_coach")
                        teamDTO.captain = team.getString("captain")
                        teamDTO.coach = coachArray

                        // 팀 정보 데이터베이스에 추가
                        firestore.collection("Team").document(team.getString("team_name")).set(teamDTO)

                        var roster = team.getJSONArray("roster")
                        for(z in 1..roster.length()){
                            var rosterObject = roster.getJSONObject(z-1)
                            var playerDTO = TeamDTO.PlayerDTO()
                            playerDTO.name = rosterObject.getString("name")
                            playerDTO.nickname = rosterObject.getString("nickname")
                            playerDTO.position = rosterObject.getString("position") ?: ""
                            playerDTO.img = rosterObject.getString("img")
                            // 팀에 해당하는 선수들 추가
                            firestore.collection("Team").document(team.get("team_name").toString())
                                    .collection("Player").document(rosterObject.getString("name")).set(playerDTO)
                        } // for 1..roster.length()
                    } // for 1..team.length()
                }, // Response.Listener
                Response.ErrorListener {
                    Log.e("test", "JSON 불러오기 오류 ${it.printStackTrace()}")
                }){} // JsonObjectRequest

        Volley.newRequestQueue(applicationContext).add(request)
    } // updateTeam()

    // 경기 정보 수정 버튼 클릭
    fun updateGame(view: View) {
//        alert(title = "게임 업데이트", message = "새로 추가하는 게임입니까?") {
//            positiveButton("예"){
//                gamesUpdate(true)
//            }
//            negativeButton("아니요"){
//                gamesUpdate(false)
//            }
//        }.show()
    } // updateGame()

    // 경기 정보 데이터베이스에 추가
    fun gamesUpdate(newGames : Boolean){
        // 공식 페이지 대회 일정 정보 URL
        val url = "https://www.leagueoflegends.co.kr/?m=esports&mod=chams_schedule&cate=1"
        try {
            val doc = Jsoup.connect(url).get()  // URL 정보와 연결
            val gameData = doc.select("tbody").select("tr") // tbody 안에는 tr 태그들을 가져오기 [게임 정보]
            var date = ""
            gameData.forEachIndexed { index, element -> // [게임 정보]의 개수 만큼 반복
                // 20:00는 날짜가 없기 때문에 17:00 날짜를 동일하게 사용
                if(element.select("th[class=table-data__date]").text().length > 2)
                    date = element.select("th[class=table-data__date]").text()  // 게임의 날짜
                var time = element.select("td[class=table-data__time]").text()  // 게임의 시간
                var result = element.select(".match_count").text()  // 게임 결과
                // 왼쪽팀:오른쪽팀
                var team = "${element.select(".table-data__position-left").select(".table-data__team-name").text()}:${element.select(".table-data__position-right").select(".table-data__team-name").text()}"
                date = date.replace("2020.02", "2020.03")
                // 아직 게임이 진행하지 않은 게임의 경우 VS로 표기되어 있는데 그것을 0:0으로 수정
                if(result.length < 2)
                    result = "0:0"

                Log.e("test", "$date $time : $team")
                // firestore 에 경기 추가
                var gameDTO = Game(date, time, team, result, "예매")
                firestore.collection("Game").document("${gameDTO.date} ${gameDTO.time}").set(gameDTO)
                // 새 게임의 경우 Seat 데이터 넣기 : 오류 방지하기 위함
                if(newGames)
                    firestore.collection("Game").document("${gameDTO.date} ${gameDTO.time}").collection("Seat").document("seat").set(mapOf("count" to 0))
            } // gameData.forEachIndexed
        } catch (e: Exception) {
            e.printStackTrace()
        }
    } // gamesUpdate

    fun addShop(view: View) {
        // 상품 이름 .itemtit
        // 상품 가격 .price_real "75,000"
        // 그룹
        // 이미지 .ee-module-width-body > .ee-image > img 의 src
    } // adShop()

    override fun logout(view: View) {
        auth.signOut()
        var intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
    } // logout()

    override fun moveHome(view: View){
        var intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
    } // moveHome()
} // ManagerActivity()