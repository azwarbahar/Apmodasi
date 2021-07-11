package com.skripsi.apmodasi.ui.activity.kader;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.skripsi.apmodasi.R;
import com.skripsi.apmodasi.app.network.ApiClient;
import com.skripsi.apmodasi.app.network.ApiInterface;
import com.skripsi.apmodasi.app.response.ResponseBunda;
import com.skripsi.apmodasi.data.model.Bunda;
import com.skripsi.apmodasi.ui.adapter.IbuKaderAdapter;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DataIbuActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView rv_ibu;
    private IbuKaderAdapter ibuKaderAdapter;
    private ArrayList<Bunda> bundas;
    private TextView tv_kosong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_ibu);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        rv_ibu = findViewById(R.id.rv_ibu);
        tv_kosong = findViewById(R.id.tv_kosong);

        mSwipeRefreshLayout = findViewById(R.id.swipe_continer);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.purple_500,
                android.R.color.holo_blue_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_green_dark);
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                loadDataIbu();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void loadDataIbu() {

        mSwipeRefreshLayout.setRefreshing(false);
        SweetAlertDialog pDialog = new SweetAlertDialog(DataIbuActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBunda> responseBundaCall = apiInterface.getAllBunda();
        responseBundaCall.enqueue(new Callback<ResponseBunda>() {
            @Override
            public void onResponse(Call<ResponseBunda> call, Response<ResponseBunda> response) {
                pDialog.dismiss();
                if (response.isSuccessful()){
                    String kode = response.body().getKode();
                    if (kode.equals("1")){
                        bundas = (ArrayList<Bunda>) response.body().getBunda_data();
                        if (bundas.size() < 1){
                            rv_ibu.setVisibility(View.GONE);
                            tv_kosong.setVisibility(View.VISIBLE);
                        } else {
                            rv_ibu.setVisibility(View.VISIBLE);
                            tv_kosong.setVisibility(View.GONE);
                            initDataBunda(bundas);
                        }
                    } else {
                        rv_ibu.setVisibility(View.GONE);
                        tv_kosong.setVisibility(View.VISIBLE);
                    }
                } else {
                    rv_ibu.setVisibility(View.GONE);
                    tv_kosong.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<ResponseBunda> call, Throwable t) {
                pDialog.dismiss();
                rv_ibu.setVisibility(View.GONE);
                tv_kosong.setVisibility(View.VISIBLE);
            }
        });

    }

    private void initDataBunda(ArrayList<Bunda> bundas) {
        ibuKaderAdapter = new IbuKaderAdapter(DataIbuActivity.this, bundas);
        rv_ibu.setLayoutManager(new LinearLayoutManager(DataIbuActivity.this));
        rv_ibu.setAdapter(ibuKaderAdapter);
    }

    @Override
    public void onRefresh() {
        loadDataIbu();
    }
}