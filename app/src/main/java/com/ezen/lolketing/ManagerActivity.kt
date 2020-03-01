package com.ezen.lolketing

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.ezen.lolketing.model.GameDTO
import com.ezen.lolketing.model.TeamDTO
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_manager.*
import org.jetbrains.anko.doAsync
import org.jsoup.Jsoup

class ManagerActivity : AppCompatActivity() {

    var firestore = FirebaseFirestore.getInstance()
    var auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manager)

        addShopEvent.setOnClickListener {
            startActivity(Intent(this, ShopEventActivity::class.java))
        }
    }

    fun addTeam(view: View) {
        var url = "https://api.myjson.com/bins/16qcj0"
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

                        Log.e("tet", "DB 추가중 ${team.getString("team_name")}")

                        var teamDTO = TeamDTO()
                        teamDTO.foundation = team.getString("foundation")
                        teamDTO.team_name = team.getString("team_name")
                        teamDTO.team_name_k = team.getString("team_name_k")
                        teamDTO.team_color = team.getString("team_color")
                        teamDTO.head_coach = team.getString("head_coach")
                        teamDTO.captain = team.getString("captain")
                        teamDTO.coach = coachArray

                        firestore.collection("Team").document(team.getString("team_name")).set(teamDTO)

                        var roster = team.getJSONArray("roster")
                        for(z in 1..roster.length()){
                            var rosterObject = roster.getJSONObject(z-1)
                            Log.e("test", "??$rosterObject")
                            var playerDTO = TeamDTO.PlayerDTO()
                            playerDTO.name = rosterObject.getString("name")
                            playerDTO.nickname = rosterObject.getString("nickname")
                            playerDTO.position = rosterObject.getString("position") ?: ""
                            playerDTO.img = rosterObject.getString("img")
                            firestore.collection("Team").document(team.get("team_name").toString())
                                    .collection("Player").document(rosterObject.getString("name")).set(playerDTO)
                        }
                    }
                },
                Response.ErrorListener {
                    it.printStackTrace()
                    Log.e("test", "JSON 불러오기 오류")
                }){}

        Volley.newRequestQueue(applicationContext).add(request)
    }
    fun addGame(view: View) {
        var gameDTOs = ArrayList<GameDTO>()
        doAsync {
            val url = "https://www.leagueoflegends.co.kr/?m=esports&mod=chams_schedule&cate=1"
            try {
                val doc = Jsoup.connect(url).get()
                val gameData = doc.select("tbody").select("tr")
                gameData.forEachIndexed { index, element ->
                    var date = element.select("th[class=table-data__date]").text()
                    var time = element.select("td[class=table-data__time]").text()
                    var result = element.select(".match_count").text()
                    var team = "${element.select(".table-data__position-left").select(".table-data__team-name").text()}:${element.select(".table-data__position-right").select(".table-data__team-name").text()}"

                    if(result.length < 2) result = "0:0"

                    gameDTOs.add(GameDTO(date, time, team, result, "예약"))

                    runOnUiThread {
                    }
                }

                for(i in gameDTOs.indices){
                    if(gameDTOs[i].date!!.length < 2){
                        gameDTOs[i].date = gameDTOs[i-1].date
                    }
                    firestore.collection("Game").document("${gameDTOs[i].date} ${gameDTOs[i].time}").set(gameDTOs[i])
//                    firestore.collection("Game").document("${gameDTOs[i].date} ${gameDTOs[i].time}").collection("Seat").document("seat").set(mapOf("count" to 0))
                    Log.e("제이선 테스트", "${gameDTOs[i].team}의 경기는 ${gameDTOs[i].date} ${gameDTOs[i].time}\n결과는 ${gameDTOs[i].result} 상태는 ${gameDTOs[i].status}")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    fun addShop(view: View) {
        // 상품 이름 .itemtit
        // 상품 가격 .price_real "75,000"
        // 그룹
        // 이미지 .ee-module-width-body > .ee-image > img 의 src
    }

    fun logout(view: View) {
        auth.signOut()
        var intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
    }
    fun moveHome(view: View){
        var intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
    }
}