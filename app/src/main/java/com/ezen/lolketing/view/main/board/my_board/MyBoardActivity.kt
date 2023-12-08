package com.ezen.lolketing.view.main.board.my_board

import android.os.Bundle
import android.os.Handler
import androidx.activity.viewModels
import com.ezen.lolketing.R
import com.ezen.lolketing.StatusViewModelActivity
import com.ezen.lolketing.databinding.ActivityMyBoardBinding
import com.ezen.lolketing.util.Constants
import com.ezen.lolketing.util.createIntent
import com.ezen.lolketing.util.toast
import com.ezen.lolketing.view.main.board.adapter.BoardListAdapter
import com.ezen.lolketing.view.main.board.detail.BoardDetailActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyBoardActivity :
    StatusViewModelActivity<ActivityMyBoardBinding, MyBoardViewModel>(R.layout.activity_my_board) {

    override val viewModel: MyBoardViewModel by viewModels()
    val adapter = BoardListAdapter(
        onclickListener = { documentId ->
            startActivity(
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

    /** 각종 뷰들 초기화 **/
    private fun initViews() = with(binding) {
        adapter = this@MyBoardActivity.adapter
        vm = viewModel

        intent
            ?.getStringExtra(Constants.TEAM)
            ?.let { viewModel.setTeam(it) }
            ?: {
                toast("오류가 발생하였습니다")
                Handler(mainLooper).postDelayed({ finish() }, 1000)
            }
        layoutTop.btnBack.setOnClickListener { finish() }
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchListOfMyWritings()
    }

}