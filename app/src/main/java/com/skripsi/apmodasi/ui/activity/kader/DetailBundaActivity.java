package com.skripsi.apmodasi.ui.activity.kader;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.skripsi.apmodasi.R;
import com.skripsi.apmodasi.ui.adapter.BayiKaderAdapter;

public class DetailBundaActivity extends AppCompatActivity {

    private RecyclerView rv_bayi;
    private BayiKaderAdapter bayiKaderAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_bunda);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        loadDataBayi();
    }

    private void loadDataBayi(){
        rv_bayi = findViewById(R.id.rv_bayi);
        bayiKaderAdapter = new BayiKaderAdapter(DetailBundaActivity.this);
        rv_bayi.setLayoutManager(new LinearLayoutManager(DetailBundaActivity.this));
        rv_bayi.setAdapter(bayiKaderAdapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}