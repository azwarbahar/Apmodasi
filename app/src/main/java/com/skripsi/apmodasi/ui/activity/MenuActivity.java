package com.skripsi.apmodasi.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.skripsi.apmodasi.R;

public class MenuActivity extends AppCompatActivity {

    private CardView cv_foto;
    private CardView cv_profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        cv_profile = findViewById(R.id.cv_profile);
        cv_profile.setOnClickListener(this::clickProfile);
        cv_foto = findViewById(R.id.cv_foto);
        cv_foto.setOnClickListener(this::clickPhoto);
    }

    private void clickProfile(View view) {
        startActivity(new Intent(MenuActivity.this, AkunActivity.class));
    }

    private void clickPhoto(View view) {
        startActivity(new Intent(MenuActivity.this, ImageViewActivity.class));
    }
}