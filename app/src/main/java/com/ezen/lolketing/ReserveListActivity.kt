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

class ReserveListActivity : AppCompatActivity(), ReserveAdapter.reserveOnClick {

    private lateinit var adapter : ReserveAdapter
    private var firestore = FirebaseFirestore.getInstance()
    private var auth = FirebaseAuth.getInstance()
    lateinit var layoutManager : LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reserve_list)

        var query = firestore.collection("Game").orderBy("date")
        var options = FirestoreRecyclerOptions.Builder<GameDTO>()
                .setQuery(query, GameDTO::class.java).build()
        adapter = ReserveAdapter(options, this)

        reserve_recycler.setHasFixedSize(true)
        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        reserve_recycler.layoutManager = layoutManager
        reserve_recycler.adapter = adapter

    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()

        val smoothScroller = object  : LinearSmoothScroller(this){
            override fun getVerticalSnapPreference(): Int {
                return super.getVerticalSnapPreference()
                return LinearSmoothScroller.SNAP_TO_END
            }
        }
        Handler().postDelayed(Runnable {
            Log.e("테스트", "${adapter.itemCount} / ${adapter.itemCount - 30}")
            smoothScroller.targetPosition = adapter.itemCount - 30
            layoutManager.startSmoothScroll(smoothScroller)
        }, 400)
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }

    override fun reserveClick(intent: Intent) {
        startActivity(intent)
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