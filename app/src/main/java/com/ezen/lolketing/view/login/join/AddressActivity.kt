package com.ezen.lolketing.view.login.join

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.ezen.lolketing.BaseViewModelActivity
import com.ezen.lolketing.R
import com.ezen.lolketing.databinding.ActivityAddressBinding
import com.ezen.lolketing.model.SearchAddressResult
import com.ezen.lolketing.util.repeatOnStarted
import com.ezen.lolketing.util.toast
import com.ezen.lolketing.view.custom.CustomEditTextView
import com.ezen.lolketing.view.dialog.adater.AddressAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class AddressActivity : BaseViewModelActivity<ActivityAddressBinding, AddressViewModel>(R.layout.activity_address) {

    override val viewModel: AddressViewModel by viewModels()
    private var isMoreData = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        repeatOnStarted {
            viewModel.eventFlow.collect { event -> eventHandler(event) }
        }
        initViews()

    }

    private fun eventHandler(event: AddressViewModel.Event) {
        when(event) {
            is AddressViewModel.Event.AddressSearchSuccess -> {
                setRecyclerView(event.list)
                isMoreData = event.isMoreData
            }
            is AddressViewModel.Event.Error -> {
                binding.txtGuide.apply {
                    isVisible = true
                    text = event.msg
                }
            }
        }
    }

    private fun initViews() = with(binding) {
        activity = this@AddressActivity

        editAddress.setOnKeyListener { _, keyCode, keyEvent ->
            if (keyEvent.action == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_ENTER) {
                searchAddress()
                return@setOnKeyListener false
            }
            false
        }

        editAddress.setDrawableClickListener(CustomEditTextView.DRAWABLE_RIGHT) {
            searchAddress()
        }

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (isMoreData && !recyclerView.canScrollVertically(1) ) {
                    viewModel.selectAddress("", false)
                }
            }
        })

    }

    private fun searchAddress() = with(binding) {
        editAddressDetail.isVisible = false
        viewLine.isVisible = true

        val adapter = AddressAdapter{
            editAddress.setText(it)
            editAddressDetail.isVisible = true
            viewLine.isVisible = false
            recyclerView.isVisible = false
            recyclerView.adapter = null
        }
        recyclerView.adapter = adapter

        viewModel.selectAddress(editAddress.text.toString(), true)
    }

    private fun setRecyclerView(list: List<SearchAddressResult>) = with(binding) {
        txtGuide.isVisible = false
        recyclerView.isVisible = true

        (binding.recyclerView.adapter as? AddressAdapter)?.apply {
            submitList(currentList + list)
        }
    }

    fun addressRegister(view: View) = with(binding) {
        if (editAddressDetail.isVisible.not()) {
            toast(getString(R.string.guide_input_address))
            return
        }

        val address =
            if (editAddressDetail.text.toString().isEmpty()) {
                editAddress.text.toString()
            } else {
                "${editAddress.text.toString()}, ${editAddressDetail.text.toString()}"
            }

        setResult(Activity.RESULT_OK, Intent().also {
            it.putExtra(JoinDetailActivity.ADDRESS, address)
        })
        finish()
    }

}