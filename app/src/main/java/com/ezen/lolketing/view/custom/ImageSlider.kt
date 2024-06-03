package com.ezen.lolketing.view.custom

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.viewpager2.widget.ViewPager2
import com.ezen.lolketing.databinding.CustomImageSliderBinding
import com.ezen.lolketing.view.custom.adapter.ImageSliderAdapter
import com.google.android.material.tabs.TabLayoutMediator

class ImageSlider : ConstraintLayout {

    private val binding = CustomImageSliderBinding.inflate(LayoutInflater.from(context))
    private val adapter = ImageSliderAdapter()
    private val handler = Handler(Looper.getMainLooper())
    private var isUserScrolling = false

    private val slideRunnable = object : Runnable {
        override fun run() {
            val viewPager = binding.viewPager
            if (!isUserScrolling) {
                val nextPosition = (viewPager.currentItem + 1) % adapter.itemCount
                viewPager.currentItem = nextPosition
            }
            handler.postDelayed(this, 3000)
        }
    }

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

    fun setAutoSlider() {
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
                isUserScrolling = state != ViewPager2.SCROLL_STATE_IDLE
            }
        })

        handler.postDelayed(slideRunnable, 3000)
    }

    override fun removeDetachedView(child: View?, animate: Boolean) {
        super.removeDetachedView(child, animate)
        handler.removeCallbacks(slideRunnable)
    }

}