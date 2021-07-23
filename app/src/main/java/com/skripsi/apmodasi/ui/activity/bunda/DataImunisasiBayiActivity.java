package com.skripsi.apmodasi.ui.activity.bunda;

import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.skripsi.apmodasi.R;
import com.skripsi.apmodasi.app.network.ApiClient;
import com.skripsi.apmodasi.app.network.ApiInterface;
import com.skripsi.apmodasi.app.response.ResponseImunisasi;
import com.skripsi.apmodasi.data.model.Imunisasi;
import com.skripsi.apmodasi.ui.adapter.DataImunisasiBayiAdapter;
import com.skripsi.apmodasi.ui.adapter.RiwayatBayiAdapter;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DataImunisasiBayiActivity extends AppCompatActivity {

    private String id_bayi_intent;
    private RecyclerView rl_data_imunisasi_bayi;
    private ArrayList<Imunisasi> imunisasis;
    private DataImunisasiBayiAdapter dataImunisasiBayiAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_imunisasi_bayi);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        id_bayi_intent = getIntent().getStringExtra("id_bayi");

        rl_data_imunisasi_bayi = findViewById(R.id.rl_data_imunisasi_bayi);

        loadImunisasiBayi(id_bayi_intent);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void loadImunisasiBayi(String bayi_id) {

        SweetAlertDialog pDialog = new SweetAlertDialog(DataImunisasiBayiActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseImunisasi> responseImunisasiCall = apiInterface.getImunisasiBayi(bayi_id);
        responseImunisasiCall.enqueue(new Callback<ResponseImunisasi>() {
            @Override
            public void onResponse(Call<ResponseImunisasi> call, Response<ResponseImunisasi> response) {
                pDialog.dismiss();
                if (response.isSuccessful()) {
                    String kode = response.body().getKode();
                    if (kode.equals("1")) {
                        imunisasis = (ArrayList<Imunisasi>) response.body().getImunisasi_bayi();
                        dataImunisasiBayiAdapter = new DataImunisasiBayiAdapter(DataImunisasiBayiActivity.this, imunisasis);
                        rl_data_imunisasi_bayi.setLayoutManager(new LinearLayoutManager(DataImunisasiBayiActivity.this));
                        rl_data_imunisasi_bayi.setAdapter(dataImunisasiBayiAdapter);
                    }
                }

            }

            @Override
            public void onFailure(Call<ResponseImunisasi> call, Throwable t) {
                pDialog.dismiss();

            }
        });

    }
}