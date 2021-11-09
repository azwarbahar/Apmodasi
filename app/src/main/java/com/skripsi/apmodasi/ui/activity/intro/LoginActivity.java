package com.skripsi.apmodasi.ui.activity.intro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.skripsi.apmodasi.R;
import com.skripsi.apmodasi.app.network.ApiClient;
import com.skripsi.apmodasi.app.network.ApiInterface;
import com.skripsi.apmodasi.app.response.ResponseAuth;
import com.skripsi.apmodasi.app.util.Constanta;
import com.skripsi.apmodasi.data.model.AuthResult;
import com.skripsi.apmodasi.ui.activity.bunda.MenuActivity;
import com.skripsi.apmodasi.ui.activity.kader.MenuKaderActivity;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private RelativeLayout rl_login;

    private SharedPreferences sharedpreferences;
    private SharedPreferences.Editor editor;

    private EditText et_username;
    private EditText et_password;

    private AuthResult authResult;

    private TextView tv_regist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharedpreferences = getApplicationContext().getSharedPreferences(Constanta.MY_SHARED_PREFERENCES,
                MODE_PRIVATE);

        String role = sharedpreferences.getString(Constanta.SESSION_ROLE, "");
        if (!role.isEmpty()) {
            switch (role) {
                case "Bunda":
                    startActivity(new Intent(this, MenuActivity.class));
                    finish();
                    break;
                case "Kader":
                    startActivity(new Intent(this, MenuKaderActivity.class));
                    finish();
                    break;
            }
        }

        tv_regist = findViewById(R.id.tv_regist);
        tv_regist.setOnClickListener(this::clickRegist);

        et_password = findViewById(R.id.et_password);
        et_username = findViewById(R.id.et_username);
        rl_login = findViewById(R.id.rl_login);
        rl_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_username.setError(null);
                et_password.setError(null);

                String username = et_username.getText().toString();
                String password = et_password.getText().toString();

                if (username.equals("") || username.isEmpty()) {
                    et_username.setError("Lengkapi");
                } else if (password.equals("") || password.isEmpty()) {
                    et_password.setError("Lengkapi");
                } else {
                    clickLogin(username, password);
                }
            }
        });

    }

    private void clickRegist(View view) {
        startActivity(new Intent(LoginActivity.this, RegistrasiActivity.class));
    }

    private void clickLogin(String username, String password) {

        SweetAlertDialog pDialog = new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseAuth> responseAuthCall = apiInterface.authLogin(username, password);
        responseAuthCall.enqueue(new Callback<ResponseAuth>() {
            @Override
            public void onResponse(Call<ResponseAuth> call, Response<ResponseAuth> response) {
                pDialog.dismiss();
                if (response.isSuccessful()) {
                    String kode = response.body().getKode();
                    String pesan = response.body().getPesan();
                    switch (kode) {
                        case "0":
                            et_username.setError(pesan);
                            new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Opss..")
                                    .setContentText(pesan)
                                    .show();
                            break;
                        case "2":
                            et_password.setError(pesan);
                            new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Opss..")
                                    .setContentText(pesan)
                                    .show();
                            break;
                        case "1":
                            authResult = response.body().getAuthResult();
                            initAuth(authResult);
                            break;
                        default:
                            new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Opss..")
                                    .setContentText("Login Gagal!")
                                    .show();
                            break;
                    }
                } else {
                    new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Opss..")
                            .setContentText("Login Gagal, Kesalahan Sistem")
                            .show();
                    Log.e("Filed" , "Msg : "+ response.message());
                }

            }

            @Override
            public void onFailure(Call<ResponseAuth> call, Throwable t) {
                pDialog.dismiss();
                new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Opss..")
                        .setContentText(t.getMessage())
                        .show();

            }
        });
    }

    private void initAuth(AuthResult authResult) {

        String status = authResult.getStatus();
        String id_auth = authResult.getId_auth();
        String user_id = authResult.getUser_kode();
        String role = authResult.getRole();

        if (status.equals("Active") || status.equals("Inactive")) {
            if (!role.isEmpty()) {
                switch (role) {
                    case "Bunda":
                        startSession(id_auth, user_id, role);
                        startActivity(new Intent(this, MenuActivity.class));
                        finish();
                        break;
                    case "Kader":
                        startSession(id_auth, user_id, role);
                        startActivity(new Intent(this, MenuKaderActivity.class));
                        finish();
                        break;
                }
            }
        } else {
            SweetAlertDialog sweetAlertDialogError = new SweetAlertDialog(LoginActivity.this,
                    SweetAlertDialog.ERROR_TYPE);
            sweetAlertDialogError.setTitleText("Opss..");
            sweetAlertDialogError.setCancelable(false);
            sweetAlertDialogError.setContentText("Username telah di suspend!");
            sweetAlertDialogError.setConfirmButton("OK", new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    sweetAlertDialog.dismiss();
                }
            });
            sweetAlertDialogError.show();
        }

    }

    private void startSession(String id_auth, String user_id, String role) {
        editor = sharedpreferences.edit();
        editor.putString(Constanta.SESSION_ID_AUTH, id_auth);
        editor.putString(Constanta.SESSION_ID_USER, user_id);
        editor.putString(Constanta.SESSION_ROLE, role);
        editor.apply();
    }
}