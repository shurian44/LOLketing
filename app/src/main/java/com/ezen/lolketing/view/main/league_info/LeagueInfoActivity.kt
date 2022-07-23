package com.ezen.lolketing.view.main.league_info

import android.os.Bundle
import com.ezen.lolketing.BaseActivity
import com.ezen.lolketing.R
import com.ezen.lolketing.adapter.LeagueInfoAdapter
import com.ezen.lolketing.databinding.ActivityLeagueInfoBinding
import com.google.android.material.tabs.TabLayoutMediator

class LeagueInfoActivity : BaseActivity<ActivityLeagueInfoBinding>(R.layout.activity_league_info) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initViews()

    } // onCreate()

    private fun initViews() = with(binding) {
        title = getString(R.string.league_info)
        layoutTop.btnBack.setOnClickListener { finish() }
        val list = listOf(getString(R.string.menu_summary), getString(R.string.menu_proceeding), getString(R.string.menu_prize_money))
        val leagueInfoAdapter = LeagueInfoAdapter(this@LeagueInfoActivity).also {
            it.addFragment(OutlineFragment())
            it.addFragment(ProgressFragment())
            it.addFragment(PrizeFragment())
        }

        viewPager.adapter = leagueInfoAdapter
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = list[position]
        }.attach()
    }

} // class
