package com.skripsi.apmodasi.ui.activity.kader;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.skripsi.apmodasi.R;
import com.skripsi.apmodasi.app.network.ApiClient;
import com.skripsi.apmodasi.app.network.ApiInterface;
import com.skripsi.apmodasi.app.response.ResponseBayi;
import com.skripsi.apmodasi.app.response.ResponseImunisasi;
import com.skripsi.apmodasi.app.util.Constanta;
import com.skripsi.apmodasi.data.model.Bayi;
import com.skripsi.apmodasi.data.model.BeratBadan;
import com.skripsi.apmodasi.data.model.Imunisasi;
import com.skripsi.apmodasi.data.model.TinggiBadan;
import com.skripsi.apmodasi.ui.activity.ImageViewActivity;
import com.skripsi.apmodasi.ui.activity.bunda.DetailBayiActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InputDataBayiActivity extends AppCompatActivity {

    private String user_id;
    private SharedPreferences mPreferences;

    private BeratBadan beratBadan;
    private TinggiBadan tinggiBadan;
    private ArrayList<Imunisasi> imunisasis;

    private Bayi bayi;
    private String bayi_id;
    private String bunda_id;
    private View dialogView;

    private TextView tv_nama_bayi;
    private TextView tv_usia;
    private TextView tv_nama_bunda;
    private TextView tv_imunisasi;
    private ImageView foto_bayi;
    private CardView cv_foto_bayi;

    private String[] no_imunisasiArray = null;
    private String[] nama_imunisasiArray = null;

    private RelativeLayout rl_imunisasi_bayi;
    private RelativeLayout rl_tinggi_bayi;
    private RelativeLayout rl_berat_bayi;
    private RelativeLayout rl_batal;

    private boolean berat_is_ready = true;
    private boolean tinggi_is_ready = true;
    private boolean imunisasi_is_ready = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_data_bayi);

        mPreferences = getSharedPreferences(Constanta.MY_SHARED_PREFERENCES, MODE_PRIVATE);
        user_id = mPreferences.getString(Constanta.SESSION_ID_USER, "");

        rl_imunisasi_bayi = findViewById(R.id.rl_imunisasi_bayi);
        rl_tinggi_bayi = findViewById(R.id.rl_tinggi_bayi);
        rl_berat_bayi = findViewById(R.id.rl_berat_bayi);
        rl_batal = findViewById(R.id.rl_batal);
        cv_foto_bayi = findViewById(R.id.cv_foto_bayi);
        tv_nama_bayi = findViewById(R.id.tv_nama_bayi);
        tv_usia = findViewById(R.id.tv_usia);
        tv_nama_bunda = findViewById(R.id.tv_nama_bunda);
        tv_imunisasi = findViewById(R.id.tv_imunisasi);
        foto_bayi = findViewById(R.id.foto_bayi);
        String data = getIntent().getStringExtra("DATA");
        if (data.equals("")) {
            SweetAlertDialog gagal = new SweetAlertDialog(InputDataBayiActivity.this, SweetAlertDialog.ERROR_TYPE);
            gagal.setTitleText("Terjadi Kesalahan");
            gagal.setContentText("Data tidak ditemukan!");
            gagal.setConfirmButton("Kelur", new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    sweetAlertDialog.dismiss();
                    finish();
                }
            });
            gagal.setCancelable(false);
            gagal.show();
        } else {
            loadDataBayi(data);
        }

