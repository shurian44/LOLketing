package com.ezen.lolketing.view.main.shop;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ezen.lolketing.R;
import com.ezen.lolketing.model.ShopDTO;
import com.ezen.lolketing.model.ShopEventDTO;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ShopEventActivity extends AppCompatActivity {

    private String TAG = "ShopEventActivity";
    private EditText editTextName;
    private ImageView imageView, imageViewBanner;

    private ProgressDialog progressDialog;
    private Uri filePath = null;

    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private FirebaseStorage storage = FirebaseStorage.getInstance();

    private ShopDTO shopDTO = new ShopDTO();
    private ShopEventDTO shopEventDTO = new ShopEventDTO();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_event);

        editTextName = findViewById(R.id.editTextName);
        imageView = findViewById(R.id.imageView);
        imageViewBanner = findViewById(R.id.imageViewBanner);

        // 검색 버튼 클릭 이벤트
        Button button_search = findViewById(R.id.button_search);
        button_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // DB Shop 컬렉션에서 데이터를 가져올 때
                String search = "[리그오브레전드] " + editTextName.getText().toString();
                firestore.collection("Shop").orderBy("name")
                        .startAt(search).endAt(search + "\uf8ff")
                        .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(queryDocumentSnapshots.size() > 0) { // 0으로 해야 검색이 됨.
                            for(DocumentSnapshot snapshot : queryDocumentSnapshots) {
                                shopDTO = snapshot.toObject(ShopDTO.class);
                                final String name = shopDTO.getName();
                                final String image = shopDTO.getImages().get(0);
                                Log.e(TAG, "검색한 상품명:" + name);
                                Log.e(TAG, "검색한 이미지:" + image);
                                // 검색해서 가져온 상품의 첫번째 이미지를 이미지뷰에 넣는다.
                                Glide.with(getApplicationContext()).load(image).into(imageView);
                                Log.e(TAG, "상품을 찾았다.");
                            }
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "상품을 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
                            Log.e(TAG, "상품을 못찾음");
                        }
                    }
                });
            }
        });

        // firebasestorage -> image upload
        imageViewBanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 이미지뷰_배너 클릭시 이벤트 처리 -> 업로드할 이미지 선택
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(
                        Intent.createChooser(intent, "이미지를 선택하세요."), 0);
                Log.e(TAG, "이미지 선택하기");
            }
        });

        // 등록 버튼 클릭시 이벤트
        Button button_add = findViewById(R.id.button_add);
        button_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (filePath != null) {
//                    shopEventDTO.setShopDTO(shopDTO);
//                    firestore.collection("ShopEvent").document().set(shopEventDTO);
                    uploadFile();
                } else {
                    setFirestore(null);
                }
            }
        });
    } // onCreate()

    // 이미지 업로드 결과 처리
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // request 코드가 0이고 ok를 선택했으며 data에 파일이 들어있다면
        if (requestCode == 0 && resultCode == RESULT_OK) {
            filePath = data.getData();
            try {
                // Uri 파일을 Bitmap으로 만들어서 ImageView에 집어넣는다.
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageViewBanner.setImageBitmap(bitmap);
                Log.e(TAG, "선택된 이미지:" + bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    } // onActivityResult()

    // 파일 업로드 처리
    private void uploadFile() {
        // 업로드 진행 Dialog 보이기
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("upload...");
        progressDialog.show();

        // create file name
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd_HHmmss");
        Date now = new Date();
        String filename = formatter.format(now) + ".png";
        // storage 주소와 폴더 파일명을 지정
        final StorageReference storageRef = storage.getReference().child("shop/" + filename);
        storageRef.putFile(filePath)
                // 진행중
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                        // dialog에 진행률을 퍼센트로 출력
                        progressDialog.setMessage("Uploaded" + ((int) progress) + "% ...");
                    }
                })
                // 성공시
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        progressDialog.dismiss(); // 업로드 진행 dialog 상자 닫기
                        Toast.makeText(getApplicationContext(), "업로드 완료", Toast.LENGTH_SHORT).show();
                        storageRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                setFirestore(task.getResult().toString());
                            }
                        });
                    }
                })
                // 실패시
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "업로드 실패", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "이미지 등록 실패함");
                    }
                });
    } // uploadFile

    // 파이어스토어 스토리지에 이미지를 등록함
    private void setFirestore(String downloadUrl) {
        shopEventDTO.setImage(downloadUrl);
        shopEventDTO.setShopDTO(shopDTO);

        firestore.collection("ShopEvent").document().set(shopEventDTO).addOnCompleteListener(
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isComplete()) {
                            Toast.makeText(getApplicationContext(),
                                    "배너 이미지가 등록되었습니다.", Toast.LENGTH_SHORT).show();
                            Log.e(TAG, "배너이미지 등록됨");
                            finish();
                        }
                    }
                });
    } // setFirestore()

} // end class
