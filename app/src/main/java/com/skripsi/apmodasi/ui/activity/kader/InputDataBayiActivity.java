package com.skripsi.apmodasi.ui.activity.kader;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.skripsi.apmodasi.R;
import com.skripsi.apmodasi.ui.activity.ImageViewActivity;
import com.skripsi.apmodasi.ui.activity.bunda.MenuActivity;

public class InputDataBayiActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_data_bayi);

        RelativeLayout rl_batal = findViewById(R.id.rl_batal);
        CardView cv_foto_bayi = findViewById(R.id.cv_foto_bayi);
        TextView tv_nama_bayi = findViewById(R.id.tv_nama_bayi);
        tv_nama_bayi.setText(getIntent().getStringExtra("DATA"));

//        Toast.makeText(this, getIntent().getStringExtra("DATA"), Toast.LENGTH_SHORT).show();

        rl_batal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        cv_foto_bayi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InputDataBayiActivity.this, ImageViewActivity.class);
                intent.putExtra("data_image", "Bayi");
                startActivity(intent);
            }
        });
    }
}