package com.ezen.lolketing

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.ezen.lolketing.model.TeamDTO
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.JsonObject

class JsonActivity : AppCompatActivity() {

    var firestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_json)

        var url = "https://api.myjson.com/bins/9t3gw"
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
                        playerDTO.position = rosterObject.getString("position")
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
}