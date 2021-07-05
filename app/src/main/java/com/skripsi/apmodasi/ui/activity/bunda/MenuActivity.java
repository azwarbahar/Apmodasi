package com.skripsi.apmodasi.ui.activity.bunda;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import com.bumptech.glide.Glide;
import com.skripsi.apmodasi.R;
import com.skripsi.apmodasi.app.network.ApiClient;
import com.skripsi.apmodasi.app.network.ApiInterface;
import com.skripsi.apmodasi.app.response.ResponseBayi;
import com.skripsi.apmodasi.app.response.ResponseBunda;
import com.skripsi.apmodasi.app.util.Constanta;
import com.skripsi.apmodasi.data.model.Bayi;
import com.skripsi.apmodasi.data.model.Bunda;
import com.skripsi.apmodasi.ui.activity.ImageViewActivity;
import com.skripsi.apmodasi.ui.activity.intro.LoginActivity;
import com.skripsi.apmodasi.ui.activity.kader.InputDataBayiActivity;
import com.skripsi.apmodasi.ui.adapter.BayiAdapter;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MenuActivity extends AppCompatActivity {

    private SharedPreferences mPreferences;

    private RecyclerView rv_bayi;
    private BayiAdapter bayiAdapter;
    private CardView cv_foto;
    private CardView cv_profile;
    private TextView tv_tambah_anak;
    private TextView tv_bayi_kosong;
    private TextView tv_nama;
    private TextView tv_telpon;
    private ImageView foto_profil;
    private ImageView img_refresh;

    private DatePickerDialog datePickerDialog;

    // sliding pannel
    private SlidingUpPanelLayout sliding_layout;
    private EditText et_nama_lengkap;
    private RadioGroup radio_jenis_kelamin;
    private RadioButton radio_laki;
    private RadioButton radio_perempuan;
    private TextView et_tanggal_lahir;
    private RelativeLayout rl_simpan;

    private String user_id;
    private Bunda bunda;
    private ArrayList<Bayi> bayiArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        mPreferences = getSharedPreferences(Constanta.MY_SHARED_PREFERENCES, MODE_PRIVATE);
        user_id = mPreferences.getString(Constanta.SESSION_ID_USER, "");

        // sliding pannel
        sliding_layout = findViewById(R.id.sliding_layout);
        et_nama_lengkap = findViewById(R.id.et_nama_lengkap);
        radio_jenis_kelamin = findViewById(R.id.radio_jenis_kelamin);
        radio_laki = findViewById(R.id.radio_laki);
        radio_perempuan = findViewById(R.id.radio_perempuan);
        et_tanggal_lahir = findViewById(R.id.et_tanggal_lahir);
        rl_simpan = findViewById(R.id.rl_simpan);

        tv_nama = findViewById(R.id.tv_nama);
        tv_telpon = findViewById(R.id.tv_telpon);
        foto_profil = findViewById(R.id.foto_profil);
        img_refresh = findViewById(R.id.img_refresh);


        rv_bayi = findViewById(R.id.rv_bayi);
        tv_bayi_kosong = findViewById(R.id.tv_bayi_kosong);
        tv_tambah_anak = findViewById(R.id.tv_tambah_anak);
        cv_profile = findViewById(R.id.cv_profile);
        cv_foto = findViewById(R.id.cv_foto);

        et_tanggal_lahir.setOnClickListener(this::clickTanggalLahir);
        tv_tambah_anak.setOnClickListener(this::clickTambahAnak);
        cv_profile.setOnClickListener(this::clickProfile);
        cv_foto.setOnClickListener(this::clickPhoto);
        rl_simpan.setOnClickListener(this::clickSimpan);

        loadDataBayi(user_id);
        loadDataBunda(user_id);

        img_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadDataBayi(user_id);
            }
        });

    }

    private void loadDataBayi(String user_id) {

        SweetAlertDialog pDialog = new SweetAlertDialog(MenuActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBayi> responseBayiCall = apiInterface.getBayiBunda(user_id);
        responseBayiCall.enqueue(new Callback<ResponseBayi>() {
            @Override
            public void onResponse(Call<ResponseBayi> call, Response<ResponseBayi> response) {
                pDialog.dismiss();
                if (response.isSuccessful()) {
                    String kode = response.body().getKode();
                    if (kode.equals("1")){
                        bayiArrayList = (ArrayList<Bayi>) response.body().getBayiData();
                        if (bayiArrayList == null || bayiArrayList.size() <= 0 || bayiArrayList.isEmpty()){
                            tv_bayi_kosong.setVisibility(View.VISIBLE);
                            rv_bayi.setVisibility(View.GONE);
                        } else {
                            tv_bayi_kosong.setVisibility(View.GONE);
                            rv_bayi.setVisibility(View.VISIBLE);
                            rv_bayi.setLayoutManager(new LinearLayoutManager(MenuActivity.this));
                            bayiAdapter = new BayiAdapter(MenuActivity.this, bayiArrayList);
                            rv_bayi.setAdapter(bayiAdapter);
                        }
                    } else {
                        tv_bayi_kosong.setVisibility(View.VISIBLE);
                        rv_bayi.setVisibility(View.GONE);
                    }
                } else {
                    tv_bayi_kosong.setVisibility(View.VISIBLE);
                    rv_bayi.setVisibility(View.GONE);
                }

            }

            @Override
            public void onFailure(Call<ResponseBayi> call, Throwable t) {
                pDialog.dismiss();
                tv_bayi_kosong.setVisibility(View.VISIBLE);
                rv_bayi.setVisibility(View.GONE);

            }
        });
    }

    private void loadDataBunda(String user_id){

        SweetAlertDialog pDialog = new SweetAlertDialog(MenuActivity.this, SweetAlertDialog.PROGRESS_TYPE);
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
                if (response.isSuccessful()){
                    String kode = response.body().getKode();
                    if (kode.equals("1")){
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
        String id_bunda = bunda.getIdBunda();
        String nama_bunda = bunda.getNamaBunda();
        String kontak_bunda = bunda.getKontakBunda();
        String alamat_bunda = bunda.getAlamatBunda();
        String foto_bunda = bunda.getFotoBunda();

        tv_nama.setText(nama_bunda);
        tv_telpon.setText(kontak_bunda);
        Glide.with(this)
                .load(Constanta.URL_IMG_BUNDA + foto_bunda)
                .into(foto_profil);


    }


    private void clickSimpan(View view) {

        String nama_lenglap = tv_nama.getText().toString();
        String jenis_kelamin;
        if (radio_laki.isChecked()){
            jenis_kelamin = radio_laki.getText().toString();
        } else {
            jenis_kelamin = radio_perempuan.getText().toString();
        }

        resetInputTambahBayi();
        loadDataBayi(user_id);
    }

    private void resetInputTambahBayi() {

        et_nama_lengkap.setText("");
        et_tanggal_lahir.setText("");

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
        Intent intent = new Intent(MenuActivity.this, ImageViewActivity.class);
        intent.putExtra("data_image", "Bunda");
        startActivity(intent);
    }
}