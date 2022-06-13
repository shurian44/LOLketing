package com.ezen.lolketing.view.custom

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.ezen.lolketing.R
import com.ezen.lolketing.databinding.CustomEventCardBinding
import com.ezen.lolketing.databinding.CustomLimitedEditTextViewBinding

class CustomLimitedEditTextView : ConstraintLayout {

    private val binding = CustomLimitedEditTextViewBinding.inflate(LayoutInflater.from(context))

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


    }

}