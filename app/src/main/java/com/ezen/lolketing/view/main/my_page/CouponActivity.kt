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
import com.ezen.lolketing.util.startActivity
import com.ezen.lolketing.view.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CouponActivity : BaseActivity<ActivityCouponBinding>(R.layout.activity_coupon) {

    @Inject lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initViews()

    }

    private fun initViews() = with(binding) {
        activity = this@CouponActivity
        // todo 뷰페이저 관련
        val list = ArrayList<Fragment>()    // ViewPager 에 들어갈 리스트
        list.add(CouponFragment("사용 안함"))
        list.add(CouponFragment("사용함"))
        val adapter = CouponViewPagerAdapter(supportFragmentManager, list)  // Viewpager 의 adapter
        myCouponViewPage.adapter = adapter
        tabLayout.setupWithViewPager(myCouponViewPage) //ViewPager 와 TabLayout 을 연결

        tabLayout.getTabAt(0)?.text = "사용 안함"
        tabLayout.getTabAt(1)?.text = "사용 함"
    }

    override fun logout(view: View) {
        auth.signOut()
        startActivity(LoginActivity::class.java, Intent.FLAG_ACTIVITY_CLEAR_TOP)
        finish()
    }

    fun moveHome(view: View) {
        startActivity(MainActivity::class.java, Intent.FLAG_ACTIVITY_CLEAR_TOP)
        finish()
    }
}