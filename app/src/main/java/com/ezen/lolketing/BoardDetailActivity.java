package com.ezen.lolketing;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class BoardDetailActivity extends AppCompatActivity {

    ImageView main_logo, img_rank, board_img, img_like, img_report;
    TextView btn_logout, board_title, content_title, userId, views, timestamp, board_content, txt_likeCount;
    RecyclerView recyclerView_comment;
    EditText input_comment;
    Button btn_submit;

    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_detail);

        setViews();

        // 라디오버튼 목록
        final String[] title = {"부적절한 홍보게시물", "음란성 또는 청소년에게 부적합한 내용", "명예훼손/사생활 침해 및 저작권침해 등"};

        // 댓글달기 버튼
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(BoardDetailActivity.this, "댓글 등록 완료", Toast.LENGTH_SHORT).show();

            }
        });

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
    }

    public void setViews(){
        main_logo = findViewById(R.id.main_logo);
        img_rank = findViewById(R.id.img_rank);
        board_img = findViewById(R.id.board_img);
        img_like = findViewById(R.id.img_like);
        img_report = findViewById(R.id.img_report);
        btn_logout = findViewById(R.id.btn_logout);
        board_title = findViewById(R.id.board_title);
        content_title = findViewById(R.id.content_title);
        userId = findViewById(R.id.userId);
        views = findViewById(R.id.views);
        timestamp = findViewById(R.id.timestamp);
        board_content = findViewById(R.id.board_content);
        txt_likeCount = findViewById(R.id.txt_likeCount);
        recyclerView_comment = findViewById(R.id.recyclerView_comment);
        input_comment = findViewById(R.id.input_comment);
        btn_submit = findViewById(R.id.btn_submit);

        Intent intent = getIntent();
        String get_subject = intent.getStringExtra("subject");
        String get_content_title = intent.getStringExtra("title");
        String get_userId = intent.getStringExtra("userId");
//        String get_timestamp = intent.getStringExtra("timestamp");
        int get_views = intent.getIntExtra("views", 0);
        String get_image = intent.getStringExtra("image");
        String get_content = intent.getStringExtra("content");
        String get_commentCounts = intent.getStringExtra("commentCounts");

        content_title.setText(get_content_title);
        userId.setText(get_userId);
        board_content.setText(get_content);
//        timestamp.setText(get_timestamp);
//        views.setText(get_views);
        Log.e("test", "get_views : " + get_views);

        if(get_image == null || get_image.length() < 1){
            board_img.setVisibility(View.GONE);
        }else{
            Glide.with(this).load(get_image).into(board_img);
        }
    }

    public void logout(View view) {

    }
}