package com.ezen.lolketing.view.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.ezen.lolketing.databinding.CustomChattingItemBinding
import com.ezen.lolketing.util.setTeamLogoImageView

class CustomChattingItem : ConstraintLayout{

    private val binding = CustomChattingItemBinding.inflate(LayoutInflater.from(context))

    constructor(context: Context) : super(context) {
        initView()
    }

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
        initView()
    }

    constructor(context: Context, attributeSet: AttributeSet, defStyle : Int) : super(context, attributeSet, defStyle) {
        initView()
    }

    private fun initView() {
        addView(binding.root)
    }

    fun setChattingItem(
        leftTeam: String,
        rightTeam: String,
        time: String,
        clickListener: (String) -> Unit
    ) = with(binding) {
        setTeamLogoImageView(imgTeamLeft, leftTeam)
        setTeamLogoImageView(imgTeamRight, rightTeam)
        txtTime.text = time
        imgTeamLeft.setOnClickListener {
            clickListener("L")
        }
        imgTeamRight.setOnClickListener {
            clickListener("R")
        }
    }

    override fun measureChild(
        child: View?,
        parentWidthMeasureSpec: Int,
        parentHeightMeasureSpec: Int
    ) {
        super.measureChild(child, parentWidthMeasureSpec, parentHeightMeasureSpec)
    }

}