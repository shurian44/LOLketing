package com.ezen.lolketing

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
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
        main_logo.setOnClickListener {
            startActivity(Intent(this, JsonActivity::class.java))
        }
    }

    fun logout(view: View) {
        auth.signOut()
        var intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
    }

    fun moveActivity(view: View) {
        var intent = Intent(this, BoardListActivity::class.java)
        when(view.id){
            R.id.btn_notice-> intent.putExtra("team", "notice")
            R.id.btn_event-> intent = Intent(this, RouletteActivity::class.java)
            R.id.btn_myPage-> intent = Intent(this, MyPageActivity::class.java)
            R.id.btn_info-> intent = Intent(this, LeagueInfoActivity::class.java)
            R.id.btn_reserve-> intent = Intent(this, ReserveListActivity::class.java)
            R.id.btn_shop-> intent = Intent(this, ShopActivity::class.java)
            R.id.btn_guid-> intent = Intent(this, LoLGuideActivity::class.java)
            R.id.btn_news-> intent = Intent(this, NewsActivity::class.java)
            R.id.btn_chatting-> intent = Intent(this, ChattingListActivity::class.java)
            R.id.icon_t1 -> intent.putExtra("team", "T1")
            R.id.icon_griffin -> intent.putExtra("team", "GRIFFIN")
            R.id.icon_damwon -> intent.putExtra("team", "DAMWON Gamming")
            R.id.icon_sendbox -> intent.putExtra("team", "SANDBOX Gamming")
            R.id.icon_freecs -> intent.putExtra("team", "Afreeca Freecs")
            R.id.icon_geng -> intent.putExtra("team", "Gen.G Esports")
            R.id.icon_dragonx -> intent.putExtra("team", "DragonX")
            R.id.icon_rolster -> intent.putExtra("team", "kt Rolster")
            R.id.icon_prince -> intent.putExtra("team", "APK Prince")
            R.id.icon_hanhwa-> intent.putExtra("team", "Hanwha Life Esports")
        }
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
    }

    private fun checkDetailJoin(){
        firestore.collection("Users").document(auth.currentUser?.email!!).addSnapshotListener { documentSnapshot, firebaseFirestoreException ->
            var user = documentSnapshot?.toObject(Users::class.java) ?: return@addSnapshotListener
            if(user.nickname == null){
                startActivity(Intent(this, JoinDetailActivity::class.java))
            }
        }
    }
}
