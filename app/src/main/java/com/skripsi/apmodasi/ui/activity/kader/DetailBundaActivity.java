package com.skripsi.apmodasi.ui.activity.kader;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.skripsi.apmodasi.R;
import com.skripsi.apmodasi.app.network.ApiClient;
import com.skripsi.apmodasi.app.network.ApiInterface;
import com.skripsi.apmodasi.app.response.ResponseBayi;
import com.skripsi.apmodasi.app.response.ResponseBunda;
import com.skripsi.apmodasi.app.util.Constanta;
import com.skripsi.apmodasi.data.model.Bayi;
import com.skripsi.apmodasi.data.model.Bunda;
import com.skripsi.apmodasi.ui.activity.bunda.MenuActivity;
import com.skripsi.apmodasi.ui.adapter.BayiAdapter;
import com.skripsi.apmodasi.ui.adapter.BayiKaderAdapter;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailBundaActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private String id_bunda_intent;
    private RecyclerView rv_bayi;
    private BayiKaderAdapter bayiKaderAdapter;

    private ImageView foto_profil;
    private TextView tv_nama;
    private TextView tv_telpon;
    private TextView tv_alamat;
    private TextView tv_kosong;
    private ArrayList<Bayi> bayiArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_bunda);

        id_bunda_intent = getIntent().getStringExtra("ID_BUNDA");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        tv_kosong = findViewById(R.id.tv_kosong);
        rv_bayi = findViewById(R.id.rv_bayi);
        foto_profil = findViewById(R.id.foto_profil);
        tv_nama = findViewById(R.id.tv_nama);
        tv_telpon = findViewById(R.id.tv_telpon);
        tv_alamat = findViewById(R.id.tv_alamat);

        mSwipeRefreshLayout = findViewById(R.id.swipe_continer);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.purple_500,
                android.R.color.holo_blue_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_green_dark);
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                loadDataBunda(id_bunda_intent);
                loadDataBayi(id_bunda_intent);
            }
        });

    }

    private void loadDataBunda(String id_bunda_intent) {

        SweetAlertDialog pDialog = new SweetAlertDialog(DetailBundaActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBunda> responseBundaCall = apiInterface.getBundaId(id_bunda_intent);
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
                .into(foto_profil);
    }

    private void loadDataBayi(String id_bunda_intent) {

        mSwipeRefreshLayout.setRefreshing(false);
        SweetAlertDialog pDialog = new SweetAlertDialog(DetailBundaActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBayi> responseBayiCall = apiInterface.getBayiBunda(id_bunda_intent, "Active");
        responseBayiCall.enqueue(new Callback<ResponseBayi>() {
            @Override
            public void onResponse(Call<ResponseBayi> call, Response<ResponseBayi> response) {
                pDialog.dismiss();
                if (response.isSuccessful()) {
                    String kode = response.body().getKode();
                    if (kode.equals("1")) {
                        bayiArrayList = (ArrayList<Bayi>) response.body().getBayiData();
                        if (bayiArrayList == null || bayiArrayList.size() <= 0 || bayiArrayList.isEmpty()) {
                            tv_kosong.setVisibility(View.VISIBLE);
                            rv_bayi.setVisibility(View.GONE);
                        } else {
                            tv_kosong.setVisibility(View.GONE);
                            rv_bayi.setVisibility(View.VISIBLE);
                            rv_bayi.setLayoutManager(new LinearLayoutManager(DetailBundaActivity.this));
                            bayiKaderAdapter = new BayiKaderAdapter(DetailBundaActivity.this, bayiArrayList);
                            rv_bayi.setAdapter(bayiKaderAdapter);
                        }
                    } else {
                        tv_kosong.setVisibility(View.VISIBLE);
                        rv_bayi.setVisibility(View.GONE);
                    }
                } else {
                    tv_kosong.setVisibility(View.VISIBLE);
                    rv_bayi.setVisibility(View.GONE);
                }

            }

            @Override
            public void onFailure(Call<ResponseBayi> call, Throwable t) {
                pDialog.dismiss();
                tv_kosong.setVisibility(View.VISIBLE);
                rv_bayi.setVisibility(View.GONE);

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
        loadDataBunda(id_bunda_intent);
        loadDataBayi(id_bunda_intent);
    }
}