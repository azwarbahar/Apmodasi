package com.skripsi.apmodasi.ui.activity.kader;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.skripsi.apmodasi.R;
import com.skripsi.apmodasi.ui.activity.ImageViewActivity;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class InputDataBayiActivity extends AppCompatActivity {


    private View dialogView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_data_bayi);

        RelativeLayout rl_imunisasi_bayi = findViewById(R.id.rl_imunisasi_bayi);
        RelativeLayout rl_tinggi_bayi = findViewById(R.id.rl_tinggi_bayi);
        RelativeLayout rl_berat_bayi = findViewById(R.id.rl_berat_bayi);
        RelativeLayout rl_batal = findViewById(R.id.rl_batal);
        CardView cv_foto_bayi = findViewById(R.id.cv_foto_bayi);
        TextView tv_nama_bayi = findViewById(R.id.tv_nama_bayi);
        String data = getIntent().getStringExtra("DATA");
//        if (data.equals("")){
//            tv_nama_bayi.setText("Nama Bayi");
//        } else {
//            tv_nama_bayi.setText(data);
//        }

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