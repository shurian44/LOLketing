package com.ezen.lolketing.view.main.board

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import com.ezen.lolketing.BaseViewModelActivity
import com.ezen.lolketing.R
import com.ezen.lolketing.databinding.ActivityBoardListBinding
import com.ezen.lolketing.model.Board
import com.ezen.lolketing.util.Constants
import com.ezen.lolketing.util.repeatOnStarted
import com.ezen.lolketing.util.toast
import com.ezen.lolketing.view.dialog.BoardPopup
import com.ezen.lolketing.view.main.board.adapter.BoardListAdapter
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import java.io.Serializable
import javax.inject.Inject

@AndroidEntryPoint
class BoardListActivity : BaseViewModelActivity<ActivityBoardListBinding, BoardListViewModel>(R.layout.activity_board_list) {

    override val viewModel : BoardListViewModel by viewModels()
    private lateinit var adapter : BoardListAdapter
    @Inject lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        repeatOnStarted {
            viewModel.eventFlow.collect { event -> eventHandler(event) }
        }

        initViews()

    }

    private fun initViews() = with(binding) {
        boardRecyclerView.adapter = setAdapter()
        txtListBy.setOnClickListener {
            //                    query = firestore.collection("Board").whereEqualTo("email", auth.getCurrentUser().getEmail()).whereEqualTo("team", team);
            viewModel.getBoardList(listOf(Pair("email", auth.currentUser?.email ?: ""), Pair("team", intent?.getStringExtra(Constants.TEAM)?: "T1")))
        }
    }

    private fun setAdapter() : BoardListAdapter {
        adapter = BoardListAdapter(
            onclickListener = { board ->
               // todo anko 제거 후 수정예정
                val intent = Intent(this, BoardDetailActivity::class.java).apply {
                    putExtra("subject", board.subject)
                    putExtra("title", board.title)
                    putExtra("userId", board.userId)
                    putExtra("timestamp", board.timestamp)
                    putExtra("views", board.views)
                    putExtra("image", board.image)
                    putExtra("content", board.content)
                    putExtra("like", board.like as Serializable)
                    putExtra("team", board.team)
                    putExtra("likeCounts", board.likeCounts)
                    putExtra("documentID", "board")
                }
                startActivity(intent)
            },
            onLongClickListener = { board, view ->
                if (auth.currentUser?.email != board.email){
                    toast("수정 권한이 없습니다.")
                    return@BoardListAdapter
                }

                // 팝업메뉴 생성
                BoardPopup.createPopup(
                    view = view,
                    modifyClickListener = {
                        modifyBoard(board)
                    },
                    deleteClickListener = {
                        deleteBoard()
                    }
                )
            }
        )
        return adapter
    }

    private fun modifyBoard(board: Board) {
        toast("글 수정")
        val intent = Intent(this, BoardWriteActivity::class.java).apply {
            putExtra("title", board.title)
            putExtra("image", board.image)
            putExtra("content", board.content)
            putExtra("team", board.team)
            putExtra("timestamp", board.timestamp)
//            putExtra("documentId", board.title)
            putExtra("statement", "modify")
        }
        startActivity(intent)
    }

    private fun deleteBoard() {
        toast("글 삭제")
    }

    override fun onResume() {
        super.onResume()

        intent?.getStringExtra(Constants.TEAM)?.let {
            viewModel.getBoardList(it)
        }

    }

    private fun eventHandler(event : BoardListViewModel.Event) {
        when(event) {
            is BoardListViewModel.Event.BoardList -> {
                setRecyclerView(event.list)
            }
            is BoardListViewModel.Event.Error -> {
                toast(event.msg)
            }
        }
    }

    private fun setRecyclerView(list: List<Board>) {
        list.forEach {
            Log.e("++++++", "${it.title} / ${it.userId}")
        }
        adapter.submitList(list)
    }



    override fun logout(view: View) {
        TODO("Not yet implemented")
    }