//        Toast.makeText(this, getIntent().getStringExtra("DATA"), Toast.LENGTH_SHORT).show();

        rl_batal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        cv_foto_bayi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InputDataBayiActivity.this, ImageViewActivity.class);
                intent.putExtra("data_image", "Bayi");
                startActivity(intent);
            }
        });

        rl_imunisasi_bayi.setOnClickListener(this::clickImunisasi);
        rl_tinggi_bayi.setOnClickListener(this::clickTinggi);
        rl_berat_bayi.setOnClickListener(this::clickBerat);
    }

    private void loadBeratBadanBayi(String bayi_id) {

        SweetAlertDialog pDialog = new SweetAlertDialog(InputDataBayiActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();

        String tanggal_send = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseImunisasi> responseImunisasiCall = apiInterface.getBeratBayiToday(bayi_id, tanggal_send);
        responseImunisasiCall.enqueue(new Callback<ResponseImunisasi>() {
            @Override
            public void onResponse(Call<ResponseImunisasi> call, Response<ResponseImunisasi> response) {
                pDialog.dismiss();
                if (response.isSuccessful()) {
                    String kode = response.body().getKode();
                    if (kode.equals("1")) {
                        beratBadan = response.body().getBerat_bayi_data();
                        if (beratBadan == null){
                            Log.e("Berat", " data null");
                            rl_berat_bayi.setBackground(ContextCompat.getDrawable(
                                    InputDataBayiActivity.this, R.drawable.bg_radius_stroke_white));
                            berat_is_ready = true;
                        } else {
                            Log.e("Berat", " harusnya ada");
                            rl_berat_bayi.setBackground(ContextCompat.getDrawable(
                                    InputDataBayiActivity.this, R.drawable.bg_radius_stroke_grey));
                            berat_is_ready = false;
                        }
                    } else {
                        showSnackbar("Memanggil Berat bayi gagal!");
                    }
                } else {
                    showSnackbar("Memanggil Berat bayi gagal!");
                }
            }

            @Override
            public void onFailure(Call<ResponseImunisasi> call, Throwable t) {
                pDialog.dismiss();
                showSnackbar("Memanggil Berat bayi gagal!");
            }
        });
    }

    private void loadImunisasiBayi(String bayi_id) {
        SweetAlertDialog pDialog = new SweetAlertDialog(InputDataBayiActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseImunisasi> responseImunisasiCall = apiInterface.getImunisasiStatusBayi(bayi_id, "Belum");
        responseImunisasiCall.enqueue(new Callback<ResponseImunisasi>() {
            @Override
            public void onResponse(Call<ResponseImunisasi> call, Response<ResponseImunisasi> response) {
                pDialog.dismiss();
                if (response.isSuccessful()) {
                    String kode = response.body().getKode();
                    if (kode.equals("1")) {
                        imunisasis = (ArrayList<Imunisasi>) response.body().getImunisasi_bayi();
                        initImunisasi(imunisasis);
//                        showSnackbar("Ada imunisasi !");
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseImunisasi> call, Throwable t) {
                pDialog.dismiss();
                showSnackbar("Memanggil Imunisasi Gagal!");

            }
        });

    }

    private void initImunisasi(ArrayList<Imunisasi> imunisasis) {

        if (imunisasis.size() < 1){
            Log.e("Imunisasi", "Kosong");
        }
        no_imunisasiArray = new String[imunisasis.size()];
        nama_imunisasiArray = new String[imunisasis.size()];
        for (int a = 0; a < imunisasis.size(); a++){
                no_imunisasiArray[a] = imunisasis.get(a).getNo_imunisasi();
                nama_imunisasiArray[a] = imunisasis.get(a).getNama_imunisasi();

            Log.e("Imunisasi", nama_imunisasiArray[a]);
        }


    }

    private void showSnackbar(String message) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT)
                .setActionTextColor(getResources().getColor(android.R.color.holo_red_light))
                .show();
    }

    private void loadDataBayi(String nomor_bayi) {

        SweetAlertDialog pDialog = new SweetAlertDialog(InputDataBayiActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBayi> responseBayiCall = apiInterface.getBayiNomor(nomor_bayi);
        responseBayiCall.enqueue(new Callback<ResponseBayi>() {
            @Override
            public void onResponse(Call<ResponseBayi> call, Response<ResponseBayi> response) {
                pDialog.dismiss();
                if (response.isSuccessful()) {
                    String kode = response.body().getKode();
                    if (kode.equals("1")) {
                        bayi = response.body().getResult_bayi();
                        initDataBayi(bayi);
                    } else {
                        SweetAlertDialog gagal = new SweetAlertDialog(InputDataBayiActivity.this, SweetAlertDialog.ERROR_TYPE);
                        gagal.setTitleText("Terjadi Kesalahan");
                        gagal.setContentText("Data tidak ditemukan!");
                        gagal.setConfirmButton("Kelur", new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                                finish();
                            }
                        });
                        gagal.setCancelable(false);
                        gagal.show();
                    }
                } else {
                    SweetAlertDialog gagal = new SweetAlertDialog(InputDataBayiActivity.this, SweetAlertDialog.ERROR_TYPE);
                    gagal.setTitleText("Terjadi Kesalahan");
                    gagal.setContentText("Data tidak ditemukan!");
                    gagal.setConfirmButton("Kelur", new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismiss();
                            finish();
                        }
                    });
                    gagal.setCancelable(false);
                    gagal.show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBayi> call, Throwable t) {
                pDialog.dismiss();
                SweetAlertDialog gagal = new SweetAlertDialog(InputDataBayiActivity.this, SweetAlertDialog.ERROR_TYPE);
                gagal.setTitleText("Terjadi Kesalahan");
                gagal.setContentText("Data tidak ditemukan!");
                gagal.setConfirmButton("Kelur", new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                        finish();
                    }
                });
                gagal.setCancelable(false);
                gagal.show();
            }
        });

    }

    private void initDataBayi(Bayi bayi) {
        bayi_id = bayi.getIdBayi();
        loadBeratBadanBayi(bayi_id);
        loadImunisasiBayi(bayi_id);
        bunda_id = bayi.getBundaId();
        tv_usia.setText(settingDate(bayi.getTanggalLahirBayi()));
        tv_nama_bayi.setText(bayi.getNamaBayi());
        tv_nama_bunda.setText(bayi.getBundaId());
        Glide.with(this)
                .load(Constanta.URL_IMG_BAYI + bayi.getFotoBayi())
                .into(foto_bayi);
    }

    private String settingDate(String tanggalLahirBayi) {
        Calendar currentDate = Calendar.getInstance();
        SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date birthdate = null;
        String tanggal = null;
        try {
            birthdate = myFormat.parse(tanggalLahirBayi);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Long time = currentDate.getTime().getTime() / 1000 - birthdate.getTime() / 1000;
        int years = Math.round(time) / 31536000;
        int months = Math.round(time - years * 31536000) / 2628000;
        if (years > 0) {
            tanggal = years + " Tahun, " + months + " Bulan";
        } else {
            tanggal = months + " Bulan";
        }
        return tanggal;
    }

    private void clickImunisasi(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pilih Imunisasi");
        builder.setItems(nama_imunisasiArray, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // the user clicked on colors[which]

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        SweetAlertDialog pDialog = new SweetAlertDialog(InputDataBayiActivity.this, SweetAlertDialog.PROGRESS_TYPE);
                        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                        pDialog.setTitleText("Loading..");
                        pDialog.setCancelable(false);
                        pDialog.show();

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                pDialog.dismiss();
                                dialog.dismiss();
                                sendImunisasi(which);
                            }
                        }, 2500);
                    }
                }, 400);


            }
        });
        builder.show();
    }

    private void sendImunisasi(int whichImunisasi) {

        AlertDialog.Builder dialog = new AlertDialog.Builder(InputDataBayiActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.input_dialog_imunisasi_bayi, null);
        dialog.setView(dialogView);
        dialog.setCancelable(false);
        dialog.setTitle("Berat Bayi");
        dialog.setPositiveButton("Simpan", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditText et_catatan_imunisasi = dialogView.findViewById(R.id.et_catatan_imunisasi);
                SweetAlertDialog pDialog = new SweetAlertDialog(InputDataBayiActivity.this, SweetAlertDialog.PROGRESS_TYPE);
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                pDialog.setTitleText("Loading");
                pDialog.setCancelable(false);
                pDialog.show();

                String bayi_id_send = bayi_id;
                String no_imunisasi = no_imunisasiArray[whichImunisasi];
                String usia_bayi_send = settingDate(bayi.getTanggalLahirBayi());
                String kader_id_send = user_id;
                String status_imunisasi = "Sudah";
                String catatan_send = et_catatan_imunisasi.getText().toString();
                String tanggal_send = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

                ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
                Call<ResponseImunisasi> responseImunisasiCall = apiInterface.editImunisasi(bayi_id_send,
                        no_imunisasi, usia_bayi_send, kader_id_send, status_imunisasi, catatan_send, tanggal_send);
                responseImunisasiCall.enqueue(new Callback<ResponseImunisasi>() {
                    @Override
                    public void onResponse(Call<ResponseImunisasi> call, Response<ResponseImunisasi> response) {
                        pDialog.dismiss();
                        if (response.isSuccessful()) {
                            String kode = response.body().getKode();
                            if (kode.equals("1")) {
                                new SweetAlertDialog(InputDataBayiActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                        .setTitleText("Success..")
                                        .setContentText("Berhasil Menyimpan Imunisasi Bayi..")
                                        .setConfirmButton("Ok", new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                sweetAlertDialog.dismiss();
                                                dialog.dismiss();
                                            }
                                        })
                                        .show();
                            } else {
                                new SweetAlertDialog(InputDataBayiActivity.this, SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("Gagal..")
                                        .setContentText("Terjadi kesalahan!, Kode : " + kode)
                                        .show();
                            }
                        } else {
                            new SweetAlertDialog(InputDataBayiActivity.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Gagal..")
                                    .setContentText("Terjadi kesalahan!")
                                    .show();
                        }

                    }

                    @Override
                    public void onFailure(Call<ResponseImunisasi> call, Throwable t) {
                        pDialog.dismiss();
                        new SweetAlertDialog(InputDataBayiActivity.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Gagal..")
                                .setContentText("Terjadi kesalahan Sistem!")
                                .show();

                    }
                });
            }
        });
        dialog.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void clickBerat(View view) {

        if (berat_is_ready){
            AlertDialog.Builder dialog = new AlertDialog.Builder(InputDataBayiActivity.this);
            LayoutInflater inflater = getLayoutInflater();
            dialogView = inflater.inflate(R.layout.input_dialog_berat_bayi, null);
            dialog.setView(dialogView);
            dialog.setCancelable(false);
            dialog.setTitle("Berat Bayi");
            dialog.setPositiveButton("Simpan", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    EditText et_berat = dialogView.findViewById(R.id.et_berat);
                    EditText et_catatan_berat = dialogView.findViewById(R.id.et_catatan_berat);
                    if (et_berat.getText().toString().equals("")) {
                        et_berat.setError("Lengkapi");
                    } else {
                        SweetAlertDialog pDialog = new SweetAlertDialog(InputDataBayiActivity.this, SweetAlertDialog.PROGRESS_TYPE);
                        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                        pDialog.setTitleText("Loading");
                        pDialog.setCancelable(false);
                        pDialog.show();

                        String bayi_id_send = bayi_id;
                        String berat_send = et_berat.getText().toString();
                        String catatan_send = et_catatan_berat.getText().toString();
                        String kader_id_send = user_id;
                        String tanggal_send = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

                        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
                        Call<ResponseImunisasi> responseImunisasiCall = apiInterface.addBeratBayi(bayi_id_send, berat_send,
                                catatan_send, kader_id_send, tanggal_send);
                        responseImunisasiCall.enqueue(new Callback<ResponseImunisasi>() {
                            @Override
                            public void onResponse(Call<ResponseImunisasi> call, Response<ResponseImunisasi> response) {
                                pDialog.dismiss();
                                if (response.isSuccessful()) {
                                    String kode = response.body().getKode();
                                    if (kode.equals("1")) {
                                        new SweetAlertDialog(InputDataBayiActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                                .setTitleText("Success..")
                                                .setContentText("Berhasil Mengisi Berat Bayi..")
                                                .setConfirmButton("Ok", new SweetAlertDialog.OnSweetClickListener() {
                                                    @Override
                                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                        sweetAlertDialog.dismiss();
                                                        loadBeratBadanBayi(bayi_id);
                                                        dialog.dismiss();
                                                    }
                                                })
                                                .show();
                                    } else {
                                        new SweetAlertDialog(InputDataBayiActivity.this, SweetAlertDialog.ERROR_TYPE)
                                                .setTitleText("Gagal..")
                                                .setContentText("Terjadi kesalahan!, Kode : " + kode)
                                                .show();
                                    }
                                } else {
                                    new SweetAlertDialog(InputDataBayiActivity.this, SweetAlertDialog.ERROR_TYPE)
                                            .setTitleText("Gagal..")
                                            .setContentText("Terjadi kesalahan!")
                                            .show();
                                }

                            }

                            @Override
                            public void onFailure(Call<ResponseImunisasi> call, Throwable t) {
                                pDialog.dismiss();
                                new SweetAlertDialog(InputDataBayiActivity.this, SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("Gagal..")
                                        .setContentText("Terjadi kesalahan Sistem!")
                                        .show();

                            }
                        });
                    }
                }
            });
            dialog.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        } else {
            new SweetAlertDialog(InputDataBayiActivity.this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Sorry")
                    .setContentText("Berat Bayi sudah diinput hari ini.")
                    .show();
        }
    }

    private void clickTinggi(View view) {

        AlertDialog.Builder dialog = new AlertDialog.Builder(InputDataBayiActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.input_dialog_tinggi_bayi, null);
        dialog.setView(dialogView);
        dialog.setCancelable(false);
        dialog.setTitle("Tinggi Bayi");
        dialog.setPositiveButton("Simpan", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditText et_tinggi = dialogView.findViewById(R.id.et_tinggi);
                EditText et_catatan_tinggi = dialogView.findViewById(R.id.et_catatan_tinggi);
                if (et_tinggi.getText().toString().equals("")) {
                    et_tinggi.setError("Lengkapi");
                } else {
                    SweetAlertDialog pDialog = new SweetAlertDialog(InputDataBayiActivity.this, SweetAlertDialog.PROGRESS_TYPE);
                    pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                    pDialog.setTitleText("Loading");
                    pDialog.setCancelable(false);
                    pDialog.show();

                    String bayi_id_send = bayi_id;
                    String berat_send = et_tinggi.getText().toString();
                    String catatan_send = et_catatan_tinggi.getText().toString();
                    String kader_id_send = user_id;
                    String tanggal_send = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

                    ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
                    Call<ResponseImunisasi> responseImunisasiCall = apiInterface.addTinggiBayi(bayi_id_send, berat_send,
                            catatan_send, kader_id_send, tanggal_send);
                    responseImunisasiCall.enqueue(new Callback<ResponseImunisasi>() {
                        @Override
                        public void onResponse(Call<ResponseImunisasi> call, Response<ResponseImunisasi> response) {
                            pDialog.dismiss();
                            if (response.isSuccessful()) {
                                String kode = response.body().getKode();
                                if (kode.equals("1")) {
                                    new SweetAlertDialog(InputDataBayiActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                            .setTitleText("Success..")
                                            .setContentText("Berhasil Mengisi Tinggi Bayi..")
                                            .setConfirmButton("Ok", new SweetAlertDialog.OnSweetClickListener() {
                                                @Override
                                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                    sweetAlertDialog.dismiss();
                                                    dialog.dismiss();
                                                }
                                            })
                                            .show();
                                } else {
                                    new SweetAlertDialog(InputDataBayiActivity.this, SweetAlertDialog.ERROR_TYPE)
                                            .setTitleText("Gagal..")
                                            .setContentText("Terjadi kesalahan!, Kode : " + kode)
                                            .show();
                                }
                            } else {
                                new SweetAlertDialog(InputDataBayiActivity.this, SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("Gagal..")
                                        .setContentText("Terjadi kesalahan!")
                                        .show();
                            }

                        }

                        @Override
                        public void onFailure(Call<ResponseImunisasi> call, Throwable t) {
                            pDialog.dismiss();
                            new SweetAlertDialog(InputDataBayiActivity.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Gagal..")
                                    .setContentText("Terjadi kesalahan Sistem!")
                                    .show();

                        }
                    });
                }
            }
        });
        dialog.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}