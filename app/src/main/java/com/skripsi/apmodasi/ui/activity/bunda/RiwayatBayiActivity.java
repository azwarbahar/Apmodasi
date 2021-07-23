package com.skripsi.apmodasi.ui.activity.bunda;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.skripsi.apmodasi.R;
import com.skripsi.apmodasi.app.network.ApiClient;
import com.skripsi.apmodasi.app.network.ApiInterface;
import com.skripsi.apmodasi.app.response.ResponseImunisasi;
import com.skripsi.apmodasi.app.response.ResponseKader;
import com.skripsi.apmodasi.data.model.Bayi;
import com.skripsi.apmodasi.data.model.Imunisasi;
import com.skripsi.apmodasi.data.model.RiwayatKader;
import com.skripsi.apmodasi.ui.activity.kader.DataBayiActivity;
import com.skripsi.apmodasi.ui.adapter.RiwayatBayiAdapter;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RiwayatBayiActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private TextView tv_kosong;
    private RecyclerView rv_riwayat_bayi;
    private ArrayList<RiwayatKader> riwayatKaders;
    private RiwayatBayiAdapter riwayatBayiAdapter;

    private String id_bayi_intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riwayat_bayi);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        id_bayi_intent = getIntent().getStringExtra("id_bayi");

        tv_kosong = findViewById(R.id.tv_kosong);
        rv_riwayat_bayi = findViewById(R.id.rv_riwayat_bayi);

        mSwipeRefreshLayout = findViewById(R.id.swipe_continer);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.purple_500,
                android.R.color.holo_blue_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_green_dark);
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                loadRiwayatBayi(id_bayi_intent);
            }
        });
    }

    private void loadRiwayatBayi(String id_bayi) {

        mSwipeRefreshLayout.setRefreshing(false);
        SweetAlertDialog pDialog = new SweetAlertDialog(RiwayatBayiActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseKader> responseImunisasiCall = apiInterface.getRiwayatBayi(id_bayi);
        responseImunisasiCall.enqueue(new Callback<ResponseKader>() {
            @Override
            public void onResponse(Call<ResponseKader> call, Response<ResponseKader> response) {

                pDialog.dismiss();
                if (response.isSuccessful()) {
                    String kode = response.body().getKode();
                    if (kode.equals("1")) {
                        riwayatKaders = (ArrayList<RiwayatKader>) response.body().getRiwayat_bayi();
                        if (riwayatKaders.size() < 1) {
                            rv_riwayat_bayi.setVisibility(View.GONE);
                            tv_kosong.setVisibility(View.VISIBLE);
                        } else {
                            rv_riwayat_bayi.setVisibility(View.VISIBLE);
                            tv_kosong.setVisibility(View.GONE);
                            riwayatBayiAdapter = new RiwayatBayiAdapter(RiwayatBayiActivity.this, riwayatKaders);
                            rv_riwayat_bayi.setLayoutManager(new LinearLayoutManager(RiwayatBayiActivity.this));
                            rv_riwayat_bayi.setAdapter(riwayatBayiAdapter);
                        }

                    } else {
                        rv_riwayat_bayi.setVisibility(View.GONE);
                        tv_kosong.setVisibility(View.VISIBLE);
                    }
                } else {
                    rv_riwayat_bayi.setVisibility(View.GONE);
                    tv_kosong.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<ResponseKader> call, Throwable t) {
                pDialog.dismiss();
                rv_riwayat_bayi.setVisibility(View.GONE);
                tv_kosong.setVisibility(View.VISIBLE);
            }
        });


    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onRefresh() {
        loadRiwayatBayi(id_bayi_intent);
    }
}