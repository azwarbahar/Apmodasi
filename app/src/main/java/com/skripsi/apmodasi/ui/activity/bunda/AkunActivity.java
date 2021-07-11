package com.skripsi.apmodasi.ui.activity.bunda;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.skripsi.apmodasi.R;
import com.skripsi.apmodasi.app.network.ApiClient;
import com.skripsi.apmodasi.app.network.ApiInterface;
import com.skripsi.apmodasi.app.response.ResponseBunda;
import com.skripsi.apmodasi.app.util.Constanta;
import com.skripsi.apmodasi.data.model.Bunda;
import com.skripsi.apmodasi.ui.activity.intro.LoginActivity;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AkunActivity extends AppCompatActivity {

    private SharedPreferences mPreferences;
    private SharedPreferences.Editor editor;
    private String user_id;

    private Bunda bunda;

    private CircularImageView img_profile;
    private TextView tv_nama;
    private TextView tv_telpon;
    private TextView tv_alamat;

    private RelativeLayout rl_logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_akun);

        mPreferences = getSharedPreferences(Constanta.MY_SHARED_PREFERENCES, MODE_PRIVATE);
        user_id = mPreferences.getString(Constanta.SESSION_ID_USER, "");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        img_profile = findViewById(R.id.img_profile);
        tv_nama = findViewById(R.id.tv_nama);
        tv_telpon = findViewById(R.id.tv_telpon);
        tv_alamat = findViewById(R.id.tv_alamat);

        rl_logout = findViewById(R.id.rl_logout);
        rl_logout.setOnClickListener(this::clickLogout);

        loadDataAkun(user_id);

    }

    private void loadDataAkun(String user_id) {
        SweetAlertDialog pDialog = new SweetAlertDialog(AkunActivity.this, SweetAlertDialog.PROGRESS_TYPE);
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
        tv_nama.setText(bunda.getNamaBunda());
        tv_telpon.setText(bunda.getKontakBunda());
        tv_alamat.setText(bunda.getAlamatBunda());
        Glide.with(this)
                .load(Constanta.URL_IMG_BUNDA + bunda.getFotoBunda())
                .into(img_profile);
    }

    private void clickLogout(View view) {

        new SweetAlertDialog(AkunActivity.this, SweetAlertDialog.WARNING_TYPE)
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
                        startActivity(new Intent(AkunActivity.this, LoginActivity.class));
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