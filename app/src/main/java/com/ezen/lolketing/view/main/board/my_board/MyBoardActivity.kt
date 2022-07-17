package com.ezen.lolketing.view.main.board.my_board

import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.view.isVisible
import com.ezen.lolketing.BaseViewModelActivity
import com.ezen.lolketing.R
import com.ezen.lolketing.databinding.ActivityMyBoardBinding
import com.ezen.lolketing.model.BoardItem
import com.ezen.lolketing.util.Constants
import com.ezen.lolketing.util.createIntent
import com.ezen.lolketing.util.repeatOnStarted
import com.ezen.lolketing.util.toast
import com.ezen.lolketing.view.main.board.adapter.BoardListAdapter
import com.ezen.lolketing.view.main.board.detail.BoardDetailActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyBoardActivity : BaseViewModelActivity<ActivityMyBoardBinding, MyBoardViewModel>(R.layout.activity_my_board) {

    override val viewModel: MyBoardViewModel by viewModels()
    private var team : String = ""
    private lateinit var adapter : BoardListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        repeatOnStarted {
            viewModel.eventFlow.collect{ event -> eventHandler(event) }
        }

        initViews()

    }

    /** 각종 뷰들 초기화 **/
    private fun initViews() = with(binding) {
        team = intent?.getStringExtra(Constants.TEAM) ?: ""
        title = getString(R.string.my_board)
        layoutTop.btnBack.setOnClickListener { onBackClick(it) }

        setAdapter()
    }

    private fun eventHandler(event: MyBoardViewModel.Event) {
        when(event) {
            is MyBoardViewModel.Event.Error -> {
                toast(R.string.error_unexpected)
                finish()
            }
            is MyBoardViewModel.Event.Success -> {
                if (event.list.isEmpty()) {
                    binding.txtEmpty.isVisible = true
                    return
                }
                setRecyclerView(event.list)
            }
        }
    }

    private fun setAdapter() {
        adapter = BoardListAdapter(
            onclickListener = { documentId ->
                launcher.launch(
                    createIntent(BoardDetailActivity::class.java).also {
                        it.putExtra(Constants.TEAM, team)
                        it.putExtra(Constants.DOCUMENT_ID, documentId)
                    }
                )
            }
        )
        viewModel.getBoardList()
    }

    private fun setRecyclerView(list: List<BoardItem.BoardListItem>) = with(binding) {
        adapter.addItemList(list)
        recyclerView.adapter = adapter
    }

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        setAdapter()
    }

}