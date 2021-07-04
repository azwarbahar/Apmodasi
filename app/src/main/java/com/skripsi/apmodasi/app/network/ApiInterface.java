package com.skripsi.apmodasi.app.network;

import com.skripsi.apmodasi.app.response.ResponseAuth;
import com.skripsi.apmodasi.app.response.ResponseBayi;
import com.skripsi.apmodasi.app.response.ResponseBunda;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("login/auth.php")
    Call<ResponseAuth> authLogin(@Query("username") String username,
                                 @Query("password") String password);


    // BUNDA
    @GET("bunda/getBundaId.php")
    Call<ResponseBunda> getBundaId(@Query("id_bunda") String id_bunda);


    // BAYI
    @GET("bayi/getBayiBunda.php")
    Call<ResponseBayi> getBayiBunda(@Query("bunda_id") String bunda_id);

    @GET("bayi/getBayiId.php")
    Call<ResponseBayi> getBayiId(@Query("id_bayi") String id_bayi);


}
