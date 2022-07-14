package com.ezen.lolketing.view.main.league_info

import android.os.Bundle
import com.ezen.lolketing.BaseActivity
import com.ezen.lolketing.R
import com.ezen.lolketing.adapter.LeagueInfoAdapter
import com.ezen.lolketing.databinding.ActivityLeagueInfoBinding
import com.ezen.lolketing.fragment.OutlineFragment
import com.ezen.lolketing.fragment.PrizeFragment
import com.ezen.lolketing.fragment.ProgressFragment
import com.google.android.material.tabs.TabLayoutMediator

class LeagueInfoActivity : BaseActivity<ActivityLeagueInfoBinding>(R.layout.activity_league_info) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initViews()

    } // onCreate()

    private fun initViews() = with(binding) {
        title = "리그 정보"
        layoutTop.btnBack.setOnClickListener { finish() }
        val list = listOf("개요", "진행 방식", "대회 상금")
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