//    ImageView main_logo, board_image, btn_write;
//    TextView btn_logout, board_title, board_searchBy, txt_listBy, txt_noResult;
//    Button btn_teamInfo, board_searchButton;
//    EditText board_searchBar;
//    RecyclerView board_recyclerView;
//
//    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
//    private FirebaseAuth auth = FirebaseAuth.getInstance();
//    String team;
//    String search;
//
//    Query query;
//    BoardAdapter adapter;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_board_list);
//
//        main_logo = findViewById(R.id.main_logo);
//        board_image = findViewById(R.id.board_image);
//        btn_logout = findViewById(R.id.btn_logout);
//        btn_teamInfo = findViewById(R.id.btn_teamInfo);
//        btn_write = findViewById(R.id.btn_write);
//        board_title = findViewById(R.id.board_title);
//        board_searchButton = findViewById(R.id.board_searchButton);
//        board_searchBar = findViewById(R.id.board_searchBar);
//        board_recyclerView = findViewById(R.id.board_recyclerView);
//        board_searchBy = findViewById(R.id.board_searchBy);
//        txt_listBy = findViewById(R.id.txt_listBy);
//        txt_noResult = findViewById(R.id.txt_noResult);
//
//        team = getIntent().getStringExtra("team");
//        search = board_searchBar.getText().toString();
//
//        // 게시글 목록 소환
//        query = firestore.collection("Board").whereEqualTo("team", team).orderBy("timestamp", Query.Direction.DESCENDING);
//
//        setRecycler(query);
//
//        board_title.setText("팀 게시판 - " + team);
//
//        // 구단 정보 버튼 클릭 이벤트
//        btn_teamInfo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(), TeamActivity.class);
//                intent.putExtra("team", team);
//                startActivity(intent);
//            }
//        });
//
//        // 내가 쓴 글 검색 및 모든 글 보기
//        txt_listBy.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
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
//        });
//
//        // 글쓰기 버튼 클릭
//        btn_write.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(), BoardWriteActivity.class);
//                intent.putExtra("team", team);
//                intent.setFlags(intent.FLAG_ACTIVITY_SINGLE_TOP |
//                                intent.FLAG_ACTIVITY_CLEAR_TOP |
//                                intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
//                startActivity(intent);
//            }
//        });
//
//        // 검색 조건 클릭
//        board_searchBy.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                searchBy();
//            }
//        });
//
//        // 검색 버튼 클릭
//        board_searchButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String search = board_searchBar.getText().toString();
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
//            }
//        });
//    } // onCreate
//
//    // 리사이클러뷰 세팅
//    private void setRecycler(Query query) {
//        FirestoreRecyclerOptions<BoardDTO> options = new FirestoreRecyclerOptions.Builder<BoardDTO>()
//                .setQuery(query, BoardDTO.class)
//                .build();
//        adapter = new BoardAdapter(options, this);
//        board_recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
//        board_recyclerView.setAdapter(adapter);
//    } // setRecycler
//
//    // 검색조건 드랍다운 메뉴
//    public void searchBy() {
//        //Creating the instance of PopupMenu
//        PopupMenu popup = new PopupMenu(getApplicationContext(), board_searchBy);
//        //Inflating the Popup using xml file
//        popup.getMenuInflater().inflate(R.menu.menu_search, popup.getMenu());
//        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//            public boolean onMenuItemClick(MenuItem item) {
//                switch (item.getItemId()) {
//                    case R.id.searchByTitle:
//                        Toast.makeText(getApplicationContext(), "제목으로 검색", Toast.LENGTH_SHORT).show();
//                        board_searchBy.setText("제목");
//                        return true;
//                    case R.id.searchByWriter:
//                        Toast.makeText(getApplicationContext(), "작성자로 검색", Toast.LENGTH_SHORT).show();
//                        board_searchBy.setText("작성자");
//                        return true;
//                    case R.id.searchByContent:
//                        Toast.makeText(getApplicationContext(), "내용으로 검색", Toast.LENGTH_SHORT).show();
//                        board_searchBy.setText("내용");
//                        return true;
//                }
//                return false;
//            }
//        });
//        popup.show();
//    } // searchBy
//
//    @Override
//    public void activityMove(Intent intent) {
//        startActivity(intent);
//    }
//
//    // 게시글 없을 시 이벤트
//    @Override
//    public void returnItemSize(int size) {
//        if(size < 1){
//            txt_noResult.setVisibility(View.VISIBLE);
//        }else{
//            txt_noResult.setVisibility(View.GONE);
//        }
//    } // returnItemSize
//
//    // 로그아웃
//    public void logout(View view) {
//        auth.signOut();
//        Intent intent = new Intent(this, LoginActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(intent);
//    }
//
//    // 로고(홈)
//    public void moveHome(View view) {
//        Intent intent = new Intent(this, MainActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(intent);
//    }
//
//    // 리사이클러뷰 어댑터 실행 및 종료
//    @Override
//    protected void onStart() {
//        super.onStart();
//        adapter.startListening();
//    }
//    @Override
//    protected void onStop(){
//        super.onStop();
//        adapter.stopListening();
//    }
//
}