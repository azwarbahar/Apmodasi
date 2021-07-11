package com.skripsi.apmodasi.ui.activity.kader;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.skripsi.apmodasi.R;
import com.skripsi.apmodasi.app.util.Constanta;
import com.skripsi.apmodasi.ui.activity.bunda.AkunActivity;
import com.skripsi.apmodasi.ui.activity.intro.LoginActivity;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class AkunKaderActivity extends AppCompatActivity {

    private SharedPreferences mPreferences;
    private SharedPreferences.Editor editor;

    private RelativeLayout rl_logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_akun_kader);

        mPreferences = getSharedPreferences(Constanta.MY_SHARED_PREFERENCES, MODE_PRIVATE);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        rl_logout = findViewById(R.id.rl_logout);
        rl_logout.setOnClickListener(this::clickLogout);
    }

    private void clickLogout(View view) {

        new SweetAlertDialog(AkunKaderActivity.this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Logout")
                .setContentText("Ingin Keluar Dari Akun ?")
                .setCancelButton("Tidak", new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                    }
                })
                .setConfirmButton("Ok", new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                        startActivity(new Intent(AkunKaderActivity.this, LoginActivity.class));
                        SharedPreferences.Editor editor = mPreferences.edit();
                        editor.apply();
                        editor.clear();
                        editor.commit();
                        finish();
                    }
                })
                .show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}