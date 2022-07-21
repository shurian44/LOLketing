package com.ezen.lolketing.view.dialog

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ezen.lolketing.databinding.DialogSearchBinding
import com.ezen.lolketing.view.main.board.search.SearchActivity
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class SearchBottomSheetDialog(
    private val searchText: String,
    private val select: String,
    private val listener : (String, String) -> Unit
) : BottomSheetDialogFragment()  {

    private val binding : DialogSearchBinding by lazy { DialogSearchBinding.inflate(layoutInflater) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()

    }

    private fun initViews() = with(binding) {

        if (searchText.isNotEmpty()) {
            editSearch.setText(searchText)
        }

        when(select){
            SearchActivity.NICKNAME -> {
                radioNickname.isChecked = true
            }
            else -> {
                radioTitle.isChecked = true
            }
        }

        editSearch.setOnKeyListener { _, keyCode, keyEvent ->
            // 엔터 키를 이용한 검색
            if (keyEvent.action == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_ENTER) {
                onSearch()
                return@setOnKeyListener false
            }
            false
        }
    }

    /** 검색 **/
    private fun onSearch() = with(binding) {
        val query = when(radioGroup.checkedRadioButtonId) {
            radioNickname.id -> {
                SearchActivity.NICKNAME
            }
            else -> {
                SearchActivity.TITLE
            }
        }

        listener(query, editSearch.text.toString())
        dismiss()
    }

}