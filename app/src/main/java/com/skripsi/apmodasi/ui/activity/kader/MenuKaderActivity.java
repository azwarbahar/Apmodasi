package com.skripsi.apmodasi.ui.activity.kader;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.blikoon.qrcodescanner.QrCodeActivity;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;
import com.skripsi.apmodasi.R;
import com.skripsi.apmodasi.ui.activity.bunda.MenuActivity;
import com.skripsi.apmodasi.ui.activity.intro.LoginActivity;
import com.skripsi.apmodasi.ui.activity.intro.SplashScreenActivity;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MenuKaderActivity extends AppCompatActivity {

    private CardView cv_history;
    private CardView cv_bayi;
    private CardView cv_ibu;
    private ImageView img_user;
    private ImageView img_qr_code;

    private SweetAlertDialog pDialog;

    private static final int REQUEST_CODE_QR_SCAN = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_kader);

        img_qr_code = findViewById(R.id.img_qr_code);
        img_user = findViewById(R.id.img_user);
        img_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuKaderActivity.this, AkunKaderActivity.class));
            }
        });

        cv_ibu = findViewById(R.id.cv_ibu);
        cv_bayi = findViewById(R.id.cv_bayi);
        cv_history = findViewById(R.id.cv_history);
        cv_history.setOnClickListener(this::clickHistory);
        cv_bayi.setOnClickListener(this::clickBayi);
        cv_ibu.setOnClickListener(this::clickIbu);

        img_qr_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(MenuKaderActivity.this, InputDataBayiActivity.class));

//                Dexter.withContext(getApplicationContext())
//                        .withPermission(Manifest.permission.CAMERA)
//                        .withListener(new PermissionListener() {
//                            @Override
//                            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
//                                Intent intent = new Intent(MenuKaderActivity.this, QrCodeActivity.class);
//                                startActivityForResult(intent, REQUEST_CODE_QR_SCAN);
//                            }
//
//                            @Override
//                            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
//                                permissionDeniedResponse.getRequestedPermission();
//                            }
//
//                            @Override
//                            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
//                                permissionToken.continuePermissionRequest();
//                            }
//                        }).check();

//                startActivity(new Intent(MenuKaderActivity.this, ScanActivity.class));
            }
        });

    }

    private void clickHistory(View view) {
        startActivity(new Intent(MenuKaderActivity.this, HistoryKaderActivity.class));
    }

    private void clickIbu(View view) {
        startActivity(new Intent(MenuKaderActivity.this, DataIbuActivity.class));
    }

    private void clickBayi(View view) {
        startActivity(new Intent(MenuKaderActivity.this, DataBayiActivity.class));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
//            Toast.makeText(this, "COULD NOT GET A GOOD RESULT.", Toast.LENGTH_SHORT).show();
            if (data == null)
                return;
            //Getting the passed result
            String result = data.getStringExtra("com.blikoon.qrcodescanner.error_decoding_image");
            if (result != null) {

                new SweetAlertDialog(MenuKaderActivity.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Scan Error..")
                        .setContentText("QR code tidak bisa discan")
                        .show();

//                AlertDialog alertDialog = new AlertDialog.Builder(MenuKaderActivity.this).create();
//                alertDialog.setTitle("Scan Error");
//                alertDialog.setMessage("QR Code Tidak Bisa Discan");
//                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.dismiss();
//                            }
//                        });
//                alertDialog.show();
            }
            return;

        }
        if (requestCode == REQUEST_CODE_QR_SCAN) {
            if (data == null) {
                return;
            }

            //Getting the passed result
            String result = data.getStringExtra("com.blikoon.qrcodescanner.got_qr_scan_relult");

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    pDialog = new SweetAlertDialog(MenuKaderActivity.this, SweetAlertDialog.PROGRESS_TYPE);
                    pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                    pDialog.setTitleText("Load Data..");
                    pDialog.setCancelable(false);
                    pDialog.show();

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            pDialog.dismiss();
                            Intent intent = new Intent(MenuKaderActivity.this, InputDataBayiActivity.class);
                            intent.putExtra("DATA", result);
                            startActivity(intent);
                        }
                    }, 2500);

                }
            }, 400);
        }
    }
}