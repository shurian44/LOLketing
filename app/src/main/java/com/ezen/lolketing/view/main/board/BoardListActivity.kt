package com.ezen.lolketing.view.main.board

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.view.isVisible
import com.ezen.lolketing.BaseViewModelActivity
import com.ezen.lolketing.R
import com.ezen.lolketing.databinding.ActivityBoardListBinding
import com.ezen.lolketing.model.Board
import com.ezen.lolketing.util.*
import com.ezen.lolketing.view.dialog.BoardPopup
import com.ezen.lolketing.view.login.LoginActivity
import com.ezen.lolketing.view.main.board.adapter.BoardListAdapter
import com.ezen.lolketing.view.main.board.detail.BoardDetailActivity
import com.ezen.lolketing.view.main.board.write.BoardWriteActivity
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@AndroidEntryPoint
class BoardListActivity : BaseViewModelActivity<ActivityBoardListBinding, BoardListViewModel>(R.layout.activity_board_list) {

    override val viewModel : BoardListViewModel by viewModels()
    private lateinit var team : String
    private lateinit var adapter : BoardListAdapter
    @Inject lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        repeatOnStarted {
            viewModel.eventFlow.collect { event -> eventHandler(event) }
        }

        initViews()
        getBoardList()

    }

    private fun eventHandler(event : BoardListViewModel.Event) {
        when(event) {
            is BoardListViewModel.Event.BoardList -> {
                setRecyclerView(event.list)
            }
            is BoardListViewModel.Event.Error -> {
                toast(event.msg)
            }
            is BoardListViewModel.Event.DeleteSuccess -> {
                getBoardList()
            }
        }
    }

    private fun initViews() = with(binding) {
        team = intent?.getStringExtra(Constants.TEAM) ?: Team.T1.name
        boardTitle.text = getString(R.string.board_title, team)
        boardRecyclerView.adapter = setAdapter()

//        // 구단 정보 버튼 클릭 이벤트
        btnTeamInfo.setOnClickListener {
            getResult.launch(
                createIntent(TeamActivity::class.java).also {
                    it.putExtra(Constants.TEAM, team)
                }
            )
        }

        // 내가 쓴 글 검색 및 모든 글 보기
        txtListBy.setOnClickListener {
            // todo 수정 필요
//                if(txt_listBy.getText().toString().equals("내가 쓴 글")) {
//                    txt_listBy.setText("<  전체 글");
//                    query = firestore.collection("Board").whereEqualTo("email", auth.getCurrentUser().getEmail()).whereEqualTo("team", team);
//
//                }else if(txt_listBy.getText().toString().equals("<  전체 글")) {
//                    txt_listBy.setText("내가 쓴 글");
//                    query = firestore.collection("Board").whereEqualTo("team", team).orderBy("timestamp", Query.Direction.DESCENDING);
//                }
//                adapter.stopListening();
//                setRecycler(query);
//                adapter.startListening();
//            }
            // query = firestore.collection("Board").whereEqualTo("email", auth.getCurrentUser().getEmail()).whereEqualTo("team", team);
            viewModel.getBoardList(listOf(Pair("email", auth.currentUser?.email ?: ""), Pair(Constants.TEAM, team)))
        }

        // 글쓰기 버튼 클릭
        btnWrite.setOnClickListener {
            getResult.launch(
                createIntent(
                    BoardWriteActivity::class.java,
                    Intent.FLAG_ACTIVITY_SINGLE_TOP,
                    Intent.FLAG_ACTIVITY_CLEAR_TOP,
                    Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP
                ).also {
                    it.putExtra(Constants.TEAM, team)
                }
            )
        }
        // 검색 조건 클릭
        boardSearchBy.setOnClickListener {
             searchBy()
        }
        // 검색 버튼 클릭
        boardSearchButton.setOnClickListener {
            val search = boardSearchBar.text.toString()
            toast("준비중입니다.")
//                if(board_searchBy.getText().toString().equals("검색조건")){
//                    Toast.makeText(BoardListActivity.this, "검색조건을 먼저 설정하세요.", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                else if(board_searchBy.getText().toString().equals("제목")){
//                    query = firestore.collection("Board").whereEqualTo("team", team).orderBy("title").startAt(search).endAt(search + "\uf8ff");
//                }
//                else if(board_searchBy.getText().toString().equals("작성자")){
//                    query = firestore.collection("Board").whereEqualTo("team", team).orderBy("userId").startAt(search).endAt(search + "\uf8ff");
//                }
//                else if(board_searchBy.getText().toString().equals("내용")){
//                    query = firestore.collection("Board").whereEqualTo("team", team).orderBy("content").startAt(search).endAt(search + "\uf8ff");
//                }
//
//                adapter.stopListening();
//                setRecycler(query);
//                adapter.startListening();
        }
    }

    private fun getBoardList() {
        intent?.getStringExtra(Constants.TEAM)?.let {
            viewModel.getBoardList(it)
        }
    }

    private fun setAdapter() : BoardListAdapter {
        adapter = BoardListAdapter(
            onclickListener = { board ->
                val intent = Intent(this, BoardDetailActivity::class.java).apply {
                    putExtra(Constants.BOARD, board)
                }
                getResult.launch(intent)
            },
            onLongClickListener = { board, view ->
                if (auth.currentUser?.email != board.email){
                    toast("수정 권한이 없습니다.")
                    return@BoardListAdapter
                }

                // 팝업메뉴 생성
                BoardPopup.createPopup(
                    view = view,
                    menuRes = R.menu.menu_board,
                ) { menuId ->
                    when(menuId) {
                        R.id.modify -> {
                            modifyBoard(board)
                        }
                        R.id.delete -> {
                            deleteBoard(board)
                        }
                    }
                }
            }
        )
        return adapter
    }

    private fun setRecyclerView(list: List<Board>) {
        if (list.isEmpty()) {
            binding.txtNoResult.isVisible = true
            return
        } else {
            binding.txtNoResult.isVisible = false
        }

        list.forEach {
            Log.e("++++++", "${it.title} / ${it.userId}")
        }

        adapter.submitList(list)
    }

    private fun modifyBoard(board: Board) {
        getResult.launch(
            createIntent(BoardWriteActivity::class.java).also{
                it.putExtra(Constants.TEAM, team)
                it.putExtra(Constants.BOARD, board)
            }
        )
    }

    private fun deleteBoard(board: Board) {
        board.documentId?.let {
            viewModel.deleteBoard(it)
        } ?: kotlin.run {
            toast("글 삭제를 실패하였습니다.")
        }
    }

    //    // 검색조건 드랍다운 메뉴
    private fun searchBy() {
        // 팝업메뉴 생성
        BoardPopup.createPopup(
            view = binding.boardSearchBy,
            menuRes = R.menu.menu_search
        ) { menuId ->
            binding.boardSearchBy.text = when(menuId) {
                R.id.searchByTitle -> {
                    "제목"
                }
                R.id.searchByWriter -> {
                    "작성자"
                }
                R.id.searchByContent -> {
                    "내용"
                }
                else -> {
                    "검색조건"
                }
            }
        }
    }

    override fun logout(view: View) {
        auth.signOut()
        startActivity(LoginActivity::class.java, Intent.FLAG_ACTIVITY_CLEAR_TOP)
        finish()
    }

    private val getResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        getBoardList()
    }

}