package com.skripsi.apmodasi.ui.activity.bunda;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.skripsi.apmodasi.R;
import com.skripsi.apmodasi.app.network.ApiClient;
import com.skripsi.apmodasi.app.network.ApiInterface;
import com.skripsi.apmodasi.app.response.ResponseBunda;
import com.skripsi.apmodasi.app.util.Constanta;
import com.skripsi.apmodasi.data.model.Bunda;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileActivity extends AppCompatActivity {

    private SharedPreferences mPreferences;
    private SharedPreferences.Editor editor;
    private String user_id;

    private EditText et_nama_lengkap;
    private EditText et_kontak;
    private EditText et_alamat;
    private RelativeLayout rl_batal;
    private RelativeLayout rl_simpan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        mPreferences = getSharedPreferences(Constanta.MY_SHARED_PREFERENCES, MODE_PRIVATE);
        user_id = mPreferences.getString(Constanta.SESSION_ID_USER, "");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        et_nama_lengkap = findViewById(R.id.et_nama_lengkap);
        et_kontak = findViewById(R.id.et_kontak);
        et_alamat = findViewById(R.id.et_alamat);
        rl_batal = findViewById(R.id.rl_batal);
        rl_simpan = findViewById(R.id.rl_simpan);

        loadDataAkun(user_id);

        rl_batal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        rl_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nama_lengkap = et_nama_lengkap.getText().toString();
                String kontak = et_kontak.getText().toString();
                String alamat = et_alamat.getText().toString();

                if (nama_lengkap.equals("")) {
                    new SweetAlertDialog(EditProfileActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Opss..")
                            .setContentText("Nama lengkap tidak boleh kosong")
                            .show();
                } else if (kontak.equals("")) {
                    new SweetAlertDialog(EditProfileActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Opss..")
                            .setContentText("Kontak tidak boleh kosong")
                            .show();
                } else if (alamat.equals("")) {
                    new SweetAlertDialog(EditProfileActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Opss..")
                            .setContentText("Alamat tidak boleh kosong")
                            .show();
                } else {
                    sendDataEdit(user_id, nama_lengkap, kontak, alamat);
                }

            }
        });
    }

    private void sendDataEdit(String user_id, String nama_lengkap, String kontak, String alamat) {

        SweetAlertDialog pDialog = new SweetAlertDialog(EditProfileActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBunda> responseBundaCall = apiInterface.editBunda(user_id, nama_lengkap, kontak, alamat);
        responseBundaCall.enqueue(new Callback<ResponseBunda>() {
            @Override
            public void onResponse(Call<ResponseBunda> call, Response<ResponseBunda> response) {
                pDialog.dismiss();
                if (response.isSuccessful()) {
                    String kode = response.body().getKode();
                    if (kode.equals("1")) {
                        new SweetAlertDialog(EditProfileActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("Success..")
                                .setContentText(response.body().getPesan())
                                .setConfirmButton("Ok", new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        sweetAlertDialog.dismiss();
                                        finish();

                                    }
                                })
                                .show();
                    } else {
                        new SweetAlertDialog(EditProfileActivity.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Mohon Maaf...")
                                .setContentText(response.body().getPesan())
                                .show();
                    }
                } else {
                    new SweetAlertDialog(EditProfileActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Mohon Maaf...")
                            .setContentText("Terjadi Kesalahan!")
                            .show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBunda> call, Throwable t) {
                pDialog.dismiss();
                new SweetAlertDialog(EditProfileActivity.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Mohon Maaf...")
                        .setContentText("Terjadi Kesalahan System!")
                        .show();
            }
        });

    }

    private void loadDataAkun(String user_id) {
        SweetAlertDialog pDialog = new SweetAlertDialog(EditProfileActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBunda> responseBundaCall = apiInterface.getBundaId(user_id);
        responseBundaCall.enqueue(new Callback<ResponseBunda>() {
            @Override
            public void onResponse(Call<ResponseBunda> call, Response<ResponseBunda> response) {
                pDialog.dismiss();
                if (response.isSuccessful()) {
                    String kode = response.body().getKode();
                    if (kode.equals("1")) {
                        initDataBunda(response.body().getBunda());
                    }
                }

            }

            @Override
            public void onFailure(Call<ResponseBunda> call, Throwable t) {
                pDialog.dismiss();

            }
        });


    }

    private void initDataBunda(Bunda bunda) {
        et_nama_lengkap.setText(bunda.getNamaBunda());
        et_kontak.setText(bunda.getKontakBunda());
        et_alamat.setText(bunda.getAlamatBunda());
    }
}