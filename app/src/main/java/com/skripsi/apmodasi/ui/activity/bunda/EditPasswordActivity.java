package com.skripsi.apmodasi.ui.activity.bunda;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.google.android.material.textfield.TextInputEditText;
import com.skripsi.apmodasi.R;
import com.skripsi.apmodasi.app.network.ApiClient;
import com.skripsi.apmodasi.app.network.ApiInterface;
import com.skripsi.apmodasi.app.response.ResponseBunda;
import com.skripsi.apmodasi.app.util.Constanta;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditPasswordActivity extends AppCompatActivity {

    private SharedPreferences mPreferences;
    private SharedPreferences.Editor editor;
    private String user_id;

    private TextInputEditText tie_password_lama;
    private TextInputEditText tie_password_baru;

    private RelativeLayout rl_batal;
    private RelativeLayout rl_simpan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_password);

        mPreferences = getSharedPreferences(Constanta.MY_SHARED_PREFERENCES, MODE_PRIVATE);
        user_id = mPreferences.getString(Constanta.SESSION_ID_USER, "");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        tie_password_lama = findViewById(R.id.tie_password_lama);
        tie_password_baru = findViewById(R.id.tie_password_baru);

        rl_simpan = findViewById(R.id.rl_simpan);
        rl_simpan.setOnClickListener(this::clickSimpan);
        rl_batal = findViewById(R.id.rl_batal);
        rl_batal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void clickSimpan(View view) {

        String old_password = tie_password_lama.getText().toString();
        String new_password = tie_password_baru.getText().toString();

        if (old_password.isEmpty() || old_password.equals("")) {
            new SweetAlertDialog(EditPasswordActivity.this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Gagal...")
                    .setContentText("Password lama tidak boleh kosong!")
                    .show();
        } else if (new_password.isEmpty() || new_password.equals("")) {
            new SweetAlertDialog(EditPasswordActivity.this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Gagal...")
                    .setContentText("Password baru tidak boleh kosong!")
                    .show();
        } else if (old_password.equals(new_password)) {
            new SweetAlertDialog(EditPasswordActivity.this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Gagal...")
                    .setContentText("Password baru dan lama tidak boleh sama!")
                    .show();
        } else {
            startUpdatePassword(old_password, new_password);
        }

    }

    private void startUpdatePassword(String old_password, String new_password) {

        SweetAlertDialog pDialog = new SweetAlertDialog(EditPasswordActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBunda> responseBundaCall = apiInterface.editPasswordBunda(user_id, old_password, new_password);
        responseBundaCall.enqueue(new Callback<ResponseBunda>() {
            @Override
            public void onResponse(Call<ResponseBunda> call, Response<ResponseBunda> response) {
                pDialog.dismiss();
                if (response.isSuccessful()) {
                    String kode = response.body().getKode();
                    if (kode.equals("0")) {
                        new SweetAlertDialog(EditPasswordActivity.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Gagal...")
                                .setContentText(response.body().getPesan())
                                .show();
                    } else if (kode.equals("1")) {
                        SweetAlertDialog success = new SweetAlertDialog(EditPasswordActivity.this, SweetAlertDialog.SUCCESS_TYPE);
                        success.setTitleText("Success..");
                        success.setCancelable(false);
                        success.setContentText("Edit Password Berhasil");
                        success.setConfirmButton("Ok", new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                                finish();
                            }
                        });
                        success.show();
                    } else if (kode.equals("2")) {
                        new SweetAlertDialog(EditPasswordActivity.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Gagal...")
                                .setContentText(response.body().getPesan())
                                .show();
                    }
                } else {
                    new SweetAlertDialog(EditPasswordActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Mohon Maaf...")
                            .setContentText("Terjadi Kesalahan!")
                            .show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBunda> call, Throwable t) {
                pDialog.dismiss();
                new SweetAlertDialog(EditPasswordActivity.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Oops...")
                        .setContentText("Permintaan Gagal, Terjadi Kesalahan Sistem!")
                        .show();
            }
        });

    }
}