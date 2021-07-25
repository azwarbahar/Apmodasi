package com.skripsi.apmodasi.ui.activity.kader;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
    private EditText et_cari;
    private RelativeLayout rl_continer;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_bayi);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        et_cari = findViewById(R.id.et_cari);
        rl_continer = findViewById(R.id.rl_continer);


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

        et_cari.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().isEmpty() || editable.toString().equals("")) {
//                    changeDrawableEdittextDefault();
                } else {
                    filter(editable.toString());
                    changeDrawableEdittext();
                }
            }
        });

        et_cari.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    if (motionEvent.getRawX() >= (et_cari.getRight() - et_cari.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
                        et_cari.setText("");
                        rl_continer.requestFocus();
                        changeDrawableEdittextDefault();
                        loadDataBayi();
                        return true;
                    }
                }
                return false;
            }
        });
    }

    private void filter(String text) {
        ArrayList<Bayi> filteredList = new ArrayList<>();
        for (Bayi item : bayis) {
            if (item.getNamaBayi().toLowerCase().contains(text.toLowerCase()) ||
                    item.getNomorBayi().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }
        bayiKaderAdapter.filterList(filteredList);
    }

    private void changeDrawableEdittext() {
        et_cari.setCompoundDrawablesWithIntrinsicBounds(null, null,
                ContextCompat.getDrawable(DataBayiActivity.this, R.drawable.ic_baseline_close_24), null);

    }

    private void changeDrawableEdittextDefault() {
        et_cari.setCompoundDrawablesWithIntrinsicBounds(null, null,
                ContextCompat.getDrawable(DataBayiActivity.this, R.drawable.ic_baseline_search_24), null);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void loadDataBayi() {

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
                if (response.isSuccessful()) {
                    String kode = response.body().getKode();
                    if (kode.equals("1")) {
                        bayis = (ArrayList<Bayi>) response.body().getBayi();
                        if (bayis.size() < 1) {
                            rv_bayi.setVisibility(View.GONE);
                            tv_kosong.setVisibility(View.VISIBLE);
                            et_cari.setEnabled(false);
                        } else {
                            et_cari.setEnabled(true);
                            rv_bayi.setVisibility(View.VISIBLE);
                            tv_kosong.setVisibility(View.GONE);
                            initdataBayi(bayis);
                        }

                    } else {
                        et_cari.setEnabled(false);
                        rv_bayi.setVisibility(View.GONE);
                        tv_kosong.setVisibility(View.VISIBLE);
                    }
                } else {
                    et_cari.setEnabled(false);
                    rv_bayi.setVisibility(View.GONE);
                    tv_kosong.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<ResponseBayi> call, Throwable t) {
                pDialog.dismiss();
                et_cari.setEnabled(false);
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