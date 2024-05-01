package com.ezen.lolketing.view.custom

import android.content.Context
import android.content.res.TypedArray
import android.text.InputFilter
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import com.ezen.lolketing.R
import com.ezen.lolketing.databinding.CustomLimitedEditTextViewBinding
import com.ezen.lolketing.util.densityDp
import com.ezen.lolketing.util.hideKeyboard
import com.ezen.lolketing.util.showKeyboard

class CustomLimitedEditTextView : ConstraintLayout {

    private val binding = CustomLimitedEditTextViewBinding.inflate(LayoutInflater.from(context))
    private var limited = 600
    private var listener : ((String) -> Unit)? = null

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

    private fun initView() = with(binding) {
        addView(root)

        root.setOnClickListener {
            editTextView.requestFocus()
        }

        editTextView.addTextChangedListener {
            val length = editTextView.text.toString().length

            txtLimit.text = "$length / $limited"
        }

        editTextView.setOnFocusChangeListener { view, isFocused ->
            if(isFocused) {
                view.showKeyboard()
                cardView.strokeColor = ContextCompat.getColor(context, R.color.main_color)
            } else {
                view.hideKeyboard()
                cardView.strokeColor = ContextCompat.getColor(context, R.color.white)
            }
        }

        txtRegister.setOnClickListener {
            listener?.invoke(editTextView.text.toString())
        }

    }

    private fun getAttrs(attributeSet: AttributeSet){
        val typeArray = context.obtainStyledAttributes(attributeSet, R.styleable.CustomLimitedEditTextView)
        setTypeArray(typeArray)
    }

    private fun setTypeArray(typeArray: TypedArray) = with(binding) {
        limited = typeArray.getInt(R.styleable.CustomLimitedEditTextView_limited, 600)
        val isVisible = typeArray.getBoolean(R.styleable.CustomLimitedEditTextView_registerBtnIsVisible, false)
        val minimumHeight = typeArray.getInt(R.styleable.CustomLimitedEditTextView_minimumHeight, 0)

        editTextView.filters = arrayOf(InputFilter.LengthFilter(limited))
        txtLimit.text = "0 / $limited"
        txtRegister.isVisible = isVisible

        (viewEmpty.layoutParams as? LayoutParams)?.also {
            it.topMargin = densityDp(context, minimumHeight)
            viewEmpty.layoutParams = it
        }
    }

    fun setLimited(limited: Int) {
        this.limited = limited
        binding.editTextView.filters = arrayOf(InputFilter.LengthFilter(limited))
        binding.txtLimit.text = "${binding.editTextView.text.toString().length} / $limited"
    }

    fun setRegisterButtonVisible(isVisible : Boolean) {
        binding.txtRegister.isVisible = isVisible
    }

    fun setRegisterListener(listener: (String) -> Unit) {
        this.listener = listener
    }

    fun setText(text: String?) {
        binding.editTextView.setText(text)
    }

    fun getText() : String = binding.editTextView.text.toString()

    fun getEditTextView() = binding.editTextView

}