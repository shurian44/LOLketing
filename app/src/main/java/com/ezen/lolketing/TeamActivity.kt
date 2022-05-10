package com.ezen.lolketing

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.ezen.lolketing.adapter.TeamAdapter
import com.ezen.lolketing.databinding.ActivityTeamBinding
import com.ezen.lolketing.model.TeamDTO
import com.ezen.lolketing.view.main.board.BoardListActivity
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore

class TeamActivity : BaseActivity<ActivityTeamBinding>(R.layout.activity_team) {
    lateinit var team : String
    var firestore = FirebaseFirestore.getInstance()
    lateinit var adapter : TeamAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        team = intent?.getStringExtra("team") ?: ""
        getTeam()

        binding.topView.setOnClickListener {
            var intent = Intent(this, BoardListActivity::class.java)
            intent.putExtra("team", team)
            startActivity(intent)
        }

        var query = firestore.collection("Team").document(team).collection("Player")
        var options = FirestoreRecyclerOptions.Builder<TeamDTO.PlayerDTO>()
            .setQuery(query, TeamDTO.PlayerDTO::class.java).build()
        adapter = TeamAdapter(options)

        binding.teamRecycler.setHasFixedSize(true)
        binding.teamRecycler.adapter = adapter
        binding.teamRecycler.layoutManager = GridLayoutManager(this, 2)

    }

    private fun getTeam(){
        binding.teamName.text = team

        firestore.collection("Team").document(team).get().addOnCompleteListener {
            if(it.result != null){
                var teamDTO = it.result!!.toObject(TeamDTO::class.java)
                binding.topView.setBackgroundColor(Color.parseColor(teamDTO!!.team_color))
                binding.teamFoundation.text = teamDTO.foundation
                while (!it.isComplete){}
            }
        }
    }

    override fun logout(view: View) {}

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }
}