package com.ezen.lolketing

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.ezen.lolketing.adapter.ReserveAdapter
import com.ezen.lolketing.model.GameDTO
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_reserve_list.*

class ReserveListActivity : AppCompatActivity(), ReserveAdapter.reserveOnClick {

    private lateinit var adapter : ReserveAdapter
    private var firestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reserve_list)

        var query = firestore.collection("Game").orderBy("date")
        var options = FirestoreRecyclerOptions.Builder<GameDTO>()
                .setQuery(query, GameDTO::class.java).build()
        adapter = ReserveAdapter(options, this)

        reserve_recycler.setHasFixedSize(true)
        reserve_recycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        reserve_recycler.adapter = adapter
    }

    fun logout(view: View?) {}

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }

    override fun reserveClick(intent: Intent) {
        startActivity(intent)
    }
}