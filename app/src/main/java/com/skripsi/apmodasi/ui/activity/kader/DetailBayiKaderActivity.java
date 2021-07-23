package com.skripsi.apmodasi.ui.activity.kader;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
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
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.listener.LineChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailBayiKaderActivity extends AppCompatActivity {

    private Bayi bayi;
    private ArrayList<Imunisasi> imunisasis;
    private ArrayList<BeratBadan> beratBadans;
    private ArrayList<TinggiBadan> tinggiBadans;
    private String qr_code_bayi;

    private TextView tv_nama_bayi;
    private TextView tv_usia;
    private TextView tv_jenis_kelamin;
    private TextView tv_tanggal_lahir;
    private ImageView img_foto;

    private LineChartView chart_tb;
    private LineChartView chart_bb;

    private String id_bayi_intent;
    private String foto;

    private List yAxisValuesBerat = new ArrayList();
    private List axisValuesBerat = new ArrayList();
    private List yAxisValuesTinggi = new ArrayList();
    private List axisValuesTinggi = new ArrayList();
    private Line lineBerat;
    private Line lineTinggi;
    private ArrayList<Integer> berat_nilai = new ArrayList<Integer>();
    private ArrayList<Integer> tinggi_nilai = new ArrayList<Integer>();

    String[] axisDataBerat = null;
    int[] yAxisDataBerat = null;
    String[] axisDataTinggi = null;
    int[] yAxisDataTinggi = null;

    String[] axisData = {"Jan", "Feb", "Mar", "Apr", "Mey", "Jun", "Jul", "Agu", "Sep", "Okt", "Nov", "Des"};
    int[] yAxisData = {50, 30, 60, 50, 70, 75, 80, 78, 85, 90, 89, 91};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_bayi_kader);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        id_bayi_intent = getIntent().getStringExtra("ID_BAYI");

        tv_nama_bayi = findViewById(R.id.tv_nama_bayi);
        tv_usia = findViewById(R.id.tv_usia);
        tv_jenis_kelamin = findViewById(R.id.tv_jenis_kelamin);
        tv_tanggal_lahir = findViewById(R.id.tv_tanggal_lahir);
        img_foto = findViewById(R.id.img_foto);

        chart_tb = findViewById(R.id.chart_tb);
        chart_bb = findViewById(R.id.chart_bb);

        img_foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(DetailBayiKaderActivity.this, ImageViewActivity.class);
                intent.putExtra("nama_foto", foto);
                intent.putExtra("role_foto", "Bayi");
                startActivity(intent);
            }
        });

        loadDataBayi(id_bayi_intent);
        loadBeratBadanBayi(id_bayi_intent);
        loadTinggiBadanBayi(id_bayi_intent);

    }

    private void loadTinggiBadanBayi(String bayi_id) {
        SweetAlertDialog pDialog = new SweetAlertDialog(DetailBayiKaderActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseImunisasi> responseImunisasiCall = apiInterface.getTinggiBayi(bayi_id);
        responseImunisasiCall.enqueue(new Callback<ResponseImunisasi>() {
            @Override
            public void onResponse(Call<ResponseImunisasi> call, Response<ResponseImunisasi> response) {
                pDialog.dismiss();
                if (response.isSuccessful()) {
                    String kode = response.body().getKode();
                    if (kode.equals("1")) {
                        tinggiBadans = (ArrayList<TinggiBadan>) response.body().getTinggi_bayi();
                        iniTinggiBayi(tinggiBadans);
                    } else {
                        showSnackbar("Memanggil Tinggi bayi gagal!");
                    }
                } else {
                    showSnackbar("Memanggil Tinggi bayi gagal!");
                }
            }

            @Override
            public void onFailure(Call<ResponseImunisasi> call, Throwable t) {
                pDialog.dismiss();
                showSnackbar("Memanggil Tinggi bayi gagal!");
            }
        });
    }

    private void iniTinggiBayi(ArrayList<TinggiBadan> tinggiBadans) {

        axisDataTinggi = new String[tinggiBadans.size()];
        yAxisDataTinggi = new int[tinggiBadans.size()];
        for (int a = 0; a < tinggiBadans.size(); a++) {
            Log.e("DATABERAT", tinggiBadans.get(a).getTanggal_tb());
            axisDataTinggi[a] = parseDateGrafik(tinggiBadans.get(a).getTanggal_tb());
            yAxisDataTinggi[a] = Integer.parseInt(tinggiBadans.get(a).getNilai_tb());
        }

        lineTinggi = new Line(yAxisValuesTinggi).setColor(Color.parseColor("#9C27B0"));

        for (int i = 0; i < axisDataTinggi.length; i++) {
            axisValuesTinggi.add(i, new AxisValue(i).setLabel(axisDataTinggi[i]));
        }

        for (int i = 0; i < yAxisDataTinggi.length; i++) {
            yAxisValuesTinggi.add(new PointValue(i, yAxisDataTinggi[i]));
        }

        List lines = new ArrayList();
        lines.add(lineTinggi);

        LineChartData data = new LineChartData();
        data.setLines(lines);

        Axis axis = new Axis();
        axis.setValues(axisValuesTinggi);
        axis.setTextSize(16);
        axis.setTextColor(Color.parseColor("#03A9F4"));
        data.setAxisXBottom(axis);

        Axis yAxis = new Axis();
        yAxis.setTextColor(Color.parseColor("#03A9F4"));
        yAxis.setTextSize(16);
        data.setAxisYLeft(yAxis);

        chart_tb.setLineChartData(data);

        if (tinggiBadans.size() < 5) {
            Viewport viewport_bb = new Viewport(0, 110, 6, 0);
            viewport_bb.top = 120;
            chart_tb.setMaximumViewport(viewport_bb);
            chart_tb.setCurrentViewport(viewport_bb);
            chart_tb.setZoomType(ZoomType.HORIZONTAL);
        } else {
            Viewport viewport_bb = new Viewport(chart_tb.getMaximumViewport());
            viewport_bb.top = 120;
            chart_tb.setMaximumViewport(viewport_bb);
            chart_tb.setCurrentViewport(viewport_bb);
            chart_tb.setZoomType(ZoomType.HORIZONTAL);
        }
        chart_tb.setOnValueTouchListener(new LineChartOnValueSelectListener() {
            @Override
            public void onValueSelected(int i, int i1, PointValue pointValue) {
                Toast.makeText(DetailBayiKaderActivity.this, "Nilai : " + pointValue.getY(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onValueDeselected() {

            }
        });

    }

    private void loadBeratBadanBayi(String bayi_id) {
        SweetAlertDialog pDialog = new SweetAlertDialog(DetailBayiKaderActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseImunisasi> responseImunisasiCall = apiInterface.getBeratBayi(bayi_id);
        responseImunisasiCall.enqueue(new Callback<ResponseImunisasi>() {
            @Override
            public void onResponse(Call<ResponseImunisasi> call, Response<ResponseImunisasi> response) {
                pDialog.dismiss();
                if (response.isSuccessful()) {
                    String kode = response.body().getKode();
                    if (kode.equals("1")) {
                        beratBadans = (ArrayList<BeratBadan>) response.body().getBerat_bayi();
                        iniBeratBayi(beratBadans);
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

    private void iniBeratBayi(ArrayList<BeratBadan> beratBadans) {

        axisDataBerat = new String[beratBadans.size()];
        yAxisDataBerat = new int[beratBadans.size()];
        for (int a = 0; a < beratBadans.size(); a++) {
            Log.e("DATABERAT", beratBadans.get(a).getTanggal_bb());
            axisDataBerat[a] = parseDateGrafik(beratBadans.get(a).getTanggal_bb());
            yAxisDataBerat[a] = Integer.parseInt(beratBadans.get(a).getNilai_bb());
        }

        lineBerat = new Line(yAxisValuesBerat).setColor(Color.parseColor("#9C27B0"));

        for (int i = 0; i < axisDataBerat.length; i++) {
            axisValuesBerat.add(i, new AxisValue(i).setLabel(axisDataBerat[i]));
        }

        for (int i = 0; i < yAxisDataBerat.length; i++) {
            yAxisValuesBerat.add(new PointValue(i, yAxisDataBerat[i]));
        }

        List lines = new ArrayList();
        lines.add(lineBerat);

        LineChartData data = new LineChartData();
        data.setLines(lines);

        Axis axis = new Axis();
        axis.setValues(axisValuesBerat);
        axis.setTextSize(16);
        axis.setTextColor(Color.parseColor("#03A9F4"));
        data.setAxisXBottom(axis);

        Axis yAxis = new Axis();
        yAxis.setTextColor(Color.parseColor("#03A9F4"));
        yAxis.setTextSize(16);
        data.setAxisYLeft(yAxis);

        chart_bb.setLineChartData(data);

        if (beratBadans.size() < 5) {
            Viewport viewport_bb = new Viewport(0, 110, 6, 0);
            viewport_bb.top = 15;
            chart_bb.setMaximumViewport(viewport_bb);
            chart_bb.setCurrentViewport(viewport_bb);
            chart_bb.setZoomType(ZoomType.HORIZONTAL);
        } else {
            Viewport viewport_bb = new Viewport(chart_bb.getMaximumViewport());
            viewport_bb.top = 15;
            chart_bb.setMaximumViewport(viewport_bb);
            chart_bb.setCurrentViewport(viewport_bb);
            chart_bb.setZoomType(ZoomType.HORIZONTAL);
        }

        chart_bb.setOnValueTouchListener(new LineChartOnValueSelectListener() {
            @Override
            public void onValueSelected(int i, int i1, PointValue pointValue) {
                Toast.makeText(DetailBayiKaderActivity.this, "Nilai : " + pointValue.getY(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onValueDeselected() {

            }
        });

    }

    private void showSnackbar(String message) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT)
                .setActionTextColor(getResources().getColor(android.R.color.holo_red_light))
//                .setAction("Close", new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        v.setVisibility(View.GONE);
//                    }
//                })
                .show();
    }

    private void loadDataBayi(String id_bayi_intent) {

        SweetAlertDialog pDialog = new SweetAlertDialog(DetailBayiKaderActivity.this, SweetAlertDialog.PROGRESS_TYPE);
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
                        Toast.makeText(DetailBayiKaderActivity.this, "Load Data Bayi Tidak Berhasil!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(DetailBayiKaderActivity.this, "Load Data Bayi Tidak Berhasil!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBayi> call, Throwable t) {
                pDialog.dismiss();
                Toast.makeText(DetailBayiKaderActivity.this, "Load Data Bayi Tidak Berhasil!", Toast.LENGTH_SHORT).show();
            }
        });

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

    private String parseDateGrafik(String time) {
        String inputPattern = "yyyy-MM-dd";
        String outputPattern = "dd MMM";
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

    private void initDataBayi(Bayi bayi) {
        tv_usia.setText(settingDate(bayi.getTanggalLahirBayi()));
        tv_nama_bayi.setText(bayi.getNamaBayi());
        tv_jenis_kelamin.setText(bayi.getJenisKelaminBayi());
        tv_tanggal_lahir.setText(parseDateToddMMyyyy(bayi.getTanggalLahirBayi()));
        foto = bayi.getFotoBayi();
        Glide.with(this)
                .load(Constanta.URL_IMG_BAYI + foto)
                .into(img_foto);

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


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}