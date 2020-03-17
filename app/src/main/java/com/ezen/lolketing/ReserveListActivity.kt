package com.ezen.lolketing

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import com.ezen.lolketing.adapter.ReserveAdapter
import com.ezen.lolketing.model.GameDTO
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_reserve_list.*
import java.text.SimpleDateFormat
import java.util.*

class ReserveListActivity : AppCompatActivity(), ReserveAdapter.reserveItemClickListener {

    private lateinit var adapter : ReserveAdapter
    private var firestore = FirebaseFirestore.getInstance()
    private var auth = FirebaseAuth.getInstance()
    lateinit var layoutManager : LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reserve_list)

        // 하루 전 날 날짜 : date 이후 날짜 기준으로 조회하기 때문
        var date = Date()
        var format = SimpleDateFormat("yyyy.MM.dd")
        date.date = date.date - 1

        // 게임 데이터 조회
        var query = firestore.collection("Game").orderBy("date").whereGreaterThan("date", format.format(date))
        var options = FirestoreRecyclerOptions.Builder<GameDTO>()
                .setQuery(query, GameDTO::class.java).build()
        adapter = ReserveAdapter(options, this)
        // 리사이클러뷰 설정
        reserve_recycler.setHasFixedSize(true)
        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        reserve_recycler.layoutManager = layoutManager
        reserve_recycler.adapter = adapter
    } // onCreate()

    override fun reserveSelect(intent: Intent) {
        startActivity(intent)
    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }

    fun logout(view: View) {
        auth.signOut()
        var intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
    }

    fun moveHome(view: View) {
        var intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
    }
}