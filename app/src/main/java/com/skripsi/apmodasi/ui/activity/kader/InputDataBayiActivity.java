package com.skripsi.apmodasi.ui.activity.kader;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.skripsi.apmodasi.R;
import com.skripsi.apmodasi.ui.activity.ImageViewActivity;
import com.skripsi.apmodasi.ui.activity.bunda.MenuActivity;

public class InputDataBayiActivity extends AppCompatActivity {


    private View dialogView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_data_bayi);

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

        rl_berat_bayi.setOnClickListener(this::clickBerat);
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
                Toast.makeText(InputDataBayiActivity.this, "Simpan Berat : "+
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