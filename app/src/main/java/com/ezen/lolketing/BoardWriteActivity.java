package com.ezen.lolketing;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.ezen.lolketing.model.BoardDTO;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class BoardWriteActivity extends AppCompatActivity {

    ImageView main_logo, board_image, icon_photo;
    TextView btn_logout, board_title;
    EditText input_title, input_content;
    ScrollView scrollView;
    View view1, view2;
    Button btn_cancel, btn_submit;

    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private BoardDTO boardDTO;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_write);

        main_logo = findViewById(R.id.main_logo);
        board_image = findViewById(R.id.board_image);
        icon_photo = findViewById(R.id.icon_photo);
        btn_logout = findViewById(R.id.btn_logout);
        board_title = findViewById(R.id.board_title);
        input_title = findViewById(R.id.input_title);
        input_content = findViewById(R.id.input_content);
        scrollView = findViewById(R.id.scrollView);
        view1 = findViewById(R.id.view1);
        view2 = findViewById(R.id.view2);
        btn_cancel = findViewById(R.id.btn_cancel);
        btn_submit = findViewById(R.id.btn_submit);

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boardDTO = new BoardDTO();
                boardDTO.setTitle(input_title.getText().toString());
                boardDTO.setContent(input_content.getText().toString());
                boardDTO.setUserId(auth.getCurrentUser().getEmail());

                firestore.collection("Board").document().set(boardDTO).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isComplete()){
                            Toast.makeText(getApplicationContext(), "글이 작성 되었습니다.", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                });

            }
        });
    }
}
