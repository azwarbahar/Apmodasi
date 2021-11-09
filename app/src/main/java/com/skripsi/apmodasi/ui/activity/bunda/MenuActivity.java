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
import android.os.Handler;
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
import com.skripsi.apmodasi.app.response.ResponseAuth;
import com.skripsi.apmodasi.app.response.ResponseBayi;
import com.skripsi.apmodasi.app.response.ResponseBunda;
import com.skripsi.apmodasi.app.util.Constanta;
import com.skripsi.apmodasi.data.model.AuthResult;
import com.skripsi.apmodasi.data.model.Bayi;
import com.skripsi.apmodasi.data.model.Bunda;
import com.skripsi.apmodasi.ui.activity.ImageViewActivity;
import com.skripsi.apmodasi.ui.activity.intro.LoginActivity;
import com.skripsi.apmodasi.ui.activity.intro.RegistrasiActivity;
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
    private CardView cv_bacaan;

    private DatePickerDialog datePickerDialog;

    // sliding pannel
    private SlidingUpPanelLayout sliding_layout;
    private EditText et_nama_lengkap;
    private RadioGroup radio_jenis_kelamin;
    private RadioButton radio_laki;
    private RadioButton radio_perempuan;
    private TextView et_tanggal_lahir;
    private RelativeLayout rl_simpan;
    private String tanggal_lahir_send;
    private String tahun;
    private String bulan;
    private String hari;
    private String kelurahan_bunda;
    private int count_baby;

    private String user_id;
    private String foto;
    private Bunda bunda;
    private ArrayList<Bayi> bayiArrayList;

    private AuthResult authResult;
    private boolean akun_aktif;
    private RelativeLayout rl_warning;
    private String status_auth;
    private TextView tv_warning;

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

        rl_warning = findViewById(R.id.rl_warning);
        tv_warning = findViewById(R.id.tv_warning);
        tv_nama = findViewById(R.id.tv_nama);
        tv_telpon = findViewById(R.id.tv_telpon);
        foto_profil = findViewById(R.id.foto_profil);
        img_refresh = findViewById(R.id.img_refresh);
        cv_bacaan = findViewById(R.id.cv_bacaan);

        rl_warning.setVisibility(View.GONE);

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
        cv_bacaan.setOnClickListener(this::clickBacaan);

