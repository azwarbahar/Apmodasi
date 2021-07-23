package com.skripsi.apmodasi.ui.activity.bunda;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.skripsi.apmodasi.R;
import com.skripsi.apmodasi.ui.activity.bunda.bacaan.Bacaan1Activity;
import com.skripsi.apmodasi.ui.activity.bunda.bacaan.Bacaan2Activity;
import com.skripsi.apmodasi.ui.activity.bunda.bacaan.Bacaan3Activity;
import com.skripsi.apmodasi.ui.activity.bunda.bacaan.Bacaan4Activity;
import com.skripsi.apmodasi.ui.activity.bunda.bacaan.Bacaan5Activity;
import com.skripsi.apmodasi.ui.activity.bunda.bacaan.Bacaan6Activity;

public class BacaanActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bacaan);

        TextView tv1 = findViewById(R.id.tv1);
        TextView tv2 = findViewById(R.id.tv2);
        TextView tv3 = findViewById(R.id.tv3);
        TextView tv4 = findViewById(R.id.tv4);
        TextView tv5 = findViewById(R.id.tv5);
        TextView tv6 = findViewById(R.id.tv6);

        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BacaanActivity.this, Bacaan1Activity.class));
            }
        });

        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BacaanActivity.this, Bacaan2Activity.class));
            }
        });

        tv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BacaanActivity.this, Bacaan3Activity.class));
            }
        });

        tv4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BacaanActivity.this, Bacaan4Activity.class));
            }
        });

        tv5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BacaanActivity.this, Bacaan5Activity.class));
            }
        });

        tv6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BacaanActivity.this, Bacaan6Activity.class));
            }
        });


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}