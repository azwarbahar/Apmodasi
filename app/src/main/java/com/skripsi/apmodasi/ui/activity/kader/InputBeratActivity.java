package com.skripsi.apmodasi.ui.activity.kader;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.skripsi.apmodasi.R;
import com.skripsi.apmodasi.app.network.ApiClient;
import com.skripsi.apmodasi.app.network.ApiInterface;
import com.skripsi.apmodasi.app.response.ResponseImunisasi;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InputBeratActivity extends AppCompatActivity {

    private NumberPicker number_bb_1;
    private NumberPicker number_bb_2;
    private String bb1;
    private String bb2;
    String bayi_id;
    String user_id;

    private RelativeLayout rl_batal;
    private RelativeLayout rl_simpan;

    private EditText et_catatan_berat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_berat);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            bayi_id = extras.getString("bayi");
            user_id = extras.getString("kader");
        }
        rl_batal = findViewById(R.id.rl_batal);
        rl_simpan = findViewById(R.id.rl_simpan);
        et_catatan_berat = findViewById(R.id.et_catatan_berat);

        number_bb_1 = findViewById(R.id.number_bb_1);
        number_bb_1.setMinValue(0);
        number_bb_1.setMaxValue(20);
        number_bb_1.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                bb1 = String.valueOf(newVal);
            }
        });
        number_bb_2 = findViewById(R.id.number_bb_2);
        number_bb_2.setMinValue(0);
        number_bb_2.setMaxValue(99);
        number_bb_2.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                bb2 = String.valueOf(newVal);
            }
        });

        rl_batal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        rl_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SweetAlertDialog pDialog = new SweetAlertDialog(InputBeratActivity.this, SweetAlertDialog.PROGRESS_TYPE);
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                pDialog.setTitleText("Loading");
                pDialog.setCancelable(false);
                pDialog.show();

                String bayi_id_send = bayi_id;
                String berat_send = bb1 + "." + bb2;
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
                                new SweetAlertDialog(InputBeratActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                        .setTitleText("Success..")
                                        .setContentText("Berhasil Mengisi Berat Bayi..")
                                        .setConfirmButton("Ok", new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                sweetAlertDialog.dismiss();
                                                finish();
                                            }
                                        })
                                        .show();
                            } else {
                                new SweetAlertDialog(InputBeratActivity.this, SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("Gagal..")
                                        .setContentText("Terjadi kesalahan!, Kode : " + kode)
                                        .show();
                            }
                        } else {
                            new SweetAlertDialog(InputBeratActivity.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Gagal..")
                                    .setContentText("Terjadi kesalahan!")
                                    .show();
                        }

                    }

                    @Override
                    public void onFailure(Call<ResponseImunisasi> call, Throwable t) {
                        pDialog.dismiss();
                        new SweetAlertDialog(InputBeratActivity.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Gagal..")
                                .setContentText("Terjadi kesalahan Sistem!")
                                .show();

                    }
                });
            }
        });
    }

}