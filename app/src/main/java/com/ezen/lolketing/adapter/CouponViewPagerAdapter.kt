package com.ezen.lolketing.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class CouponViewPagerAdapter(
    fm : FragmentManager,
    private val list : ArrayList<Fragment>
) : FragmentPagerAdapter(fm) {
    // todo Deprecated 수정 필요

    override fun getItem(position: Int): Fragment {
        return list[position]
    }

    override fun getCount(): Int {
        return list.size
    }
}