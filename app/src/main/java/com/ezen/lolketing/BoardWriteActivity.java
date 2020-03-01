package com.ezen.lolketing;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.ezen.lolketing.model.BoardDTO;
import com.ezen.lolketing.model.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class BoardWriteActivity extends AppCompatActivity {

    ImageView board_image, icon_photo;
    TextView board_title;
    EditText input_title, input_content;
    ScrollView scrollView;
    Button btn_cancel, btn_submit;

    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    ProgressDialog progressDialog;

    private BoardDTO boardDTO;
    private String nickname;
    private String team;

    private Uri filePath = null;

    final static int PERMISSION_REQUEST_CODE = 1000;

    String statement = "";
    String documentId = "";

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_write);

        permissionCheck();
        setView();

        board_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(
                Intent.createChooser(intent, "이미지를 선택하세요."), 0);
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(filePath != null)
                    uploadFile();
                else{
                    setFirestore(null);
                }
            }
        });


    } // onCreate

    private void setView(){
        board_image = findViewById(R.id.board_image);
        icon_photo = findViewById(R.id.icon_photo);
        board_title = findViewById(R.id.board_title);
        input_title = findViewById(R.id.input_title);
        input_content = findViewById(R.id.input_content);
        scrollView = findViewById(R.id.scrollView);
        btn_cancel = findViewById(R.id.btn_cancel);
        btn_submit = findViewById(R.id.btn_submit);

        team = getIntent().getStringExtra("team");
        String title = getIntent().getStringExtra("title");
        String image = getIntent().getStringExtra("image");
        String content = getIntent().getStringExtra("content");
        documentId = getIntent().getStringExtra("documentId");
        statement = getIntent().getStringExtra("statement");

        if(statement != null && statement.equals("modify")){
            if (image != null && image.length() > 4){
                Glide.with(this).load(image).into(board_image);
                icon_photo.setVisibility(View.GONE);
            }
            input_title.setText(title);
            input_content.setText(content);
        }

        scrollView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input_content.setFocusable(true);
                input_content.requestFocus();
            }
        });

        firestore.collection("Users").document(auth.getCurrentUser().getEmail()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Users users = documentSnapshot.toObject(Users.class);
                nickname = users.getNickname();
            }
        });

    }

    private void permissionCheck() {
        if (android.os.Build.VERSION.SDK_INT >= 23) {
            int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
            ArrayList<String> arrayPermission = new ArrayList<String>();

            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                arrayPermission.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            }

            if (arrayPermission.size() > 0) {
                String strArray[] = new String[arrayPermission.size()];
                strArray = arrayPermission.toArray(strArray);
                ActivityCompat.requestPermissions(this, strArray, PERMISSION_REQUEST_CODE);
            } else {
                // Initialize 코드
            }
        }
    } // permissionCheck

    // 이미지 업로드 결과 처리
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // request코드가 0이고 OK를 선택했으며 data에 파일이 들어있다면.
        if(requestCode == 0 && resultCode == RESULT_OK){
            filePath = data.getData();
            try {
                //Uri 파일을 Bitmap으로 만들어서 ImageView에 집어 넣는다.
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);

                icon_photo.setVisibility(View.GONE);
                board_image.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    } // onActivityResult

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode) {
            case PERMISSION_REQUEST_CODE: {
                if (grantResults.length < 1) {
                    Toast.makeText(this, "Failed get permission", Toast.LENGTH_SHORT).show();
                    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                    finish();
                    return ;
                }
                for (int i=0; i<grantResults.length; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "Permission is denied : " + permissions[i], Toast.LENGTH_SHORT).show();
                        finish();
                        return ;
                    }
                }
                Toast.makeText(this, "Permission is granted", Toast.LENGTH_SHORT).show();
                // Initialize 코드
            }
            break;
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    // 파일 업로드
    private void uploadFile() {
        //업로드 진행 Dialog 보이기
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("업로드중...");
        progressDialog.show();

        //Unique한 파일명을 만들자.
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd_HHmmss");
        Date now = new Date();
        String filename = formatter.format(now) + ".png";
        //storage 주소와 폴더 파일명을 지정해 준다.
        final StorageReference storageRef = storage.getReference().child("board/" + filename);
//                    getReferenceFromUrl("gs://lolketing.appspot.com").child("board/" + filename);
        //올라가거라...
        storageRef.putFile(filePath)
                //진행중
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        @SuppressWarnings("VisibleForTests") //이걸 넣어 줘야 아랫줄에 에러가 사라진다. 넌 누구냐?
                                double progress = (100 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                        //dialog에 진행률을 퍼센트로 출력해 준다
                        progressDialog.setMessage("Uploaded " + ((int) progress) + "% ...");
                    }
                })
                //성공시
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        progressDialog.dismiss(); //업로드 진행 Dialog 상자 닫기
                        Toast.makeText(getApplicationContext(), "업로드 완료!", Toast.LENGTH_SHORT).show();
                        storageRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                setFirestore(task.getResult().toString());
                            }
                        });
                    }
                })
                //실패시
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "업로드 실패!", Toast.LENGTH_SHORT).show();
                    }
                });

    } // uploadFile

    private void setFirestore(String downloadUrl) {
        boardDTO = new BoardDTO();
        boardDTO.setEmail(auth.getCurrentUser().getEmail());
        boardDTO.setTitle(input_title.getText().toString());
        boardDTO.setContent(input_content.getText().toString());
        boardDTO.setUserId(nickname);

        boardDTO.setLike(new HashMap<String, Boolean>());
        boardDTO.setImage(downloadUrl);
        boardDTO.setSubject("[게시판]");
        boardDTO.setTeam(team);
        if(statement != null && statement.equals("modify"))
            boardDTO.setTimestamp(getIntent().getLongExtra("timestamp", 0));
        else
            boardDTO.setTimestamp(System.currentTimeMillis());

        if(statement != null && statement.equals("modify")) {
            firestore.collection("Board").document(documentId).update("content", input_content.getText().toString(), "title", input_title.getText().toString());
            if(downloadUrl != null){
                firestore.collection("Board").document(documentId).update("image", downloadUrl);
            }
            finish();
        }else{
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
    }

    public void logout(View view) {
        auth.signOut();
    }

    public void moveHome(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
} // class
