package com.skripsi.apmodasi.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.skripsi.apmodasi.R;

public class ImageViewActivity extends AppCompatActivity {

    private ImageView img_close;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);

        String data = getIntent().getStringExtra("data_image");
        PhotoView img_zoom = findViewById(R.id.img_zoom);

        if (data.equals("Bayi")){
            Glide.with(this)
                    .load(R.drawable.img_baby)
                    .into(img_zoom);
        } else {
            Glide.with(this)
                    .load(R.drawable.adinda)
                    .into(img_zoom);
        }

        img_close = findViewById(R.id.img_close);
        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}