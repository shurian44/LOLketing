package com.ezen.lolketing.view.custom

import android.content.Context
import android.util.AttributeSet
import android.widget.Checkable
import com.ezen.lolketing.R
import com.google.android.material.textview.MaterialTextView

class CustomCheckBox : MaterialTextView, Checkable {

    private var isChecked = false

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
        setView()
    }

    private fun setView() {
        if (isChecked) {
            setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_check_box_checked, 0, 0, 0)
        } else {
            setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_check_box_unchecked, 0, 0, 0)
        }
    }

    override fun setChecked(checked: Boolean) {
        isChecked = checked
        setView()
    }

    override fun isChecked(): Boolean = isChecked

    override fun toggle() {
        isChecked = !isChecked
        setView()
    }

}