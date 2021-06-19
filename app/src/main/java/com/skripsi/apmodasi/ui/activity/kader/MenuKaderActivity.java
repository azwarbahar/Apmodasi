package com.skripsi.apmodasi.ui.activity.kader;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.skripsi.apmodasi.R;

public class MenuKaderActivity extends AppCompatActivity {

    private CardView cv_bayi;
    private CardView cv_ibu;
    private ImageView img_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_kader);

        img_user = findViewById(R.id.img_user);
        img_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuKaderActivity.this, AkunKaderActivity.class));
            }
        });

        cv_ibu = findViewById(R.id.cv_ibu);
        cv_bayi = findViewById(R.id.cv_bayi);
        cv_bayi.setOnClickListener(this::clickBayi);
        cv_ibu.setOnClickListener(this::clickIbu);

    }

    private void clickIbu(View view) {
        startActivity(new Intent(MenuKaderActivity.this, DataIbuActivity.class));
    }

    private void clickBayi(View view) {
        startActivity(new Intent(MenuKaderActivity.this, DataBayiActivity.class));
    }
}