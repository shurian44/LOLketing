package com.ezen.lolketing

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.ezen.lolketing.model.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var auth = FirebaseAuth.getInstance()
    var firestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        checkDetailJoin()

        btn_board.setOnClickListener {

            var builder = AlertDialog.Builder(this)
            var view = layoutInflater.inflate(R.layout.dialog_team, null)
            builder.setView(view)
            val dialog = builder.show()
            var intent = Intent(this, BoardListActivity::class.java)
            view.findViewById<ImageView>(R.id.icon_t1).setOnClickListener {
                intent.putExtra("team", "T1")
                startActivity(intent)
            }
            view.findViewById<ImageView>(R.id.icon_griffin).setOnClickListener {
                intent.putExtra("team", "GRIFFIN")
                startActivity(intent)
            }
            view.findViewById<ImageView>(R.id.icon_geng).setOnClickListener {
                intent.putExtra("team", "Gen.G Esports")
                startActivity(intent)
            }
            view.findViewById<ImageView>(R.id.icon_dragonx).setOnClickListener {
                intent.putExtra("team", "DragonX")
                startActivity(intent)
            }
            view.findViewById<ImageView>(R.id.icon_freecs).setOnClickListener {
                intent.putExtra("team", "Afreeca Freecs")
                startActivity(intent)
            }
            view.findViewById<ImageView>(R.id.icon_sandbox).setOnClickListener {
                intent.putExtra("team", "SANDBOX Gamming")
                startActivity(intent)
            }
            view.findViewById<ImageView>(R.id.icon_damwon).setOnClickListener {
                intent.putExtra("team", "DAMWON Gamming")
                startActivity(intent)
            }
            view.findViewById<ImageView>(R.id.icon_apk).setOnClickListener {
                intent.putExtra("team", "APK Prince")
                startActivity(intent)
            }
            view.findViewById<ImageView>(R.id.icon_rolster).setOnClickListener {
                intent.putExtra("team", "kt Rolster")
                startActivity(intent)
            }
            view.findViewById<ImageView>(R.id.icon_hanhwa).setOnClickListener {
                intent.putExtra("team", "Hanwha Life Esports")
                startActivity(intent)
            }
        }

        //progressDialog(title = "제목", message = "잠시만 기다려주세요")
    }

    fun logout(view: View) {
        auth.signOut()
        var intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
    }

    fun managerPage(view: View) {
        startActivity(Intent(this, JsonActivity::class.java))
    }

    fun moveActivity(view: View) {
        var intent = Intent(this, BoardListActivity::class.java)
        when(view.id){
            R.id.btn_event-> intent = Intent(this, EventListActivity::class.java)
            R.id.btn_myPage-> intent = Intent(this, MyPageActivity::class.java)
            R.id.btn_info-> intent = Intent(this, LeagueInfoActivity::class.java)
            R.id.btn_reserve-> intent = Intent(this, ReserveListActivity::class.java)
            R.id.btn_shop-> intent = Intent(this, ShopActivity::class.java)
            R.id.btn_guid-> intent = Intent(this, LoLGuideActivity::class.java)
            R.id.btn_news-> intent = Intent(this, NewsActivity::class.java)
            R.id.btn_chatting-> intent = Intent(this, ChattingListActivity::class.java)
        }
        startActivity(intent)
    }

    private fun checkDetailJoin(){
        firestore.collection("Users").document(auth.currentUser?.email!!).addSnapshotListener { documentSnapshot, firebaseFirestoreException ->
            var user = documentSnapshot?.toObject(Users::class.java) ?: return@addSnapshotListener
            if(user.nickname == null){
                startActivity(Intent(this, JoinDetailActivity::class.java))
            }
            if(user.grade == "마스터"){
                btn_manager.visibility = View.VISIBLE
            }
        }
    }
}
