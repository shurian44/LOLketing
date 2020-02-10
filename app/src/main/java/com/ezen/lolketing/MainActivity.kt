package com.ezen.lolketing

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        icon_griffin_layout.setOnClickListener {
//            startActivity(Intent(this, JsonActivity::class.java))
//        }
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
            R.id.icon_t1_layout -> intent.putExtra("team", "T1")
            R.id.icon_griffin_layout -> intent.putExtra("team", "GRIFFIN")
            R.id.icon_damwon_layout -> intent.putExtra("team", "DAMWON Gamming")
            R.id.icon_sendbox_layout -> intent.putExtra("team", "SANDBOX Gamming")
            R.id.icon_freecs_layout -> intent.putExtra("team", "Afreeca Freecs")
            R.id.icon_geng_layout -> intent.putExtra("team", "Gen.G Esports")
            R.id.icon_dragonx_layout -> intent.putExtra("team", "DragonX")
            R.id.icon_rolster_layout -> intent.putExtra("team", "kt Rolster")
            R.id.icon_prince_layout -> intent.putExtra("team", "APK Prince")
            R.id.icon_hanwha_layout-> intent.putExtra("team", "Hanwha Life Esports")
        }
        startActivity(intent)
    }
}