//        Toast.makeText(this, user_id, Toast.LENGTH_SHORT).show();

        loadDataBayi(user_id);
        loadDataBunda(user_id);
        loadDataAuth(user_id);

        img_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadDataBayi(user_id);
                loadDataBunda(user_id);
                loadDataAuth(user_id);
            }
        });

    }

    private void loadDataAuth(String user_id) {

        SweetAlertDialog pDialog = new SweetAlertDialog(MenuActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseAuth> responseAuthCall = apiInterface.getAuth(user_id, "Bunda");
        responseAuthCall.enqueue(new Callback<ResponseAuth>() {
            @Override
            public void onResponse(Call<ResponseAuth> call, Response<ResponseAuth> response) {
                pDialog.dismiss();
                if (response.isSuccessful()){
                    String kode = response.body().getKode();
                    if (kode.equals("1")){
                        authResult = response.body().getAuthResult();
                        initAuth(authResult);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseAuth> call, Throwable t) {
                pDialog.dismiss();
            }
        });

    }

    private void initAuth(AuthResult authResult) {

        status_auth = authResult.getStatus();
        if (status_auth.equals("Inactive")){
            akun_aktif = false;
            rl_warning.setVisibility(View.VISIBLE);
        } else if (status_auth.equals("Suspend")){
            akun_aktif = false;
            tv_warning.setText("Akun anda telah disuspend, segera hubungi admin jika akun anda ingin kembali!");
            rl_warning.setVisibility(View.VISIBLE);
        } else {
            akun_aktif = true;
            rl_warning.setVisibility(View.GONE);
        }

    }

    private void clickBacaan(View view) {
        startActivity(new Intent(MenuActivity.this, BacaanActivity.class));
    }

    private void loadDataBayi(String user_id) {

        SweetAlertDialog pDialog = new SweetAlertDialog(MenuActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBayi> responseBayiCall = apiInterface.getBayiBunda(user_id, "Semua");
        responseBayiCall.enqueue(new Callback<ResponseBayi>() {
            @Override
            public void onResponse(Call<ResponseBayi> call, Response<ResponseBayi> response) {
                pDialog.dismiss();
                if (response.isSuccessful()) {
                    String kode = response.body().getKode();
                    if (kode.equals("1")) {
                        bayiArrayList = (ArrayList<Bayi>) response.body().getBayiData();
                        count_baby = bayiArrayList.size();
                        if (bayiArrayList == null || bayiArrayList.size() <= 0 || bayiArrayList.isEmpty()) {
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

    private void loadDataBunda(String user_id) {

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
                if (response.isSuccessful()) {
                    String kode = response.body().getKode();
                    if (kode.equals("1")) {
                        initDataBunda(response.body().getResult_bunda());
                    } else {
                        Toast.makeText(MenuActivity.this, "gagal1", Toast.LENGTH_SHORT).show();
                    }
                } else {

                    Toast.makeText(MenuActivity.this, "gagal2", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<ResponseBunda> call, Throwable t) {
                pDialog.dismiss();
                Toast.makeText(MenuActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });


    }

    private void initDataBunda(Bunda bunda) {
        String id_bunda = bunda.getNik_bunda();
        String nama_bunda = bunda.getNama_bunda();
        String kontak_bunda = bunda.getKontak_bunda();
        String alamat_bunda = bunda.getAlamat_bunda();
        kelurahan_bunda = bunda.getKelurahan_bunda();
        String foto_bunda = bunda.getFoto_bunda();
        foto = bunda.getFoto_bunda();

        tv_nama.setText(nama_bunda);
        tv_telpon.setText(kontak_bunda);
        Glide.with(this)
                .load(Constanta.URL_IMG_BUNDA + foto_bunda)
                .into(foto_profil);
    }


    private void clickSimpan(View view) {

        String nama_lenglap = et_nama_lengkap.getText().toString();
        String tanggal_lahir = tanggal_lahir_send;
        String jenis_kelamin;
        String jekel_singkat;
        if (radio_laki.isChecked()) {
            jenis_kelamin = radio_laki.getText().toString();
            jekel_singkat = "L";
        } else {
            jenis_kelamin = radio_perempuan.getText().toString();
            jekel_singkat = "P";
        }

        String kelurahan;

        if (kelurahan_bunda.equals("Tetebatu")){
            kelurahan = "A1";
        } else if (kelurahan_bunda.equals("Parangbanoa")){
            kelurahan = "A2";
        } else if (kelurahan_bunda.equals("Pangkabinanga")){
            kelurahan = "A3";
        } else if (kelurahan_bunda.equals("Jenetallasa")){
            kelurahan = "A4";
        } else if (kelurahan_bunda.equals("Bontoala")){
            kelurahan = "A5";
        } else if (kelurahan_bunda.equals("Panakukang")){
            kelurahan = "A6";
        } else if (kelurahan_bunda.equals("Taeng")){
            kelurahan = "A7";
        } else {
            kelurahan = "A8";
        }

        String gambar_qr = "-";
        String foto_bayi = "img_baby.png";
        String status_bayi = "Menunggu";
        String bunda_id = user_id;
        String nomor_bayi = kelurahan+"-"+bunda_id+"-"+(bayiArrayList.size() + 1) +"-"+ jekel_singkat;

        if (nama_lenglap.equals("")) {
            new SweetAlertDialog(MenuActivity.this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Opss..")
                    .setContentText("Nama lengkap tidak boleh kosong")
                    .show();
        } else if (tanggal_lahir.equals("")) {
            new SweetAlertDialog(MenuActivity.this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Opss..")
                    .setContentText("Tanggal lahir tidak boleh kosong")
                    .show();
        } else if (jenis_kelamin.equals("")) {
            new SweetAlertDialog(MenuActivity.this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Opss..")
                    .setContentText("Jenis kelamin tidak boleh kosong")
                    .show();
        } else {
            sendDataBayi(nomor_bayi, nama_lenglap, tanggal_lahir, jenis_kelamin, gambar_qr, foto_bayi,
                    status_bayi, bunda_id);
        }

    }

    private void sendDataBayi(String nomor_bayi, String nama_lenglap, String tanggal_lahir,
                              String jenis_kelamin, String gambar_qr, String foto_bayi,
                              String status_bayi, String bunda_id) {

        SweetAlertDialog pDialog = new SweetAlertDialog(MenuActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading..");
        pDialog.setCancelable(false);
        pDialog.show();

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBayi> responseBayiCall = apiInterface.addBayi(nomor_bayi, nama_lenglap,
                tanggal_lahir, jenis_kelamin, gambar_qr, foto_bayi, status_bayi, bunda_id);
        responseBayiCall.enqueue(new Callback<ResponseBayi>() {
            @Override
            public void onResponse(Call<ResponseBayi> call, Response<ResponseBayi> response) {
                pDialog.dismiss();
                if (response.isSuccessful()) {
                    String kode = response.body().getKode();
                    if (kode.equals("1")) {
                        new SweetAlertDialog(MenuActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("Success..")
                                .setContentText("Berhasil menambahkan data bayi..")
                                .setConfirmButton("Ok", new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        sweetAlertDialog.dismiss();
                                        configIntent();
                                    }
                                })
                                .show();
                    } else {
                        new SweetAlertDialog(MenuActivity.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Gagal..")
                                .setContentText("Terjadi kesalahan!, Kode : " + kode)
                                .show();
                    }
                } else {
                    new SweetAlertDialog(MenuActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Gagal..")
                            .setContentText("Terjadi kesalahan!")
                            .show();
                }

            }

            @Override
            public void onFailure(Call<ResponseBayi> call, Throwable t) {
                pDialog.dismiss();
                new SweetAlertDialog(MenuActivity.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Gagal..")
                        .setContentText("Terjadi kesalahan Sistem!")
                        .show();
            }
        });


    }

    private void configIntent() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                SweetAlertDialog pDialog = new SweetAlertDialog(MenuActivity.this, SweetAlertDialog.PROGRESS_TYPE);
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                pDialog.setTitleText("Menyimpan Data..");
                pDialog.setCancelable(true);
                pDialog.show();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pDialog.dismiss();
                        showPanel();
                        resetInputTambahBayi();
                        loadDataBayi(user_id);
                    }
                }, 2500);
            }
        }, 400);
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
        final DateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy");
        final DateFormat dateFormatSend = new SimpleDateFormat("yyyy-MM-dd");
        final DateFormat dateFormatDay = new SimpleDateFormat("dd");
        final DateFormat dateFormatMonth = new SimpleDateFormat("MM");
        final DateFormat dateFormatYear = new SimpleDateFormat("yy");
        Calendar newCalendar = Calendar.getInstance();

        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                Calendar newDate = Calendar.getInstance();
                newDate.set(year, month, dayOfMonth);

                et_tanggal_lahir.setText(dateFormat.format(newDate.getTime()));
                tanggal_lahir_send = dateFormatSend.format(newDate.getTime());
                hari = dateFormatDay.format(newDate.getTime());
                bulan = dateFormatMonth.format(newDate.getTime());
                tahun = dateFormatYear.format(newDate.getTime());
            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }

    private void clickTambahAnak(View view) {
        if (akun_aktif){
            showPanel();
        } else {
            new SweetAlertDialog(MenuActivity.this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Maaf..")
                    .setContentText("Akun anda belum diverifikasi")
                    .show();
        }
    }

    private void clickProfile(View view) {
        startActivity(new Intent(MenuActivity.this, AkunActivity.class));
    }

    private void clickPhoto(View view) {
        Intent intent = new Intent(MenuActivity.this, ImageViewActivity.class);
        intent.putExtra("nama_foto", foto);
        intent.putExtra("role_foto", "Bunda");
        startActivity(intent);
    }
}