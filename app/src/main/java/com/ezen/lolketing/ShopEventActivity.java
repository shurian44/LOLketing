package com.ezen.lolketing;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;

public class ShopEventActivity extends AppCompatActivity {

    EditText editTextName;
    ImageView imageView, imageViewBanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_event);

        editTextName = findViewById(R.id.editTextName);
        imageView = findViewById(R.id.imageView);
        imageViewBanner = findViewById(R.id.imageViewBanner);
    }
}
