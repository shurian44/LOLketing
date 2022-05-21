package com.ezen.lolketing.view.dialog

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.ezen.lolketing.BR
import com.ezen.lolketing.R
import com.ezen.lolketing.databinding.DialogJoinBinding
import com.ezen.lolketing.model.SearchAddressResult
import com.ezen.lolketing.util.repeatOnStarted
import com.ezen.lolketing.util.toast
import com.ezen.lolketing.view.dialog.adater.AddressAdapter
import com.ezen.lolketing.view.login.join.AddressViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class JoinBottomSheetDialog(
    val okClickListener : (String) -> Unit
) : BottomSheetDialogFragment() {

    val viewModel : AddressViewModel by viewModels()
    private val binding : DialogJoinBinding by lazy { DialogJoinBinding.inflate(layoutInflater) }
    private var isMoreData = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()

        binding.apply {
            lifecycleOwner = this@JoinBottomSheetDialog
            setVariable(BR._all, viewModel)
        }

        repeatOnStarted {
            viewModel.eventFlow.collect { event -> eventHandler(event) }
        }

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
        editAddress.setOnKeyListener { _, keyCode, _ ->
            if (keyCode == KeyEvent.KEYCODE_ENTER ) {
                editAddressDetail.isVisible = false
                viewModel.selectAddress(editAddress.text.toString(), true)
                return@setOnKeyListener false
            }
            false
        }

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (isMoreData && !recyclerView.canScrollVertically(1) ) {
                    viewModel.selectAddress("", false)
                }
            }
        })

        btnCancel.setOnClickListener { dismiss() }

        btnOk.setOnClickListener {
            if (editAddressDetail.isVisible.not()) {
                toast(getString(R.string.guide_input_address))
                return@setOnClickListener
            }

            val address =
                if (editAddressDetail.text.toString().isEmpty()) {
                    editAddress.text.toString()
                } else {
                    "${editAddress.text.toString()}, ${editAddressDetail.text.toString()}"
                }

            okClickListener(address)
            dismiss()
        }
    }

    private fun setRecyclerView(list: List<SearchAddressResult>) = with(binding) {
        txtGuide.isVisible = false

        val adapter = AddressAdapter{
            editAddress.setText(it)
            editAddressDetail.isVisible = true
            recyclerView.isVisible = false
            recyclerView.adapter = null
        }.apply {
            submitList(list)
        }
        recyclerView.adapter = adapter
        recyclerView.isVisible = true
    }

}