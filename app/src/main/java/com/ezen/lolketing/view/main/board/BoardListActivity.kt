package com.ezen.lolketing.view.main.board

import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.ezen.lolketing.BaseViewModelActivity
import com.ezen.lolketing.R
import com.ezen.lolketing.databinding.ActivityBoardListBinding
import com.ezen.lolketing.model.BoardItem
import com.ezen.lolketing.util.*
import com.ezen.lolketing.view.main.board.adapter.BoardListAdapter
import com.ezen.lolketing.view.main.board.detail.BoardDetailActivity
import com.ezen.lolketing.view.main.board.write.BoardWriteActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BoardListActivity : BaseViewModelActivity<ActivityBoardListBinding, BoardListViewModel>(R.layout.activity_board_list) {

    override val viewModel : BoardListViewModel by viewModels()
    private lateinit var team : String
    private lateinit var adapter : BoardListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        repeatOnStarted {
            viewModel.eventFlow.collect { event -> eventHandler(event) }
        }

        initViews()
    }

    private fun eventHandler(event : BoardListViewModel.Event) {
        when(event) {
            is BoardListViewModel.Event.BoardList -> {
                setRecyclerView(event.list)
            }
            is BoardListViewModel.Event.Error -> {
                toast(getString(R.string.error_board_load))
            }
        }
    }

    /** 각종 뷰들 초기화 **/
    private fun initViews() = with(binding) {
        team = intent?.getStringExtra(Constants.TEAM) ?: Team.T1.name
        title = team
        activity = this@BoardListActivity

        layoutTop.layoutTop.setBackgroundResource(android.R.color.transparent)
        layoutTop.btnBack.setOnClickListener { onBackClick(it) }

        // 스크롤에따라 타이틀 영역 색상 변경
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val res = if (recyclerView.computeVerticalScrollOffset() > 200) {
                    R.color.black
                } else {
                    android.R.color.transparent
                }

                layoutTop.layoutTop.setBackgroundResource(res)
            }
        })

        setAdapter()
    }

    /** adapter 설정 **/
    private fun setAdapter(){
        adapter = BoardListAdapter(
            onclickListener = { documentId ->
                launcher.launch(
                    createIntent(BoardDetailActivity::class.java).also {
                        it.putExtra(Constants.TEAM, team)
                        it.putExtra(Constants.DOCUMENT_ID, documentId)
                    }
                )
            }
        ).also {
            // 최상단 이미지
            it.addItem(BoardItem.TeamImage(team = team))
        }

        viewModel.getBoardList(team)
    }

    /** Recyclerview 설정 **/
    private fun setRecyclerView(list: List<BoardItem.BoardListItem>) {
        binding.recyclerView.adapter = adapter
        if (list.isEmpty() && adapter.itemCount < 2) {
            binding.txtEmpty.isVisible = true
            return
        } else {
            binding.txtEmpty.isVisible = false
            adapter.addItemList(list)
        }
    }

    /** 글쓰기 버튼 클릭 **/
    fun writeBoard(view: View) {
        launcher.launch(
            createIntent(BoardWriteActivity::class.java).also{
                it.putExtra(Constants.TEAM, team)
            }
        )
    }

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        setAdapter()
    }

}