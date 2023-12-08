package com.ezen.lolketing.view.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ezen.lolketing.databinding.DialogSearchBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SearchBottomSheetDialog(
    query: String,
    isTitleSearch: Boolean,
    private val listener : (String, Boolean) -> Unit
) : BottomSheetDialogFragment()  {

    private val binding : DialogSearchBinding by lazy { DialogSearchBinding.inflate(layoutInflater) }

    val search = MutableStateFlow(query)

    private val _isTitle = MutableStateFlow(isTitleSearch)
    val isTitle: StateFlow<Boolean> = _isTitle

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.dialog = this
    }

    /** 검색 **/
    fun onSearch() {
        listener(search.value, binding.radioTitle.isChecked)
        dismiss()
    }

}