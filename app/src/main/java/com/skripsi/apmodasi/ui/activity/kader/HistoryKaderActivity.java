package com.skripsi.apmodasi.ui.activity.kader;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.skripsi.apmodasi.R;
import com.skripsi.apmodasi.app.network.ApiClient;
import com.skripsi.apmodasi.app.network.ApiInterface;
import com.skripsi.apmodasi.app.response.ResponseKader;
import com.skripsi.apmodasi.app.util.Constanta;
import com.skripsi.apmodasi.data.model.RiwayatKader;
import com.skripsi.apmodasi.ui.adapter.BayiKaderAdapter;
import com.skripsi.apmodasi.ui.adapter.HistoryKaderAdapter;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryKaderActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private String user_id;
    private SharedPreferences mPreferences;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView rv_history;
    private HistoryKaderAdapter historyKaderAdapter;
    private TextView tv_kosong;
    private ArrayList<RiwayatKader> riwayatKaders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_kader);

        mPreferences = getSharedPreferences(Constanta.MY_SHARED_PREFERENCES, MODE_PRIVATE);
        user_id = mPreferences.getString(Constanta.SESSION_ID_USER, "");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        tv_kosong = findViewById(R.id.tv_kosong);
        rv_history = findViewById(R.id.rv_history);

        mSwipeRefreshLayout = findViewById(R.id.swipe_continer);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.purple_500,
                android.R.color.holo_blue_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_green_dark);
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                loadDataBayi(user_id);
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void loadDataBayi(String user_id){

        mSwipeRefreshLayout.setRefreshing(false);
        SweetAlertDialog pDialog = new SweetAlertDialog(HistoryKaderActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseKader> responseKaderCall = apiInterface.getRiwayatKader(user_id);
        responseKaderCall.enqueue(new Callback<ResponseKader>() {
            @Override
            public void onResponse(Call<ResponseKader> call, Response<ResponseKader> response) {
                pDialog.dismiss();
                if (response.isSuccessful()){
                    String kode = response.body().getKode();
                    if (kode.equals("1")){
                        tv_kosong.setVisibility(View.GONE);
                        rv_history.setVisibility(View.VISIBLE);
                        riwayatKaders = (ArrayList<RiwayatKader>) response.body().getRiwayat_kader();
                        historyKaderAdapter = new HistoryKaderAdapter(HistoryKaderActivity.this, riwayatKaders);
                        rv_history.setLayoutManager(new LinearLayoutManager(HistoryKaderActivity.this));
                        rv_history.setAdapter(historyKaderAdapter);
                    } else {
                        tv_kosong.setVisibility(View.VISIBLE);
                        rv_history.setVisibility(View.GONE);
                    }
                } else {
                    tv_kosong.setVisibility(View.VISIBLE);
                    rv_history.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<ResponseKader> call, Throwable t) {
                pDialog.dismiss();
                tv_kosong.setVisibility(View.VISIBLE);
                rv_history.setVisibility(View.GONE);
            }
        });

    }

    @Override
    public void onRefresh() {
        loadDataBayi(user_id);
    }
}