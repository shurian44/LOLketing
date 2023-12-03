package com.ezen.lolketing.view.main.board.my_board

import android.os.Bundle
import androidx.activity.viewModels
import com.ezen.lolketing.R
import com.ezen.lolketing.StatusViewModelActivity
import com.ezen.lolketing.databinding.ActivityMyBoardBinding
import com.ezen.lolketing.util.Constants
import com.ezen.lolketing.util.createIntent
import com.ezen.lolketing.view.main.board.adapter.BoardListAdapter
import com.ezen.lolketing.view.main.board.detail.BoardDetailActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyBoardActivity : StatusViewModelActivity<ActivityMyBoardBinding, MyBoardViewModel>(R.layout.activity_my_board) {

    override val viewModel: MyBoardViewModel by viewModels()
    private var team : String = ""
    private lateinit var adapter : BoardListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initViews()

    }

    /** 각종 뷰들 초기화 **/
    private fun initViews() = with(binding) {
        team = intent?.getStringExtra(Constants.TEAM) ?: ""
        title = getString(R.string.my_board)
        layoutTop.btnBack.setOnClickListener { finish() }

        setAdapter()
    }

    private fun setAdapter() {
        adapter = BoardListAdapter(
            onclickListener = { documentId ->
                startActivity(
                    createIntent(BoardDetailActivity::class.java).also {
                        it.putExtra(Constants.TEAM, team)
                        it.putExtra(Constants.DOCUMENT_ID, documentId)
                    }
                )
            }
        )
        viewModel.getBoardList()
    }

}