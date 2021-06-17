package com.skripsi.apmodasi.ui.activity.bunda;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.skripsi.apmodasi.R;
import com.skripsi.apmodasi.ui.activity.ImageViewActivity;
import com.skripsi.apmodasi.ui.adapter.BayiAdapter;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

public class MenuActivity extends AppCompatActivity {

    private RecyclerView rv_bayi;
    private BayiAdapter bayiAdapter;
    private CardView cv_foto;
    private CardView cv_profile;
    private TextView tv_tambah_anak;

    private DatePickerDialog datePickerDialog;

    // sliding pannel
    private SlidingUpPanelLayout sliding_layout;
    private EditText et_nama_lengkap;
    private RadioGroup radio_jenis_kelamin;
    private RadioButton radio_laki;
    private RadioButton radio_perempuan;
    private TextView et_tanggal_lahir;
    private RelativeLayout rl_simpan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        // sliding pannel
        sliding_layout = findViewById(R.id.sliding_layout);
        et_nama_lengkap = findViewById(R.id.et_nama_lengkap);
        radio_jenis_kelamin = findViewById(R.id.radio_jenis_kelamin);
        radio_laki = findViewById(R.id.radio_laki);
        radio_perempuan = findViewById(R.id.radio_perempuan);
        et_tanggal_lahir = findViewById(R.id.et_tanggal_lahir);
        rl_simpan = findViewById(R.id.rl_simpan);

        rv_bayi = findViewById(R.id.rv_bayi);
        tv_tambah_anak = findViewById(R.id.tv_tambah_anak);
        cv_profile = findViewById(R.id.cv_profile);
        cv_foto = findViewById(R.id.cv_foto);

        et_tanggal_lahir.setOnClickListener(this::clickTanggalLahir);
        tv_tambah_anak.setOnClickListener(this::clickTambahAnak);
        cv_profile.setOnClickListener(this::clickProfile);
        cv_foto.setOnClickListener(this::clickPhoto);
        rl_simpan.setOnClickListener(this::clickSimpan);

        loadDataBayi();

    }

    private void loadDataBayi() {
        Toast.makeText(this, "Load Data Bayi", Toast.LENGTH_SHORT).show();
        rv_bayi.setLayoutManager(new LinearLayoutManager(this));
        bayiAdapter = new BayiAdapter(this);
        rv_bayi.setAdapter(bayiAdapter);
    }


    private void clickSimpan(View view) {
        showPanel();
        resetInputTambahBayi();
        loadDataBayi();
    }

    private void resetInputTambahBayi() {

        et_nama_lengkap.setText("");
        et_tanggal_lahir.setText("Lengkapi");

    }

    private void showPanel() {
        if (sliding_layout != null &&
                (sliding_layout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED || sliding_layout.getPanelState() == SlidingUpPanelLayout.PanelState.ANCHORED)) {
            sliding_layout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        } else {
            sliding_layout.setPanelState(SlidingUpPanelLayout.PanelState.ANCHORED);
        }

    }

    private void clickTanggalLahir(View view) {
        final DateFormat dateFormat = new SimpleDateFormat("EEEE, dd MMMM yyyy");
        Calendar newCalendar = Calendar.getInstance();

        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                Calendar newDate = Calendar.getInstance();
                newDate.set(year, month, dayOfMonth);

                et_tanggal_lahir.setText(dateFormat.format(newDate.getTime()));

            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }

    private void clickTambahAnak(View view) {
        showPanel();
    }

    private void clickProfile(View view) {
        startActivity(new Intent(MenuActivity.this, AkunActivity.class));
    }

    private void clickPhoto(View view) {
        startActivity(new Intent(MenuActivity.this, ImageViewActivity.class));
    }
}