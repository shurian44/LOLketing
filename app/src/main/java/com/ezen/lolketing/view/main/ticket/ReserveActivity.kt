package com.ezen.lolketing.view.main.ticket

import android.os.Bundle
import com.ezen.lolketing.BaseActivity
import com.ezen.lolketing.R
import com.ezen.lolketing.databinding.ActivityReserveBinding

class ReserveActivity : BaseActivity<ActivityReserveBinding>(R.layout.activity_reserve) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.apply {
            layoutTop.btnBack.setOnClickListener { onBackClick(it) }
        }

    }// onCreate()

}