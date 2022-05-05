package com.ezen.lolketing.view.main.my_page

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.ezen.lolketing.view.main.MainActivity
import com.ezen.lolketing.R
import com.ezen.lolketing.adapter.CouponViewPagerAdapter
import com.ezen.lolketing.view.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_coupon.*

class CouponActivity : AppCompatActivity() {

    private var auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coupon)

        var list = ArrayList<Fragment>()    // ViewPager 에 들어갈 리스트
        list.add(CouponFragment("사용 안함"))
        list.add(CouponFragment("사용함"))
        var adapter = CouponViewPagerAdapter(supportFragmentManager, list)  // Viewpager 의 adapter
        myCoupon_viewPage.adapter = adapter
        tabLayout.setupWithViewPager(myCoupon_viewPage) //ViewPager 와 TabLayout 을 연결

        tabLayout.getTabAt(0)?.text = "사용 안함"
        tabLayout.getTabAt(1)?.text = "사용 함"
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