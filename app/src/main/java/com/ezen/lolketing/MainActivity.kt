package com.ezen.lolketing

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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

    fun moveTeamActivity(view: View) {
        var intent = Intent(this, TeamActivity::class.java)
        when(view.id){
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

    fun checkDetailJoin(){
        firestore.collection("Users").document(auth.currentUser?.email!!).addSnapshotListener { documentSnapshot, firebaseFirestoreException ->
            var user = documentSnapshot?.toObject(Users::class.java) ?: return@addSnapshotListener
            if(user.nickname == null){
                startActivity(Intent(this, JoinDetailActivity::class.java))
            }
        }
    }
}
