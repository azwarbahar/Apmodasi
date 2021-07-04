package com.skripsi.apmodasi.app.network;

import com.skripsi.apmodasi.app.response.ResponseAuth;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("login/auth.php")
    Call<ResponseAuth> authLogin(@Query("username") String username,
                                      @Query("password") String password);
}
