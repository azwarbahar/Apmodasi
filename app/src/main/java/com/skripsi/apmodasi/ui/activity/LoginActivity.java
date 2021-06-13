package com.skripsi.apmodasi.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.skripsi.apmodasi.R;

public class LoginActivity extends AppCompatActivity {

    private RelativeLayout rl_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        rl_login = findViewById(R.id.rl_login);
        rl_login.setOnClickListener(this::clickLogin);
    }

    private void clickLogin(View view) {
        startActivity(new Intent(LoginActivity.this, MenuActivity.class));
    }
}