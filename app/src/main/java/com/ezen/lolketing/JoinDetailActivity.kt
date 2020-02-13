package com.ezen.lolketing

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.ezen.lolketing.model.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_join_detail.*


class JoinDetailActivity : AppCompatActivity() {
    private val SEARCH_ADDRESS_ACTIVITY = 10000
    private var firestore = FirebaseFirestore.getInstance()
    private var auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join_detail)

        textChanged()

        join_detail_check.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                layout_check.visibility = View.GONE
                layout_register.visibility = View.VISIBLE
            }
        }

        btn_address.setOnClickListener {
            val i = Intent(this, DaumWebViewActivity::class.java)
            startActivityForResult(i, SEARCH_ADDRESS_ACTIVITY)
        }

        btn_register.setOnClickListener {
            var user = Users()
            var email = auth.currentUser?.email!!
            var address = "${join_detail_address?.text.toString()}, ${join_detail_address2?.text.toString()}"
            user.id = email
            user.uid = auth.currentUser?.uid
            user.address = address
            user.nickname = join_detail_nickname?.text.toString()
            user.phone = join_detail_phone?.text.toString()
            firestore.collection("Users").document(email).set(user)
            finish()
        }
    }

    private fun textChanged(){
        // 특수문자 제한
        join_detail_phone.filters = arrayOf(InputFilter { source, start, end, dest, dstart, dend ->
            for (i in start until end) {
                if (!Character.isLetterOrDigit(source[i])) {
                    return@InputFilter ""
                }
            }
            null
        })

        join_detail_nickname.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(join_detail_nickname.length() !in 2..10) join_detail_nickname.error = "닉네임은 2~10만 가능합니다."
            }

        })

        join_detail_phone.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(join_detail_phone.length() !in 10..11) join_detail_phone.error = "전화번호를 확인해 주세요"
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)

        when (requestCode) {
            SEARCH_ADDRESS_ACTIVITY -> if (resultCode === Activity.RESULT_OK) {
                val data = intent?.extras!!.getString("data")
                if (data != null) join_detail_address.text = Editable.Factory.getInstance().newEditable(data)
            }
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if(event?.action == KeyEvent.ACTION_DOWN){
            if(keyCode == KeyEvent.KEYCODE_BACK ){
                var intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
                auth.signOut()
                finish()
                return false
            }
        }
        return super.onKeyDown(keyCode, event)
    }
}