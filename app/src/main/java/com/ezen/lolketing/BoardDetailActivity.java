package com.ezen.lolketing;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ezen.lolketing.adapter.CommentAdapter;
import com.ezen.lolketing.model.BoardDTO;
import com.ezen.lolketing.model.Users;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class BoardDetailActivity extends AppCompatActivity {

    ImageView main_logo, img_rank, board_img, img_like, img_comment, img_report, btn_more;
    TextView btn_logout, board_title, content_title, userId, views, timestamp, board_content, txt_likeCount, txt_commentCount;
    RecyclerView recyclerView_comment;
    EditText input_comment;
    Button btn_submit;
    View view1, view2, view3;
    private String documentID;

    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    Query query;
    CommentAdapter adapter;

    String get_image;

    private BoardDTO.commentDTO commentDTO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_detail);

        setViews();

        recyclerView_comment = findViewById(R.id.recyclerView_comment);

        query = firestore.collection("Board").document(documentID).collection("Comment").orderBy("timestamp", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<BoardDTO.commentDTO> options = new FirestoreRecyclerOptions.Builder<BoardDTO.commentDTO>()
                .setQuery(query, BoardDTO.commentDTO.class)
                .build();

        adapter = new CommentAdapter(options, documentID);
        recyclerView_comment.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView_comment.setAdapter(adapter);

        // 조회수 증가
        firestore.collection("Board").document(getIntent().getStringExtra("documentID")).update("views", FieldValue.increment(1));

        // 라디오버튼 목록
        final String[] title = {"부적절한 홍보게시물", "음란성 또는 청소년에게 부적합한 내용", "명예훼손/사생활 침해 및 저작권침해 등"};

        // 신고하기 버튼
        img_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder pc = new AlertDialog.Builder(BoardDetailActivity.this);

                pc.setTitle("신고하기");
//                pc.setIcon(R.drawable.policeman);
                pc.setSingleChoiceItems(title, 3, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                pc.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                pc.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                });
                pc.show();

            }
        });

        // 좋아요 버튼
        img_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        // 댓글 등록 버튼
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (input_comment.length() < 1 || input_comment == null) {
                    Toast.makeText(BoardDetailActivity.this, "댓글 내용이 없습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    setFirestore();
                    // 댓글 남길 때 댓글 수 증가
                    firestore.collection("Board").document(getIntent().getStringExtra("documentID")).update("commentCounts", FieldValue.increment(1));
                }
            }
        });

        btn_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu(getApplicationContext(), btn_more);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.menu_board, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.modify:
                                Toast.makeText(getApplicationContext(),"글 수정", Toast.LENGTH_SHORT).show();
                                modifyContent();
                                return true;
                            case R.id.delete:
                                Toast.makeText(getApplicationContext(),"글 삭제", Toast.LENGTH_SHORT).show();
                                return true;
                        }
                        return false;
                    }
                });
                popup.show();
            }
        });
    }

    private void setFirestore() {
        firestore.collection("Users").document(auth.getCurrentUser().getEmail()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Users user = documentSnapshot.toObject(Users.class);
                commentDTO = new BoardDTO.commentDTO();
                commentDTO.setUserId(user.getNickname());
                commentDTO.setTimestamp(System.currentTimeMillis());
                commentDTO.setComment(input_comment.getText().toString());
                commentDTO.setEmail(auth.getCurrentUser().getEmail());

                firestore.collection("Board").document(documentID).collection("Comment").document().set(commentDTO).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isComplete()) {
                            Toast.makeText(getApplicationContext(), "댓글이 작성 되었습니다.", Toast.LENGTH_SHORT).show();
                            input_comment.setText(null);
                        }
                    }
                });
            }
        });
    }

    public void setViews() {
        main_logo = findViewById(R.id.main_logo);
        img_rank = findViewById(R.id.img_rank);
        board_img = findViewById(R.id.board_img);
        img_like = findViewById(R.id.img_like);
        img_report = findViewById(R.id.img_report);
        btn_logout = findViewById(R.id.btn_logout);
        board_title = findViewById(R.id.board_title);
        content_title = findViewById(R.id.content_title);
        userId = findViewById(R.id.userId);
        btn_more = findViewById(R.id.btn_more);
        views = findViewById(R.id.views);
        timestamp = findViewById(R.id.timestamp);
        board_content = findViewById(R.id.board_content);
        txt_likeCount = findViewById(R.id.txt_likeCount);
        recyclerView_comment = findViewById(R.id.recyclerView_comment);
        input_comment = findViewById(R.id.input_comment);
        btn_submit = findViewById(R.id.btn_submit);
        img_comment = findViewById(R.id.img_comment);
        txt_commentCount = findViewById(R.id.txt_commentCount);
        view1 = findViewById(R.id.view1);
        view2 = findViewById(R.id.view2);
        view3 = findViewById(R.id.view3);

        Intent intent = getIntent();
        String get_subject = intent.getStringExtra("subject");
        String get_content_title = intent.getStringExtra("title");
        String get_userId = intent.getStringExtra("userId");
        get_image = intent.getStringExtra("image");
        String get_content = intent.getStringExtra("content");
        documentID = intent.getStringExtra("documentID");
        Long get_timestamp = intent.getLongExtra("timestamp", 0);
        int get_views = intent.getIntExtra("views", 0);
        int get_likeCounts = intent.getIntExtra("likeCounts", 0);
        int get_commentCounts = intent.getIntExtra("commentCounts", 0);
        Map<String, Boolean> like = (HashMap<String, Boolean>) intent.getSerializableExtra("like");
        Log.e("test", "나옴? " + like);

        content_title.setText(get_content_title);
        userId.setText(get_userId);
        board_content.setText(get_content);

        // Long타입 timestamp String타입으로 변경하기
        Date date = new Date(get_timestamp);
        SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd HH:mm");
        String timestampToString = format.format(date);
        timestamp.setText(timestampToString);
        views.setText("조회수 " + get_views);
        txt_likeCount.setText(get_likeCounts + "");
        txt_commentCount.setText(get_commentCounts + "");

        if (get_image == null || get_image.length() < 1) {
            board_img.setVisibility(View.GONE);
            view2.setVisibility(View.GONE);
        } else {
            Glide.with(this).load(get_image).into(board_img);
        }
    }

    public void modifyContent() {
        Intent intent = new Intent(getApplicationContext(), BoardWriteActivity.class);
        intent.putExtra("title", content_title.getText().toString());
        intent.putExtra("image", get_image);
        intent.putExtra("content", board_content.getText().toString());
        intent.putExtra("documentId", documentID);
        intent.putExtra("statement", "modify");
        startActivity(intent);
    }

    public void logout(View view) {
        auth.signOut();
    }

    public void moveHome(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
}

