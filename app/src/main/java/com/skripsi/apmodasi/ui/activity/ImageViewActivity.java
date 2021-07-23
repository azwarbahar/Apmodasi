package com.skripsi.apmodasi.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.skripsi.apmodasi.R;
import com.skripsi.apmodasi.app.util.Constanta;

public class ImageViewActivity extends AppCompatActivity {

    private ImageView img_close;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);

        String nama_foto = getIntent().getStringExtra("nama_foto");
        String role_foto = getIntent().getStringExtra("role_foto");
        PhotoView img_zoom = findViewById(R.id.img_zoom);

        if (role_foto.equals("Bayi")){
            Glide.with(this)
                    .load(Constanta.URL_IMG_BAYI + nama_foto)
                    .into(img_zoom);
        } else if (role_foto.equals("Bunda")) {
            Glide.with(this)
                    .load(Constanta.URL_IMG_BUNDA + nama_foto)
                    .into(img_zoom);
        } else {
            Glide.with(this)
                    .load(Constanta.URL_IMG_KADER + nama_foto)
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