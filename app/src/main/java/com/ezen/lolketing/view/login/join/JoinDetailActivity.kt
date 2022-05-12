package com.ezen.lolketing.view.login.join

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import com.ezen.lolketing.BaseActivity
import com.ezen.lolketing.view.login.DaumWebViewActivity
import com.ezen.lolketing.R
import com.ezen.lolketing.databinding.ActivityJoinDetailBinding
import com.ezen.lolketing.model.Coupon
import com.ezen.lolketing.model.Users
import com.ezen.lolketing.view.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class JoinDetailActivity : BaseActivity<ActivityJoinDetailBinding>(R.layout.activity_join_detail) {
    private val SEARCH_ADDRESS_ACTIVITY = 10000
    private var firestore = FirebaseFirestore.getInstance()
    private var auth = FirebaseAuth.getInstance()
    private var classify: String ?= null
    private var user = Users()
    private lateinit var id : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        id = auth.currentUser?.email!!
        editTextConstraints() // EditText 제한조건
        // classify : null - 회원가입 상세 / modify - 회원정보 수정
        classify = intent.getStringExtra("modify") ?: null

        // 회원정보 수정일 경우
        if(classify == "modify"){
            setModifyLayout() // 회원 정보 수정용으로 레이아웃 설정
            // 기존 회원의 정보 가져오기
            firestore.collection("Users").document(auth.currentUser?.email!!).get().addOnCompleteListener {
                user = it.result?.toObject(Users::class.java)!!
            }
        }

        // 이용약관 체크박스 채크
        binding.joinDetailCheck.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                // 이용약관 레이아웃 감추고 데이터 입력 레이아웃 보여주기
                binding.layoutCheck.visibility = View.GONE
                binding.layoutRegister.visibility = View.VISIBLE
            }
        }

        // 주소 검색버튼 클릭
        binding.btnAddress.setOnClickListener {
            // 다음 API가 있는 페이지로 이동
            val intent = Intent(this, DaumWebViewActivity::class.java)
            startActivityForResult(intent, SEARCH_ADDRESS_ACTIVITY)
        }
    } // onCreate()

    // 등록하기 버튼 클릭
    fun setUser(view: View) {
        var address = "${binding.joinAddress?.text.toString()}, ${binding.joinDetailAddress?.text.toString()}"

        user.id = id
        user.uid = auth.currentUser?.uid
        user.address = address
        user.nickname = binding.joinDetailNickname?.text.toString()
        user.phone = binding.joinDetailPhone?.text.toString()

        // 회원가입 상세의 경우
        if(classify != "modify"){
            // 등급을 브론즈로 설정
            user.grade = "브론즈"
            // 신규 가입 쿠폰 지급
            var newUserCoupon = Coupon()
            newUserCoupon.id = id
            newUserCoupon.title = "신규 가입 쿠폰"
            newUserCoupon.limit = "2222.01.01"
            firestore.collection("Coupon").document().set(newUserCoupon)
        }

        // 유저 정보 수정
        firestore.collection("Users").document(id).set(user).addOnCompleteListener {
            if(it.isComplete){
                finish()
            }
        }
    } // setUser()

    // EditText 제한조건
    private fun editTextConstraints(){
        // 특수문자 제한 : 입력 시 빈 칸 리턴
        binding.joinDetailPhone.filters = arrayOf(InputFilter { source, start, end, dest, dstart, dend ->
            for (i in start until end) {
                if (!Character.isLetterOrDigit(source[i])) {
                    return@InputFilter ""
                }
            }
            null
        })

        binding.joinDetailNickname.filters = arrayOf(InputFilter { source, start, end, dest, dstart, dend ->
            for (i in start until end) {
                if (!Character.isLetterOrDigit(source[i])) {
                    return@InputFilter ""
                }
            }
            null
        })

        // 닉네임 길이 체크
        binding.joinDetailNickname.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(binding.joinDetailNickname.length() !in 2..10) binding.joinDetailNickname.error = "닉네임은 2~10만 가능합니다."
            }
        })

        // 전화번호 길이 체크
        binding.joinDetailPhone.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(binding.joinDetailPhone.length() !in 10..11) binding.joinDetailPhone.error = "전화번호를 확인해 주세요"
            }
        })
    }

    // 회원정보 수정 시 레이아웃 세팅
    private fun setModifyLayout(){
        // 이용약관 레이아웃 감추고 데이터 입력 레이아웃 보여주기
        binding.layoutCheck.visibility = View.GONE
        binding.layoutRegister.visibility = View.VISIBLE

        firestore.collection("Users").document(auth.currentUser?.email!!).get().addOnCompleteListener {
            var user = it.result?.toObject(Users::class.java)!!
            var address = user.address!!.split(",")

            binding.joinDetailNickname.setText(user.nickname)
            binding.joinDetailPhone.setText(user.phone)
            binding.btnRegister.text = "수정하기"

            // 데이터베이스에 있는 주소를 일반 주소와 상세 주소로 나누어서 EditText에 채워준다.
            if(address.size > 1)
                binding.joinAddress.setText("${address[0]},${address[1]}")
            if(address.size > 2)
                binding.joinDetailAddress.setText("${address[2]}")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)

        if (requestCode == SEARCH_ADDRESS_ACTIVITY && resultCode === Activity.RESULT_OK) {
            // 다음 API에서 받아온 주소 정보를 join_address 에 채워준다.
            val data = intent?.extras!!.getString("data")
            if (data != null)
                binding.joinAddress.text = Editable.Factory.getInstance().newEditable(data)
        }

    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        // 키를 눌렀을 때 이벤트
        if(event?.action == KeyEvent.ACTION_DOWN){
            // 상세 회원가입이고 백 키를 눌렀을 때 로그아웃 시키고 로그인 페이지로 이동
            if(keyCode == KeyEvent.KEYCODE_BACK && classify != "modify"){
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

    override fun logout(view: View) {}

}