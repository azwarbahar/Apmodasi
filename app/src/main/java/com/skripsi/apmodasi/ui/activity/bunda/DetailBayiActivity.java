package com.skripsi.apmodasi.ui.activity.bunda;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import cn.pedant.SweetAlert.SweetAlertDialog;
import lecho.lib.hellocharts.listener.LineChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
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
import com.skripsi.apmodasi.data.model.Imunisasi;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailBayiActivity extends AppCompatActivity {

    private Bayi bayi;
    private ArrayList<Imunisasi> imunisasis;
    private String qr_code_bayi;

    private ImageView img_edit;
    private TextView tv_nama_bayi;
    private TextView tv_usia;
    private TextView tv_jenis_kelamin;
    private TextView tv_tanggal_lahir;
    private ImageView img_foto;


    private RelativeLayout rl_qr_code;
    // sliding pannel
    private SlidingUpPanelLayout sliding_layout;
    private ImageView img_qr;
    private TextView tv_nomor_bayi_pannel;
    private TextView tv_nama_bayi_pannel;


    private LineChartView chart_tb;
    private LineChartView chart_bb;

    private String id_bayi_intent;

    String[] axisData = {"Jan", "Feb", "Mar", "Apr", "Mey", "Jun", "Jul", "Agu", "Sep", "Okt", "Nov", "Des"};
    int[] yAxisData = {50, 30, 60, 50, 70, 75, 80, 78, 85, 90, 89, 91};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_bayi);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        id_bayi_intent = getIntent().getStringExtra("id_bayi");

        // sliding pannel
        sliding_layout = findViewById(R.id.sliding_layout);
        img_qr = findViewById(R.id.img_qr);
        tv_nomor_bayi_pannel = findViewById(R.id.tv_nomor_bayi_pannel);
        tv_nama_bayi_pannel = findViewById(R.id.tv_nama_bayi_pannel);


        tv_nama_bayi = findViewById(R.id.tv_nama_bayi);
        tv_usia = findViewById(R.id.tv_usia);
        tv_jenis_kelamin = findViewById(R.id.tv_jenis_kelamin);
        tv_tanggal_lahir = findViewById(R.id.tv_tanggal_lahir);
        img_foto = findViewById(R.id.img_foto);

        img_edit = findViewById(R.id.img_edit);
        img_edit.setOnClickListener(this::clickEdit);


        chart_tb = (LineChartView) findViewById(R.id.chart_tb);
        chart_bb = (LineChartView) findViewById(R.id.chart_bb);



        List yAxisValues = new ArrayList();
        List axisValues = new ArrayList();


        Line line = new Line(yAxisValues).setColor(Color.parseColor("#9C27B0"));

        for (int i = 0; i < axisData.length; i++) {
            axisValues.add(i, new AxisValue(i).setLabel(axisData[i]));
        }

        for (int i = 0; i < yAxisData.length; i++) {
            yAxisValues.add(new PointValue(i, yAxisData[i]));
        }

        List lines = new ArrayList();
        lines.add(line);

        LineChartData data = new LineChartData();
        data.setLines(lines);

        Axis axis = new Axis();
        axis.setValues(axisValues);
        axis.setTextSize(16);
        axis.setTextColor(Color.parseColor("#03A9F4"));
        data.setAxisXBottom(axis);

        Axis yAxis = new Axis();
        yAxis.setTextColor(Color.parseColor("#03A9F4"));
        yAxis.setTextSize(16);
        data.setAxisYLeft(yAxis);

        chart_tb.setLineChartData(data);
        Viewport viewport = new Viewport(chart_tb.getMaximumViewport());
        viewport.top = 110;
        chart_tb.setMaximumViewport(viewport);
        chart_tb.setCurrentViewport(viewport);
        chart_bb.setOnValueTouchListener(new LineChartOnValueSelectListener() {
            @Override
            public void onValueSelected(int i, int i1, PointValue pointValue) {
                Toast.makeText(DetailBayiActivity.this, "Nilai : " + pointValue.getY(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onValueDeselected() {

            }
        });

        chart_bb.setLineChartData(data);
        Viewport viewport_bb = new Viewport(chart_bb.getMaximumViewport());
        viewport_bb.top = 110;
        chart_bb.setMaximumViewport(viewport_bb);
        chart_bb.setCurrentViewport(viewport_bb);

        rl_qr_code = findViewById(R.id.rl_qr_code);
        rl_qr_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPanel();
            }
        });

        loadDataBayi(id_bayi_intent);

    }

    private void loadImunisasiBayi(String bayi_id){

        SweetAlertDialog pDialog = new SweetAlertDialog(DetailBayiActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseImunisasi> responseImunisasiCall = apiInterface.getImunisasiBayi(bayi_id);
        responseImunisasiCall.enqueue(new Callback<ResponseImunisasi>() {
            @Override
            public void onResponse(Call<ResponseImunisasi> call, Response<ResponseImunisasi> response) {
                pDialog.dismiss();
                if (response.isSuccessful()){
                    String kode = response.body().getKode();
                    if (kode.equals("1")){
                        imunisasis = (ArrayList<Imunisasi>) response.body().getImunisasi_bayi();
                        initImunisasi(imunisasis);
                    } else {

                    }
                }

            }

            @Override
            public void onFailure(Call<ResponseImunisasi> call, Throwable t) {
                pDialog.dismiss();

            }
        });

    }

    private void initImunisasi(ArrayList<Imunisasi> imunisasis) {

    }

    private void loadDataBayi(String id_bayi_intent) {

        SweetAlertDialog pDialog = new SweetAlertDialog(DetailBayiActivity.this, SweetAlertDialog.PROGRESS_TYPE);
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
                if (response.isSuccessful()){
                    String kode = response.body().getKode();
                    if (kode.equals("1")){
                        bayi = response.body().getResult_bayi();
                        initDataBayi(bayi);
                    } else {
                        Toast.makeText(DetailBayiActivity.this, "Load Data Bayi Tidak Berhasil!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(DetailBayiActivity.this, "Load Data Bayi Tidak Berhasil!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBayi> call, Throwable t) {
                pDialog.dismiss();
                Toast.makeText(DetailBayiActivity.this, "Load Data Bayi Tidak Berhasil!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private String parseDateToddMMyyyy(String time) {
        String inputPattern ="yyyy-MM-dd";
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

    private void initDataBayi(Bayi bayi) {
//        qr_code_bayi = bayi.getGambarQrBayi();
        tv_nomor_bayi_pannel.setText(bayi.getNomorBayi());
        tv_usia.setText(settingDate(bayi.getTanggalLahirBayi()));

        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {

            BitMatrix bitMatrix = multiFormatWriter.encode(bayi.getNomorBayi(),
                    BarcodeFormat.QR_CODE,500,500);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            img_qr.setImageBitmap(bitmap);

        } catch (Exception e){
            e.printStackTrace();
        }

        tv_nama_bayi_pannel.setText(bayi.getNamaBayi());
        tv_nama_bayi.setText(bayi.getNamaBayi());
        tv_jenis_kelamin.setText(bayi.getJenisKelaminBayi());
        tv_tanggal_lahir.setText(parseDateToddMMyyyy(bayi.getTanggalLahirBayi()));
        String bayi_foto = bayi.getFotoBayi();
        if (bayi_foto.equals("-")){
            Glide.with(this)
                    .load(R.drawable.img_baby)
                    .into(img_foto);
        } else {
            Glide.with(this)
                    .load(Constanta.URL_IMG_BAYI + bayi_foto)
                    .into(img_foto);
        }

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

    private void clickEdit(View view) {

        startActivity(new Intent(DetailBayiActivity.this, EditBayiActivity.class));

    }

    private void showPanel() {
        if (sliding_layout != null &&
                (sliding_layout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED || sliding_layout.getPanelState() == SlidingUpPanelLayout.PanelState.ANCHORED)) {
            sliding_layout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        } else {
            sliding_layout.setPanelState(SlidingUpPanelLayout.PanelState.ANCHORED);
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}