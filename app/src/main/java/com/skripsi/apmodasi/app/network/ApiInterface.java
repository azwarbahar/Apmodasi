package com.skripsi.apmodasi.app.network;

import com.skripsi.apmodasi.app.response.ResponseAuth;
import com.skripsi.apmodasi.app.response.ResponseBayi;
import com.skripsi.apmodasi.app.response.ResponseBunda;
import com.skripsi.apmodasi.app.response.ResponseImunisasi;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("login/auth.php")
    Call<ResponseAuth> authLogin(@Query("username") String username,
                                 @Query("password") String password);


    // BUNDA
    @GET("bunda/getBundaId.php")
    Call<ResponseBunda> getBundaId(@Query("id_bunda") String id_bunda);


    // BAYI
    @FormUrlEncoded
    @POST("bayi/addBayi.php")
    Call<ResponseBayi> addBayi(@Field("nomor_bayi") String nomor_bayi,
                               @Field("nama_bayi") String nama_bayi,
                               @Field("tanggal_lahir_bayi") String tanggal_lahir_bayi,
                               @Field("jenis_kelamin_bayi") String jenis_kelamin_bayi,
                               @Field("gambar_qr_bayi") String gambar_qr_bayi,
                               @Field("foto_bayi") String foto_bayi,
                               @Field("status_bayi") String status_bayi,
                               @Field("bunda_id") String bunda_id);

    @GET("bayi/getBayiBunda.php")
    Call<ResponseBayi> getBayiBunda(@Query("bunda_id") String bunda_id);

    @GET("bayi/getBayiId.php")
    Call<ResponseBayi> getBayiId(@Query("id_bayi") String id_bayi);

    @GET("bayi/getBayiNomor.php")
    Call<ResponseBayi> getBayiNomor(@Query("nomor_bayi") String nomor_bayi);


    // IMUNISASI
    @GET("imunisasi/getImunisasiBayi.php")
    Call<ResponseImunisasi> getImunisasiBayi(@Query("bayi_id") String bayi_id);

    @GET("imunisasi/getBeratBayi.php")
    Call<ResponseImunisasi> getBeratBayi(@Query("bayi_id") String bayi_id);

    @GET("imunisasi/getTinggiBayi.php")
    Call<ResponseImunisasi> getTinggiBayi(@Query("bayi_id") String bayi_id);
}
