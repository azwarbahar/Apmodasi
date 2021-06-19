package com.skripsi.apmodasi.ui.activity.intro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.skripsi.apmodasi.R;
import com.skripsi.apmodasi.ui.activity.bunda.MenuActivity;
import com.skripsi.apmodasi.ui.activity.kader.MenuKaderActivity;

public class LoginActivity extends AppCompatActivity {

    private RelativeLayout rl_login;
    private RelativeLayout rl_login_kader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        rl_login_kader = findViewById(R.id.rl_login_kader);
        rl_login = findViewById(R.id.rl_login);
        rl_login_kader.setOnClickListener(this::clickKader);
        rl_login.setOnClickListener(this::clickLogin);
    }

    private void clickKader(View view) {
        startActivity(new Intent(LoginActivity.this, MenuKaderActivity.class));
    }

    private void clickLogin(View view) {
        startActivity(new Intent(LoginActivity.this, MenuActivity.class));
    }
}