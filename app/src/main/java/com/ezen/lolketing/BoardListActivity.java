package com.ezen.lolketing;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ezen.lolketing.adapter.BoardAdapter;
import com.ezen.lolketing.model.BoardDTO;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class BoardListActivity extends AppCompatActivity implements BoardAdapter.setActivityMove{

    ImageView main_logo, board_image, btn_write;
    TextView btn_logout, board_title, board_searchBy;
    Button btn_teamInfo, board_searchButton;
    EditText board_searchBar;
    RecyclerView board_recyclerView;

    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    Query query;
    BoardAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_list);

        main_logo = findViewById(R.id.main_logo);
        board_image = findViewById(R.id.board_image);
        btn_logout = findViewById(R.id.btn_logout);
        btn_teamInfo = findViewById(R.id.btn_teamInfo);
        btn_write = findViewById(R.id.btn_write);
        board_title = findViewById(R.id.board_title);
        board_searchButton = findViewById(R.id.board_searchButton);
        board_searchBar = findViewById(R.id.board_searchBar);
        board_recyclerView = findViewById(R.id.board_recyclerView);
        board_searchBy = findViewById(R.id.board_searchBy);

        query = firestore.collection("Board").orderBy("timestamp", Query.Direction.DESCENDING);
        setRecycler(query);

        main_logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), JsonActivity.class);
                intent.setFlags(intent.FLAG_ACTIVITY_SINGLE_TOP |
                                intent.FLAG_ACTIVITY_CLEAR_TOP |
                                intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
                startActivity(intent);
            }
        });

        board_title.setText("팀 게시판 - " + getIntent().getStringExtra("team"));

//        String[] search_conditions = getResources().getStringArray(R.array.search_conditions);

        // 팀 정보 게시판 버튼 클릭
        btn_teamInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(BoardListActivity.this, "구단 정보로 이동", Toast.LENGTH_SHORT).show();
            }
        });

        // 글쓰기 버튼 클릭
        btn_write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), BoardWriteActivity.class);
                intent.putExtra("team", "T1");
                intent.setFlags(intent.FLAG_ACTIVITY_SINGLE_TOP |
                                intent.FLAG_ACTIVITY_CLEAR_TOP |
                                intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
                startActivity(intent);
            }
        });

        // 검색 조건 클릭
        board_searchBy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchBy();
            }
        });

        // 검색 버튼 클릭
        board_searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String search = board_searchBar.getText().toString();
                if(board_searchBy.getText().toString().equals("검색조건")){
                    Toast.makeText(BoardListActivity.this, "검색조건을 먼저 설정하세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(board_searchBy.getText().toString().equals("제목")){
                    Log.e("test", "제목");
                    query = firestore.collection("Board").orderBy("title").startAt(search).endAt(search + "\uf8ff");
                }
                else if(board_searchBy.getText().toString().equals("작성자")){
                    Log.e("test", "작성자");

                    query = firestore.collection("Board").orderBy("userId").startAt(search).endAt(search + "\uf8ff");
                }
                else if(board_searchBy.getText().toString().equals("내용")){
                    Log.e("test", "내용");
                    query = firestore.collection("Board").orderBy("content").startAt(search).endAt(search + "\uf8ff");
                }
                adapter.stopListening();
                setRecycler(query);
                adapter.startListening();
            }
        });
    }

    // 리사이클러뷰 세팅
    private void setRecycler(Query query) {
        FirestoreRecyclerOptions<BoardDTO> options = new FirestoreRecyclerOptions.Builder<BoardDTO>()
                .setQuery(query, BoardDTO.class)
                .build();

        adapter = new BoardAdapter(options, this);
        board_recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        board_recyclerView.setAdapter(adapter);
    }

    // 검색조건 드랍다운 메뉴
    public void searchBy() {
        //Creating the instance of PopupMenu
        PopupMenu popup = new PopupMenu(getApplicationContext(), board_searchBy);
        //Inflating the Popup using xml file
        popup.getMenuInflater().inflate(R.menu.menu_search, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.searchByTitle:
                        Toast.makeText(getApplicationContext(), "제목으로 검색", Toast.LENGTH_SHORT).show();
                        board_searchBy.setText("제목");
                        return true;
                    case R.id.searchByWriter:
                        Toast.makeText(getApplicationContext(), "작성자로 검색", Toast.LENGTH_SHORT).show();
                        board_searchBy.setText("작성자");
                        return true;
                    case R.id.searchByContent:
                        Toast.makeText(getApplicationContext(), "내용으로 검색", Toast.LENGTH_SHORT).show();
                        board_searchBy.setText("내용");
                        return true;
                }
                return false;
            }
        });
        popup.show();
    }

    @Override
    public void activityMove(Intent intent) {
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop(){
        super.onStop();
        adapter.stopListening();
    }

    public void logout(View view) {
        auth.signOut();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void moveHome(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
} // class
