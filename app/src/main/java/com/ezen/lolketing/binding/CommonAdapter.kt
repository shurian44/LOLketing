package com.ezen.lolketing.binding

import android.annotation.SuppressLint
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ezen.lolketing.view.custom.CustomEditTextView
import com.ezen.lolketing.view.custom.CustomLimitedEditTextView
import com.ezen.lolketing.view.custom.ImageSlider

@BindingAdapter("adapter")
fun bindAdapter(view: RecyclerView, adapter: RecyclerView.Adapter<*>) {
    view.adapter = adapter.apply {
        stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
    }
}

@BindingAdapter("submitList")
fun bindSubmitList(view: RecyclerView, itemList: List<Any>?) {
    (view.adapter as? ListAdapter<Any, *>)?.submitList(itemList)
}

@BindingAdapter("imageRes")
fun imageLoad(imageView: ImageView, @DrawableRes resId: Int) {
    imageView.setImageResource(resId)
}

@BindingAdapter("sliderImage")
fun imageListLoad(slider: ImageSlider, list: List<Int>) {
    slider.setImageList(list)
}

@BindingAdapter("isVisible")
fun bindIsVisible(view: View, isVisible: Boolean) {
    view.isVisible = isVisible
}

@SuppressLint("ClickableViewAccessibility")
@BindingAdapter("editSearch")
fun bindEditTextViewSearch(editText: EditText, onSearch: () -> Unit) {
    editText.setOnKeyListener { _, keyCode, keyEvent ->
        // 엔터 키를 이용한 검색
        if (keyEvent.action == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_ENTER) {
            onSearch()
            return@setOnKeyListener false
        }
        false
    }

    editText.setOnEditorActionListener { _, actionId, _ ->
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            onSearch()
            return@setOnEditorActionListener false
        }
        false
    }

    editText.setOnTouchListener { _, motionEvent ->
        if (motionEvent.action == MotionEvent.ACTION_UP) {
            if (motionEvent.rawX >= (editText.right - editText.compoundDrawables[CustomEditTextView.DRAWABLE_END].bounds.width())) {
                onSearch()
                return@setOnTouchListener true
            }
        }
        return@setOnTouchListener false
    }
}