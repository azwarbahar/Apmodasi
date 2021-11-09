package com.skripsi.apmodasi.ui.activity.intro;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.skripsi.apmodasi.R;
import com.skripsi.apmodasi.app.network.ApiClient;
import com.skripsi.apmodasi.app.network.ApiInterface;
import com.skripsi.apmodasi.app.response.ResponseAuth;
import com.skripsi.apmodasi.ui.activity.bunda.AkunActivity;
import com.skripsi.apmodasi.ui.activity.bunda.EditProfileActivity;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrasiActivity extends AppCompatActivity {

    private EditText et_nama;
    private EditText et_telpon;
    private EditText et_alamat;
    private EditText et_nik;
    private EditText et_password;
    private TextView et_kelurahan;

    private RelativeLayout rl_regist;

    private TextView tv_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrasi);

        et_nama = findViewById(R.id.et_nama);
        et_telpon = findViewById(R.id.et_telpon);
        et_alamat = findViewById(R.id.et_alamat);
        et_nik = findViewById(R.id.et_nik);
        et_password = findViewById(R.id.et_password);
        et_kelurahan = findViewById(R.id.et_kelurahan);

        rl_regist = findViewById(R.id.rl_regist);

        tv_login = findViewById(R.id.tv_login);
        tv_login.setOnClickListener(this::clickLogin);

        rl_regist.setOnClickListener(this::clickRegist);

        et_kelurahan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder b = new AlertDialog.Builder(RegistrasiActivity.this);
                b.setTitle("Kelurahan");
                String[] types = {"Tetebatu", "Parangbanoa", "Pangkabinanga", "Jenetallasa", "Bontoala",
                        "Panakukang", "Taeng", "Mangalli"};
                b.setItems(types, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        et_kelurahan.setText(types[which]);
                    }

                });

                b.show();
            }
        });
    }

    private void clickRegist(View view) {

        et_nama.setError(null);
        et_telpon.setError(null);
        et_alamat.setError(null);
        et_nik.setError(null);
        et_password.setError(null);

        String nama = et_nama.getText().toString();
        String telpon = et_telpon.getText().toString();
        String alamat = et_alamat.getText().toString();
        String kelurahan = et_kelurahan.getText().toString();
        String nik = et_nik.getText().toString();
        String password = et_password.getText().toString();

        if (nama.equals("") || nama.isEmpty()) {
            et_nama.setError("Lengkapi");
        } else if (telpon.equals("") || telpon.isEmpty()) {
            et_telpon.setError("Lengkapi");
        } else if (alamat.equals("") || alamat.isEmpty()) {
            et_alamat.setError("Lengkapi");
        } else if (nik.equals("") || nik.isEmpty()) {
            et_nik.setError("Lengkapi");
        } else if (password.equals("") || password.isEmpty()) {
            et_password.setError("Lengkapi");
        } else {
            sendData(nama, telpon, alamat, kelurahan, nik, password);
        }

    }

    private void sendData(String nama, String telpon, String alamat, String kelurahan, String nik, String password) {

        SweetAlertDialog pDialog = new SweetAlertDialog(RegistrasiActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseAuth> responseAuthCall = apiInterface.registrasi_akun(nik, nama, telpon, alamat, kelurahan, password);
        responseAuthCall.enqueue(new Callback<ResponseAuth>() {
            @Override
            public void onResponse(Call<ResponseAuth> call, Response<ResponseAuth> response) {
                pDialog.dismiss();
                if (response.isSuccessful()) {
                    String kode = response.body().getKode();
                    String pesan = response.body().getKode();
                    if (kode.equals("1")) {

                        initSuccess();
                    } else {
                        new SweetAlertDialog(RegistrasiActivity.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Opss..")
                                .setContentText(pesan)
                                .show();
                    }

                } else {
                    new SweetAlertDialog(RegistrasiActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Opss..")
                            .setContentText("Permintaan gagal.")
                            .show();
                }
            }

            @Override
            public void onFailure(Call<ResponseAuth> call, Throwable t) {
                pDialog.dismiss();
                new SweetAlertDialog(RegistrasiActivity.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Opss..")
                        .setContentText("Permintaan gagal.")
                        .show();
            }
        });

    }

    private void initSuccess() {

        SweetAlertDialog sweetAlertDialogError = new SweetAlertDialog(RegistrasiActivity.this,
                SweetAlertDialog.SUCCESS_TYPE);
        sweetAlertDialogError.setTitleText("Registrasi Berhasil");
        sweetAlertDialogError.setCancelable(false);
        sweetAlertDialogError.setContentText("Silahkan login menggunakan akun yang telah didaftarkan!");
        sweetAlertDialogError.setConfirmButton("OK", new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                sweetAlertDialog.dismiss();
                finish();
            }
        });
        sweetAlertDialogError.show();

    }

    private void clickLogin(View view) {
        finish();
    }
}