package com.skripsi.apmodasi.ui.activity.bunda;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.skripsi.apmodasi.R;
import com.skripsi.apmodasi.app.network.ApiClient;
import com.skripsi.apmodasi.app.network.ApiInterface;
import com.skripsi.apmodasi.app.response.ResponseBayi;
import com.skripsi.apmodasi.data.model.Bayi;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditBayiActivity extends AppCompatActivity {

    private String id_bayi_intent;
    private EditText et_nama_lengkap;
    private RadioGroup radio_jenis_kelamin;
    private RadioButton radio_laki;
    private RadioButton radio_perempuan;
    private TextView et_tanggal_lahir;
    private RelativeLayout rl_batal;
    private RelativeLayout rl_simpan;
    private String tanggal_lahir_send = "";
    private String tanggal_lahir_default;
    private String tahun;
    private String bulan;
    private String hari;

    private Bayi bayi;
    private DatePickerDialog datePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_bayi);

        id_bayi_intent = getIntent().getStringExtra("id_bayi");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        et_nama_lengkap = findViewById(R.id.et_nama_lengkap);
        radio_jenis_kelamin = findViewById(R.id.radio_jenis_kelamin);
        radio_laki = findViewById(R.id.radio_laki);
        radio_perempuan = findViewById(R.id.radio_perempuan);
        et_tanggal_lahir = findViewById(R.id.et_tanggal_lahir);
        rl_batal = findViewById(R.id.rl_batal);
        rl_simpan = findViewById(R.id.rl_simpan);

        et_tanggal_lahir.setOnClickListener(this::clickTanggalLahir);

        rl_batal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        rl_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tanggal_lahir;
                String nama_lenglap = et_nama_lengkap.getText().toString();
                if (tanggal_lahir_send.equals("")) {
                    tanggal_lahir = tanggal_lahir_default;
                } else {
                    tanggal_lahir = tanggal_lahir_send;
                }
                String jenis_kelamin;
                if (radio_laki.isChecked()) {
                    jenis_kelamin = radio_laki.getText().toString();
                } else {
                    jenis_kelamin = radio_perempuan.getText().toString();
                }

                if (nama_lenglap.equals("")) {
                    new SweetAlertDialog(EditBayiActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Opss..")
                            .setContentText("Nama lengkap tidak boleh kosong")
                            .show();
                } else if (tanggal_lahir.equals("")) {
                    new SweetAlertDialog(EditBayiActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Opss..")
                            .setContentText("Tanggal lahir tidak boleh kosong")
                            .show();
                } else if (jenis_kelamin.equals("")) {
                    new SweetAlertDialog(EditBayiActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Opss..")
                            .setContentText("Jenis kelamin tidak boleh kosong")
                            .show();
                } else {
                    sendDataBayi(id_bayi_intent, nama_lenglap, tanggal_lahir, jenis_kelamin);
                }
            }
        });

        loadDataBayi(id_bayi_intent);
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

    private void sendDataBayi(String id_bayi_intent, String nama_lenglap, String tanggal_lahir, String jenis_kelamin) {

        SweetAlertDialog pDialog = new SweetAlertDialog(EditBayiActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading..");
        pDialog.setCancelable(false);
        pDialog.show();

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBayi> responseBayiCall = apiInterface.editBayi(id_bayi_intent, nama_lenglap,
                tanggal_lahir, jenis_kelamin);
        responseBayiCall.enqueue(new Callback<ResponseBayi>() {
            @Override
            public void onResponse(Call<ResponseBayi> call, Response<ResponseBayi> response) {
                pDialog.dismiss();
                if (response.isSuccessful()) {
                    String kode = response.body().getKode();
                    if (kode.equals("1")) {
                        new SweetAlertDialog(EditBayiActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("Success..")
                                .setContentText(response.body().getPesan())
                                .setConfirmButton("Ok", new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        sweetAlertDialog.dismiss();
                                        finish();

                                    }
                                })
                                .show();
                    } else {
                        new SweetAlertDialog(EditBayiActivity.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Mohon Maaf...")
                                .setContentText(response.body().getPesan())
                                .show();
                    }
                } else {
                    new SweetAlertDialog(EditBayiActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Mohon Maaf...")
                            .setContentText("Terjadi Kesalahan!")
                            .show();
                }

            }

            @Override
            public void onFailure(Call<ResponseBayi> call, Throwable t) {
                pDialog.dismiss();
                new SweetAlertDialog(EditBayiActivity.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Mohon Maaf...")
                        .setContentText("Terjadi Kesalahan System!")
                        .show();
            }
        });

    }

    private void loadDataBayi(String id_bayi_intent) {

        SweetAlertDialog pDialog = new SweetAlertDialog(EditBayiActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBayi> responseBayiCall = apiInterface.getBayiId(id_bayi_intent);
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
                        Toast.makeText(EditBayiActivity.this, "Load Data Bayi Tidak Berhasil!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(EditBayiActivity.this, "Load Data Bayi Tidak Berhasil!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBayi> call, Throwable t) {
                pDialog.dismiss();
                Toast.makeText(EditBayiActivity.this, "Load Data Bayi Tidak Berhasil!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void initDataBayi(Bayi bayi) {
        et_nama_lengkap.setText(bayi.getNamaBayi());
        tanggal_lahir_default = bayi.getTanggalLahirBayi();
        et_tanggal_lahir.setText(parseDateToddMMyyyy(bayi.getTanggalLahirBayi()));
    }

    private String parseDateToddMMyyyy(String time) {
        String inputPattern = "yyyy-MM-dd";
        String outputPattern = "dd MMMM yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}