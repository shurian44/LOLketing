package com.ezen.lolketing.view.custom

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.MotionEvent
import androidx.core.content.ContextCompat
import com.ezen.lolketing.R
import com.ezen.lolketing.util.densityDp
import com.google.android.material.textfield.TextInputEditText

class CustomEditTextView : TextInputEditText {

    private var stateError = false

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
        setBackgroundResource(R.drawable.bg_round_outline_3_gray)
        setPadding(getPadding(), getPadding(), getPadding(), getPadding())
        setTextColor(ContextCompat.getColor(context, R.color.white))
        setHintTextColor(ContextCompat.getColor(context, R.color.gray))
        compoundDrawablePadding = densityDp(context, 16)
        gravity = Gravity.CENTER_VERTICAL

        onFocusChangeListener = OnFocusChangeListener { _, isFocused ->
            if (stateError) return@OnFocusChangeListener

            when(isFocused) {
                true -> {
                    setBackgroundResource(R.drawable.bg_round_outline_3_white)
                }
                false -> {
                    setBackgroundResource(R.drawable.bg_round_outline_3_gray)
                }
            }
        }
    }

    private fun getPadding() : Int =
        densityDp(context, 12)

    fun setStateError(isError: Boolean) {
        stateError = isError

        when(isError) {
            true -> {
                setBackgroundResource(R.drawable.bg_round_outline_3_red)
            }
            false -> {
                when(isFocused) {
                    true -> {
                        setBackgroundResource(R.drawable.bg_round_outline_3_white)
                    }
                    false -> {
                        setBackgroundResource(R.drawable.bg_round_outline_3_gray)
                    }
                }
            }
        }
    }

    fun getStateError() = stateError

    @SuppressLint("ClickableViewAccessibility")
    fun setDrawableClickListener(drawablePosition: Int, listener: () -> Unit) {
        setOnTouchListener { _, motionEvent ->
            if (motionEvent.action == MotionEvent.ACTION_UP) {
                if (motionEvent.rawX >= (right - compoundDrawables[drawablePosition].bounds.width())) {
                    listener()
                    return@setOnTouchListener true
                }
            }
            return@setOnTouchListener false
        }
    }

    companion object {
        const val DRAWABLE_LEFT = 0
        const val DRAWABLE_TOP = 1
        const val DRAWABLE_RIGHT = 2
        const val DRAWABLE_BOTTOM = 3
    }

}