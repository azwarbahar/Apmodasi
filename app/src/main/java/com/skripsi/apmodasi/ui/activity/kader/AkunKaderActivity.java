package com.skripsi.apmodasi.ui.activity.kader;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.skripsi.apmodasi.R;
import com.skripsi.apmodasi.app.network.ApiClient;
import com.skripsi.apmodasi.app.network.ApiInterface;
import com.skripsi.apmodasi.app.response.ResponseBunda;
import com.skripsi.apmodasi.app.response.ResponseKader;
import com.skripsi.apmodasi.app.util.Constanta;
import com.skripsi.apmodasi.data.model.Kader;
import com.skripsi.apmodasi.ui.activity.ImagePickerActivity;
import com.skripsi.apmodasi.ui.activity.ImageViewActivity;
import com.skripsi.apmodasi.ui.activity.bunda.AkunActivity;
import com.skripsi.apmodasi.ui.activity.intro.LoginActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AkunKaderActivity extends AppCompatActivity {

    private SharedPreferences mPreferences;
    private SharedPreferences.Editor editor;
    private String user_id;

    private Kader kader;

    private CircularImageView img_profile;
    private TextView tv_nip;
    private TextView tv_nama;
    private TextView tv_telpon;
    private TextView tv_alamat;
    private TextView tv_edit_profile;

    private RelativeLayout rl_edit_password;
    private RelativeLayout rl_logout;

    private String foto;
    private Bitmap bitmap_foto;

    private static final String TAG = AkunKaderActivity.class.getSimpleName();
    public static final int REQUEST_IMAGE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_akun_kader);

        mPreferences = getSharedPreferences(Constanta.MY_SHARED_PREFERENCES, MODE_PRIVATE);
        user_id = mPreferences.getString(Constanta.SESSION_ID_USER, "");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        img_profile = findViewById(R.id.img_profile);
        tv_nip = findViewById(R.id.tv_nip);
        tv_nama = findViewById(R.id.tv_nama);
        tv_telpon = findViewById(R.id.tv_telpon);
        tv_alamat = findViewById(R.id.tv_alamat);

        img_profile.setOnClickListener(this::clickFoto);

        tv_edit_profile = findViewById(R.id.tv_edit_profile);
        tv_edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AkunKaderActivity.this, EditProfileKaderActivity.class));
            }
        });

        rl_edit_password = findViewById(R.id.rl_edit_password);
        rl_edit_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AkunKaderActivity.this, EditPasswordKaderActivity.class));
            }
        });

        rl_logout = findViewById(R.id.rl_logout);
        rl_logout.setOnClickListener(this::clickLogout);

        loadDataAkun(user_id);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                Uri uri = data.getParcelableExtra("path");
                try {
                    // You can update this bitmap to your server
                    bitmap_foto = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    startUpdatePhoto(bitmap_foto);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private void startUpdatePhoto(Bitmap bitmap_foto) {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap_foto.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        byte[] imgByte = byteArrayOutputStream.toByteArray();
        String foto_send = Base64.encodeToString(imgByte, Base64.DEFAULT);

        SweetAlertDialog pDialog = new SweetAlertDialog(AkunKaderActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();

//        File file = new File(uri.getPath());
//        RequestBody foto = RequestBody.create(MediaType.parse("image/*"), file);
//        MultipartBody.Part foto_send = MultipartBody.Part.createFormData("foto_bunda", file.getName(), foto);
//        RequestBody id_bunda = RequestBody.create(MediaType.parse("text/plain"), user_id);

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseKader> responseBundaCall = apiInterface.editFotoKader(user_id, foto_send);
        responseBundaCall.enqueue(new Callback<ResponseKader>() {
            @Override
            public void onResponse(Call<ResponseKader> call, Response<ResponseKader> response) {
                pDialog.dismiss();
                if (response.isSuccessful()) {
                    String kode = response.body().getKode();
                    if (kode.equals("1")) {
                        SweetAlertDialog success = new SweetAlertDialog(AkunKaderActivity.this, SweetAlertDialog.SUCCESS_TYPE);
                        success.setTitleText("Success..");
                        success.setCancelable(false);
                        success.setContentText("Edit Foto Berhasil");
                        success.setConfirmButton("Ok", new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                                loadDataAkun(user_id);
                            }
                        });
                        success.show();
                    } else {
                        new SweetAlertDialog(AkunKaderActivity.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Uups..")
                                .setContentText(response.body().getPesan())
                                .show();
                    }
                } else {
                    new SweetAlertDialog(AkunKaderActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Uups..")
                            .setContentText("Terjadi kesalahan!")
                            .show();
                }
            }

            @Override
            public void onFailure(Call<ResponseKader> call, Throwable t) {
                pDialog.dismiss();
                new SweetAlertDialog(AkunKaderActivity.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Uups..")
                        .setContentText(t.getLocalizedMessage())
                        .show();


                Log.e("FOTO",  "Locazed : "+t.getLocalizedMessage());
                Log.e("FOTO", "Nessage : "+t.getMessage());
            }
        });

    }

    private void clickFoto(View view) {

        Dexter.withActivity(AkunKaderActivity.this)
                .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            showImagePickerOptions();
                        }

                        if (report.isAnyPermissionPermanentlyDenied()) {
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    private void showImagePickerOptions() {
        ImagePickerActivity.showImagePickerOptions(AkunKaderActivity.this, new ImagePickerActivity.PickerOptionListener() {
            @Override
            public void onTakeCameraSelected() {
                launchCameraIntent();
            }

            @Override
            public void onChooseGallerySelected() {
                launchGalleryIntent();
            }

            @Override
            public void onViewImage() {
                launchViewImage();
            }
        });
    }

    private void launchCameraIntent() {
        Intent intent = new Intent(AkunKaderActivity.this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_IMAGE_CAPTURE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);

        // setting maximum bitmap width and height
        intent.putExtra(ImagePickerActivity.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, true);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_WIDTH, 1000);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_HEIGHT, 1000);

        startActivityForResult(intent, REQUEST_IMAGE);
    }

    private void launchViewImage() {
//        Toast.makeText(getActivity(), "Lihat Gambar!!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(AkunKaderActivity.this, ImageViewActivity.class);
        intent.putExtra("nama_foto", foto);
        intent.putExtra("role_foto", "Kader");
        startActivity(intent);
    }

    private void launchGalleryIntent() {
        Intent intent = new Intent(AkunKaderActivity.this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_GALLERY_IMAGE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);
        startActivityForResult(intent, REQUEST_IMAGE);
    }

    // navigating user to app settings
    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }

    /**
     * Showing Alert Dialog with Settings option
     * Navigates user to app settings
     * NOTE: Keep proper title and message depending on your app
     */
    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AkunKaderActivity.this);
        builder.setTitle(getString(R.string.dialog_permission_title));
        builder.setMessage(getString(R.string.dialog_permission_message));
        builder.setPositiveButton(getString(R.string.go_to_settings), (dialog, which) -> {
            dialog.cancel();
            openSettings();
        });
        builder.setNegativeButton(getString(android.R.string.cancel), (dialog, which) -> dialog.cancel());
        builder.show();

    }

    private void loadDataAkun(String user_id) {

        SweetAlertDialog pDialog = new SweetAlertDialog(AkunKaderActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseKader> responseKaderCall = apiInterface.getKaderId(user_id);
        responseKaderCall.enqueue(new Callback<ResponseKader>() {
            @Override
            public void onResponse(Call<ResponseKader> call, Response<ResponseKader> response) {
                pDialog.dismiss();
                if (response.isSuccessful()) {
                    String kode = response.body().getKode();
                    if (kode.equals("1")) {
                        kader = response.body().getKader();
                        initKader(kader);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseKader> call, Throwable t) {
                pDialog.dismiss();
            }
        });

    }

    private void initKader(Kader kader) {
        tv_nip.setText(kader.getNik_kader());
        tv_nama.setText(kader.getNama_kader());
        tv_telpon.setText(kader.getKontak_kader());
        tv_alamat.setText(kader.getAlamat_kader());
        foto = kader.getFoto_kader();
        Glide.with(this)
                .load(Constanta.URL_IMG_KADER + kader.getFoto_kader())
                .into(img_profile);
    }

    private void clickLogout(View view) {

        new SweetAlertDialog(AkunKaderActivity.this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Logout")
                .setContentText("Ingin Keluar Dari Akun ?")
                .setCancelButton("Tidak", new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                    }
                })
                .setConfirmButton("Ok", new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                        startActivity(new Intent(AkunKaderActivity.this, LoginActivity.class));
                        SharedPreferences.Editor editor = mPreferences.edit();
                        editor.apply();
                        editor.clear();
                        editor.commit();
                        finish();
                    }
                })
                .show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPreferences = getSharedPreferences(Constanta.MY_SHARED_PREFERENCES, MODE_PRIVATE);
        user_id = mPreferences.getString(Constanta.SESSION_ID_USER, "");
        loadDataAkun(user_id);
    }
}