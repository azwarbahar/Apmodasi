package com.skripsi.apmodasi.ui.activity.kader;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.skripsi.apmodasi.R;
import com.skripsi.apmodasi.app.network.ApiClient;
import com.skripsi.apmodasi.app.network.ApiInterface;
import com.skripsi.apmodasi.app.response.ResponseBayi;
import com.skripsi.apmodasi.data.model.Bayi;
import com.skripsi.apmodasi.ui.adapter.BayiKaderAdapter;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DataBayiActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView rv_bayi;
    private BayiKaderAdapter bayiKaderAdapter;
    private ArrayList<Bayi> bayis;
    private TextView tv_kosong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_bayi);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        tv_kosong = findViewById(R.id.tv_kosong);
        rv_bayi = findViewById(R.id.rv_bayi);

        mSwipeRefreshLayout = findViewById(R.id.swipe_continer);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.purple_500,
                android.R.color.holo_blue_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_green_dark);
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                loadDataBayi();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void loadDataBayi(){

        mSwipeRefreshLayout.setRefreshing(false);
        SweetAlertDialog pDialog = new SweetAlertDialog(DataBayiActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBayi> responseBayiCall = apiInterface.getAllBayi();
        responseBayiCall.enqueue(new Callback<ResponseBayi>() {
            @Override
            public void onResponse(Call<ResponseBayi> call, Response<ResponseBayi> response) {
                pDialog.dismiss();
                if (response.isSuccessful()){
                    String kode = response.body().getKode();
                    if (kode.equals("1")){
                        bayis = (ArrayList<Bayi>) response.body().getBayi();
                        if (bayis.size() < 1){
                            rv_bayi.setVisibility(View.GONE);
                            tv_kosong.setVisibility(View.VISIBLE);
                        } else {
                            rv_bayi.setVisibility(View.VISIBLE);
                            tv_kosong.setVisibility(View.GONE);
                            initdataBayi(bayis);
                        }

                    } else {
                        rv_bayi.setVisibility(View.GONE);
                        tv_kosong.setVisibility(View.VISIBLE);
                    }
                } else {
                    rv_bayi.setVisibility(View.GONE);
                    tv_kosong.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<ResponseBayi> call, Throwable t) {
                pDialog.dismiss();
                rv_bayi.setVisibility(View.GONE);
                tv_kosong.setVisibility(View.VISIBLE);
            }
        });

    }

    private void initdataBayi(ArrayList<Bayi> bayis) {

        bayiKaderAdapter = new BayiKaderAdapter(DataBayiActivity.this, bayis);
        rv_bayi.setLayoutManager(new LinearLayoutManager(DataBayiActivity.this));
        rv_bayi.setAdapter(bayiKaderAdapter);

    }

    @Override
    public void onRefresh() {
        loadDataBayi();
    }
}