package com.ezen.lolketing.view.main.board.search

import android.app.Activity
import android.os.Bundle
import android.view.KeyEvent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.view.isVisible
import com.ezen.lolketing.BaseViewModelActivity
import com.ezen.lolketing.R
import com.ezen.lolketing.databinding.ActivitySearchBinding
import com.ezen.lolketing.model.BoardItem
import com.ezen.lolketing.util.*
import com.ezen.lolketing.view.custom.CustomEditTextView
import com.ezen.lolketing.view.dialog.SearchMenuPopup
import com.ezen.lolketing.view.main.board.adapter.BoardListAdapter
import com.ezen.lolketing.view.main.board.detail.BoardDetailActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchActivity : BaseViewModelActivity<ActivitySearchBinding, SearchViewModel>(R.layout.activity_search) {

    override val viewModel: SearchViewModel by viewModels()
    private lateinit var team : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        repeatOnStarted {
            viewModel.eventFlow.collect{ event -> eventHandler(event) }
        }
        initViews()

    }

    private fun eventHandler(event: SearchViewModel.Event) {
        when(event) {
            is SearchViewModel.Event.Loading -> {
                showDialog()
            }
            is SearchViewModel.Event.Failure -> {
                dismissDialog()
                toast(R.string.error_unexpected)
            }
            is SearchViewModel.Event.Success -> {
                dismissDialog()
                setAdapter(event.list)
            }
        }
    }

    private fun initViews() = with(binding) {
        team = intent?.getStringExtra(Constants.TEAM) ?: ""
        title = team

        layoutTop.btnBack.setOnClickListener { onBackClick(it) }

        editSearch.setOnKeyListener { _, keyCode, keyEvent ->
            // 엔터 키를 이용한 검색
            if (keyEvent.action == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_ENTER) {
                searchBoard()
                return@setOnKeyListener false
            }
            false
        }

//        editSearch.setDrawableClickListener(CustomEditTextView.DRAWABLE_END) {
//            searchBoard()
//        }

        editSearch.setDrawableClickListener(CustomEditTextView.DRAWABLE_START) {
//            editSearch.hideKeyboard()
            SearchMenuPopup(layoutInflater).also {
                it.createPopup()
                it.setListener(
                    writerClickListener = {
                        viewModel.setChangeField(SearchViewModel.WRITER)
                    },
                    boardTitleClickListener = {
                        viewModel.setChangeField(SearchViewModel.BOARD_TITLE)
                    }
                )
                it.showPopup(editSearch)
            }
        }
    }

    private fun searchBoard() {
        viewModel.getSearchBoardList(binding.editSearch.text.toString())
    }

    private fun setAdapter(list: List<BoardItem.BoardListItem>) = with(binding) {
        val adapter = BoardListAdapter(
            onclickListener = { documentId ->
                launcher.launch(
                    createIntent(BoardDetailActivity::class.java).also {
                        it.putExtra(Constants.TEAM, team)
                        it.putExtra(Constants.DOCUMENT_ID, documentId)
                    }
                )
            }
        )

        adapter.addItemList(list)

        recyclerView.adapter = adapter
        txtEmpty.text = getString(R.string.guide_search_empty)
        txtEmpty.isVisible = list.isEmpty()
    }

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        setResult(Activity.RESULT_OK)
        searchBoard()
    }

}