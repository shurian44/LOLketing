package com.ezen.lolketing.view.main.my_page

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.ezen.lolketing.BaseActivity
import com.ezen.lolketing.view.main.MainActivity
import com.ezen.lolketing.R
import com.ezen.lolketing.adapter.CouponViewPagerAdapter
import com.ezen.lolketing.databinding.ActivityCouponBinding
import com.ezen.lolketing.view.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth

class CouponActivity : BaseActivity<ActivityCouponBinding>(R.layout.activity_coupon) {

    private var auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var list = ArrayList<Fragment>()    // ViewPager 에 들어갈 리스트
        list.add(CouponFragment("사용 안함"))
        list.add(CouponFragment("사용함"))
        var adapter = CouponViewPagerAdapter(supportFragmentManager, list)  // Viewpager 의 adapter
        binding.myCouponViewPage.adapter = adapter
        binding.tabLayout.setupWithViewPager(binding.myCouponViewPage) //ViewPager 와 TabLayout 을 연결

        binding.tabLayout.getTabAt(0)?.text = "사용 안함"
        binding.tabLayout.getTabAt(1)?.text = "사용 함"
    }

    override fun logout(view: View) {
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