package com.ezen.lolketing.view.custom

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.widget.Checkable
import com.ezen.lolketing.R
import com.google.android.material.textview.MaterialTextView

class CustomSeatCheckBox : MaterialTextView, Checkable {

    private var onClick: () -> Unit = {}

    private var status = false

    constructor(context: Context) : super(context) {
        initView()
    }

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
        getAttrs(attributeSet)
        initView()
    }

    constructor(context: Context, attributeSet: AttributeSet, defStyle : Int) : super(context, attributeSet, defStyle) {
        getAttrs(attributeSet)
        initView()
    }

    private fun initView() {
        setOnClickListener { onClick() }

        if (isEnabled.not()) {
            setBackgroundResource(R.drawable.bg_round_fill_3_light_gray)
            return
        }

        setBackgroundResource(
            if (status) {
                R.drawable.bg_round_white_outline_fill_main_color
            } else {
                R.drawable.bg_round_outline_3_white
            }
        )
    }

    private fun getAttrs(attributeSet: AttributeSet){
        val typeArray = context.obtainStyledAttributes(attributeSet, R.styleable.CustomSeatCheckBox)
        setTypeArray(typeArray)
    }

    private fun setTypeArray(typeArray: TypedArray) {
        val checked = typeArray.getBoolean(R.styleable.CustomSeatCheckBox_seatChecked, false)
        val enabled = typeArray.getBoolean(R.styleable.CustomSeatCheckBox_seatEnable, true)

        isEnabled = enabled

        status = checked

    }

    fun setOnClick(onClick: () -> Unit) {
        this.onClick = onClick
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        initView()
    }

    override fun setChecked(checked: Boolean) {
        status = checked
        initView()
    }

    override fun isChecked(): Boolean = status

    override fun toggle() {
        status = status.not()
        initView()
    }
}