package com.ezen.lolketing;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ezen.lolketing.adapter.BoardAdapter;
import com.ezen.lolketing.model.BoardDTO;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import org.angmarch.views.NiceSpinner;

public class BoardListActivity extends AppCompatActivity implements BoardAdapter.setActivityMove{

    ImageView main_logo, board_image;
    TextView btn_logout, board_title;
    Button btn_write, board_searchButton;
    NiceSpinner board_spinner;
    EditText board_searchBar;
    RecyclerView board_recyclerView;
    TabLayout main_tab;

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
        board_title = findViewById(R.id.board_title);
        btn_write = findViewById(R.id.btn_write);
        board_searchButton = findViewById(R.id.board_searchButton);
        board_searchBar = findViewById(R.id.board_searchBar);
        board_recyclerView = findViewById(R.id.board_recyclerView);
        main_tab = findViewById(R.id.main_tab);

        query = firestore.collection("Board").orderBy("timestamp", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<BoardDTO> options = new FirestoreRecyclerOptions.Builder<BoardDTO>()
                .setQuery(query, BoardDTO.class)
                .build();

        adapter = new BoardAdapter(options, this);
        board_recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        board_recyclerView.setAdapter(adapter);



        main_logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), JsonActivity.class);
                intent.setFlags(intent.FLAG_ACTIVITY_SINGLE_TOP |
                                intent.FLAG_ACTIVITY_CLEAR_TOP |
                                intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
                intent.putExtra("team", "T1");
                startActivity(intent);
            }
        });

        board_title.setText("팀 게시판 - " + getIntent().getStringExtra("team"));

//        String[] search_conditions = getResources().getStringArray(R.array.search_conditions);
//
        board_spinner = findViewById(R.id.board_spinner);
//
//        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(
//                this,
//                R.layout.activity_board_list,
//                search_conditions);
//
//        board_spinner.setAdapter(spinnerAdapter);

        board_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch(position){
                    case 0:
                        Log.e("test", "0번이 선택 됨.");
                        break;
                    case 1:
                        Log.e("test", "1번이 선택 됨.");
                        break;
                    case 2:
                        Log.e("test", "2번이 선택 됨.");
                        break;
                    default:
                        Log.e("test", "잘못 누름.");
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

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
    }
    private void logout() {
        auth.signOut();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(intent.FLAG_ACTIVITY_SINGLE_TOP |
                        intent.FLAG_ACTIVITY_CLEAR_TOP |
                        intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
        startActivity(intent);
    } // logout

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
} // class
