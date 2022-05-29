package com.ezen.lolketing.view.custom

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.widget.LinearLayout
import com.ezen.lolketing.R

class CustomSeatRowLayout : LinearLayout {

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
        orientation = HORIZONTAL
    }

    private fun getAttrs(attributeSet: AttributeSet){
        val typeArray = context.obtainStyledAttributes(attributeSet, R.styleable.CustomSeatRowLayout)
        setTypeArray(typeArray)
    }

    private fun setTypeArray(typeArray: TypedArray) {
        val checkBox = CustomSeatCheckBox(context)
        val startStr = typeArray.getString(R.styleable.CustomSeatRowLayout_startStr) ?: ""
        val size = typeArray.getInt(R.styleable.CustomSeatRowLayout_seatSize, 0)

        for (i in 1..size) {
            checkBox.text = "$size$i"
        }


    }

}