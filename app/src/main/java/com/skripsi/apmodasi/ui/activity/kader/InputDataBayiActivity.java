package com.skripsi.apmodasi.ui.activity.kader;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.skripsi.apmodasi.R;
import com.skripsi.apmodasi.app.network.ApiClient;
import com.skripsi.apmodasi.app.network.ApiInterface;
import com.skripsi.apmodasi.app.response.ResponseBayi;
import com.skripsi.apmodasi.app.util.Constanta;
import com.skripsi.apmodasi.data.model.Bayi;
import com.skripsi.apmodasi.ui.activity.ImageViewActivity;
import com.skripsi.apmodasi.ui.activity.bunda.DetailBayiActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InputDataBayiActivity extends AppCompatActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_data_bayi);

        RelativeLayout rl_imunisasi_bayi = findViewById(R.id.rl_imunisasi_bayi);
        RelativeLayout rl_tinggi_bayi = findViewById(R.id.rl_tinggi_bayi);
        RelativeLayout rl_berat_bayi = findViewById(R.id.rl_berat_bayi);
        RelativeLayout rl_batal = findViewById(R.id.rl_batal);
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
        bunda_id = bayi.getBundaId();
        tv_usia.setText(settingDate(bayi.getTanggalLahirBayi()));
        tv_nama_bayi.setText(bayi.getNamaBayi());
        tv_nama_bunda.setText(bayi.getBundaId());
        Glide.with(this)
                .load(Constanta.URL_IMG_BAYI + bayi.getFotoBayi())
                .into(foto_bayi);
    }

    private String settingDate(String tanggalLahirBayi){
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

        new SweetAlertDialog(InputDataBayiActivity.this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Imunisasi")
                .setContentText("Bayi Telah Mendapatkan Imunisasi ?")
                .setCancelButton("Batal", new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                        Toast.makeText(InputDataBayiActivity.this, "Batal Imunisasi", Toast.LENGTH_SHORT).show();

                    }
                })
                .setConfirmButton("Selesai", new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                        Toast.makeText(InputDataBayiActivity.this, "Selesai Imunisasi", Toast.LENGTH_SHORT).show();

                    }
                })
                .show();


    }

    private void clickBerat(View view) {

        AlertDialog.Builder dialog = new AlertDialog.Builder(InputDataBayiActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.input_dialog_berat_bayi, null);
        dialog.setView(dialogView);
        dialog.setCancelable(true);
        dialog.setTitle("Berat Bayi");
        dialog.setPositiveButton("Simpan", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditText et_berat = dialogView.findViewById(R.id.et_berat);
                Toast.makeText(InputDataBayiActivity.this, "Simpan Berat : " +
                        et_berat.getText().toString(), Toast.LENGTH_SHORT).show();
                dialog.dismiss();
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

    private void clickTinggi(View view) {

        AlertDialog.Builder dialog = new AlertDialog.Builder(InputDataBayiActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.input_dialog_tinggi_bayi, null);
        dialog.setView(dialogView);
        dialog.setCancelable(true);
        dialog.setTitle("Tinggi Bayi");
        dialog.setPositiveButton("Simpan", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditText et_berat = dialogView.findViewById(R.id.et_berat);
                Toast.makeText(InputDataBayiActivity.this, "Simpan Tinggi : " +
                        et_berat.getText().toString(), Toast.LENGTH_SHORT).show();
                dialog.dismiss();
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