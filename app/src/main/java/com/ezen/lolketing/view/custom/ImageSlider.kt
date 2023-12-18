package com.ezen.lolketing.view.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.ezen.lolketing.databinding.CustomImageSliderBinding
import com.ezen.lolketing.view.custom.adapter.ImageSliderAdapter
import com.google.android.material.tabs.TabLayoutMediator

class ImageSlider : ConstraintLayout {

    private val binding = CustomImageSliderBinding.inflate(LayoutInflater.from(context))
    private val adapter = ImageSliderAdapter()

    constructor(context: Context) : super(context) {
        initView()
    }

    constructor(
        context: Context,
        attributeSet: AttributeSet
    ) : super(context, attributeSet) {
        initView()
    }

    constructor(
        context: Context,
        attributeSet: AttributeSet,
        defStyle: Int
    ) : super(context, attributeSet, defStyle) {
        initView()
    }

    private fun initView() = with(binding) {
        addView(root)
        root.layoutParams = LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.MATCH_PARENT
        )
        custom = this@ImageSlider
        viewPager.adapter = this@ImageSlider.adapter
        TabLayoutMediator(tabLayout, viewPager){ _, _ -> }.attach()
    }

    fun setImageList(list: List<Int>) {
        adapter.currentList.clear()
        adapter.submitList(list)
    }

    fun setImageAddressList(list: List<String>) {
        adapter.submitList(list)
    }

}