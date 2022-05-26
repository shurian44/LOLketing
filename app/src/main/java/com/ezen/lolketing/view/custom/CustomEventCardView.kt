package com.ezen.lolketing.view.custom

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.ezen.lolketing.R
import com.ezen.lolketing.databinding.CustomEventCardBinding

class CustomEventCardView : ConstraintLayout {

    private val binding = CustomEventCardBinding.inflate(LayoutInflater.from(context))

    constructor(context: Context) : super(context) {
        initView()
    }

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
        initView()
        getAttrs(attributeSet)
    }

    constructor(context: Context, attributeSet: AttributeSet, defStyle : Int) : super(context, attributeSet, defStyle) {
        initView()
        getAttrs(attributeSet)
    }

    private fun initView() {
        addView(binding.root)
    }

    private fun getAttrs(attributeSet: AttributeSet){
        val typeArray = context.obtainStyledAttributes(attributeSet, R.styleable.CustomEventCardView)
        setTypeArray(typeArray)
    }

    private fun setTypeArray(typeArray: TypedArray) = with(binding) {
        val imageRes = typeArray.getResourceId(R.styleable.CustomEventCardView_eventImage, R.drawable.banner1)
        val title = typeArray.getString(R.styleable.CustomEventCardView_eventTitle) ?: ""
        val period = typeArray.getString(R.styleable.CustomEventCardView_eventPeriod) ?: ""

        image.setImageResource(imageRes)
        txtTitle.text = title
        txtPeriod.text = period

    }

}