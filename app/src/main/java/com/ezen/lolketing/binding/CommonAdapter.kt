package com.ezen.lolketing.binding

import android.annotation.SuppressLint
import android.net.Uri
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
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

@BindingAdapter("viewPagerAdapter")
fun bindViewPagerAdapter(view: ViewPager2, adapter: RecyclerView.Adapter<*>) {
    view.adapter = adapter.apply {
        stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
    }
}

@BindingAdapter("viewPagerSubmitList")
fun bindViewPagerSubmitList(view: ViewPager2, itemList: List<Any>?) {
    (view.adapter as? ListAdapter<Any, *>)?.submitList(itemList)
}

@BindingAdapter("imageRes")
fun imageLoad(imageView: ImageView, @DrawableRes resId: Int) {
    imageView.setImageResource(resId)
}

@BindingAdapter("sliderAddressImage")
fun imageListLoad(slider: ImageSlider, list: List<String>) {
    slider.setImageAddressList(list)
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

@BindingAdapter("imageAddress")
fun loadGlideImage(imageView: ImageView, imageAddress: String?) {
    if (imageAddress.isNullOrEmpty()) return

    Glide
        .with(imageView.context)
        .load(imageAddress)
        .into(imageView)
}

@BindingAdapter("imageUri")
fun loadGlideImageUri(imageView: ImageView, uri: Uri?) {
    if (uri == null) return

    Glide
        .with(imageView.context)
        .load(uri)
        .fitCenter()
        .into(imageView)
}

@BindingAdapter("android:text")
fun setText(view: CustomLimitedEditTextView, text: String?) {
    if (view.getText() != text) {
        view.setText(text ?: "")
    }
}

@InverseBindingAdapter(attribute = "android:text")
fun getText(view: CustomLimitedEditTextView): String {
    return view.getText()
}

@BindingAdapter("android:textAttrChanged")
fun setTextWatcher(view: CustomLimitedEditTextView, listener: InverseBindingListener?) {
    val watcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(s: Editable?) {
            listener?.onChange()
        }
    }
    view.getEditTextView().addTextChangedListener(watcher)
}
