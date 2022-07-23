package com.ezen.lolketing.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class LeagueInfoAdapter(fragmentActivity: FragmentActivity): FragmentStateAdapter(fragmentActivity) {
    private var fragments : ArrayList<Fragment> = ArrayList()

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment = fragments[position]

    fun addFragment(fragment: Fragment) {
        fragments.add(fragment)
    }

}


