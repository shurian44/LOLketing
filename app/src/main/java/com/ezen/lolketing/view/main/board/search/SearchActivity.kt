package com.ezen.lolketing.view.main.board.search

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.ezen.lolketing.R
import com.ezen.lolketing.StatusViewModelActivity
import com.ezen.lolketing.databinding.ActivitySearchBinding
import com.ezen.lolketing.util.Constants
import com.ezen.lolketing.util.createIntent
import com.ezen.lolketing.util.toast
import com.ezen.lolketing.view.dialog.SearchBottomSheetDialog
import com.ezen.lolketing.view.main.board.adapter.BoardListAdapter
import com.ezen.lolketing.view.main.board.detail.BoardDetailActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchActivity :
    StatusViewModelActivity<ActivitySearchBinding, SearchViewModel>(R.layout.activity_search) {

    override val viewModel: SearchViewModel by viewModels()
    val adapter = BoardListAdapter(
        onclickListener = { documentId ->
            launcher.launch(
                createIntent(BoardDetailActivity::class.java).also {
                    it.putExtra(Constants.TEAM, viewModel.team)
                    it.putExtra(Constants.DOCUMENT_ID, documentId)
                }
            )
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initViews()

    }

    private fun initViews() = with(binding) {
        activity = this@SearchActivity
        vm = viewModel

        intent
            ?.getStringExtra(Constants.TEAM)
            ?.let {
                viewModel.setTeam(it)
                title = it
            }
            ?: {
                toast("오류가 발생하였습니다")
                Handler(mainLooper).postDelayed({ finish() }, 1000)
            }

        layoutTop.btnBack.setOnClickListener { finish() }

        layoutBottom.setOnClickListener {
            SearchBottomSheetDialog(
                viewModel.query,
                viewModel.isTitleSearch
            ) { query, isTitleSearch ->
                viewModel.onSearch(isTitleSearch, query)
            }.show(supportFragmentManager, null)
        }
        layoutBottom.performClick()

    }

    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            setResult(Activity.RESULT_OK)
            viewModel.getSearchBoardList()
        }

}