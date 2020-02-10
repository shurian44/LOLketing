package com.ezen.lolketing

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.ezen.lolketing.adapter.TeamAdapter
import com.ezen.lolketing.model.TeamDTO
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_team.*

class TeamActivity : AppCompatActivity() {
    lateinit var team : String
    var firestore = FirebaseFirestore.getInstance()
    lateinit var adapter : TeamAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_team)

        team = intent.getStringExtra("team")
        getTeam()

        var query = firestore.collection("Team").document(team).collection("Player")
        var options = FirestoreRecyclerOptions.Builder<TeamDTO.PlayerDTO>()
            .setQuery(query, TeamDTO.PlayerDTO::class.java).build()
        adapter = TeamAdapter(options)

        team_recycler.setHasFixedSize(true)
        team_recycler.adapter = adapter
        team_recycler.layoutManager = GridLayoutManager(this, 2)

    }

    private fun getTeam(){
        team_name.text = team

        firestore.collection("Team").document(team).get().addOnCompleteListener {
            if(it.result != null){
                var teamDTO = it.result!!.toObject(TeamDTO::class.java)
                top_view.setBackgroundColor(Color.parseColor(teamDTO!!.team_color))
                team_foundation.text = teamDTO.foundation
                while (!it.isComplete){}
            }
        }
    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }
}