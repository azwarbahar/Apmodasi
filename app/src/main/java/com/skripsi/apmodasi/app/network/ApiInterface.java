package com.skripsi.apmodasi.app.network;

import com.skripsi.apmodasi.app.response.ResponseAuth;
import com.skripsi.apmodasi.app.response.ResponseBayi;
import com.skripsi.apmodasi.app.response.ResponseBunda;
import com.skripsi.apmodasi.app.response.ResponseImunisasi;
import com.skripsi.apmodasi.app.response.ResponseKader;

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


    // KADER
    @GET("kader/getRiwayatKader.php")
    Call<ResponseKader> getRiwayatKader(@Query("kader_id") String kader_id);


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

    @GET("bayi/getAllBayi.php")
    Call<ResponseBayi> getAllBayi();

    @GET("bayi/getBayiId.php")
    Call<ResponseBayi> getBayiId(@Query("id_bayi") String id_bayi);

    @GET("bayi/getBayiNomor.php")
    Call<ResponseBayi> getBayiNomor(@Query("nomor_bayi") String nomor_bayi);


    // IMUNISASI
    @GET("imunisasi/getImunisasiBayi.php")
    Call<ResponseImunisasi> getImunisasiBayi(@Query("bayi_id") String bayi_id);

    @GET("imunisasi/getImunisasiStatusBayi.php")
    Call<ResponseImunisasi> getImunisasiStatusBayi(@Query("bayi_id") String bayi_id,
                                                   @Query("status_imunisasi") String status_imunisasi);

    @GET("imunisasi/getBeratBayi.php")
    Call<ResponseImunisasi> getBeratBayi(@Query("bayi_id") String bayi_id);

    @GET("imunisasi/getBeratBayiToday.php")
    Call<ResponseImunisasi> getBeratBayiToday(@Query("bayi_id") String bayi_id,
                                              @Query("tanggal_bb") String tanggal_bb);

    @GET("imunisasi/getTinggiBayi.php")
    Call<ResponseImunisasi> getTinggiBayi(@Query("bayi_id") String bayi_id);

    @GET("imunisasi/getTinggiBayiToday.php")
    Call<ResponseImunisasi> getTinggiBayiToday(@Query("bayi_id") String bayi_id,
                                               @Query("tanggal_tb") String tanggal_tb);

    @GET("imunisasi/getImunisasiBayiToday.php")
    Call<ResponseImunisasi> getImunisasiBayiToday(@Query("bayi_id") String bayi_id,
                                                  @Query("tanggal_imunisasi") String tanggal_imunisasi);

    @FormUrlEncoded
    @POST("imunisasi/addBeratBayi.php")
    Call<ResponseImunisasi> addBeratBayi(@Field("bayi_id") String bayi_id,
                                         @Field("nilai_bb") String nilai_bb,
                                         @Field("catatan_bb") String catatan_bb,
                                         @Field("kader_id") String kader_id,
                                         @Field("tanggal_bb") String tanggal_bb);

    @FormUrlEncoded
    @POST("imunisasi/addTinggiBayi.php")
    Call<ResponseImunisasi> addTinggiBayi(@Field("bayi_id") String bayi_id,
                                          @Field("nilai_tb") String nilai_tb,
                                          @Field("catatan_tb") String catatan_tb,
                                          @Field("kader_id") String kader_id,
                                          @Field("tanggal_tb") String tanggal_tb);

    @FormUrlEncoded
    @POST("imunisasi/editImunisasi.php")
    Call<ResponseImunisasi> editImunisasi(@Field("bayi_id") String bayi_id,
                                          @Field("no_imunisasi") String no_imunisasi,
                                          @Field("usia_bayi_imunisasi") String usia_bayi_imunisasi,
                                          @Field("kader_id") String kader_id,
                                          @Field("status_imunisasi") String status_imunisasi,
                                          @Field("catatan_imunisasi") String catatan_imunisasi,
                                          @Field("tanggal_imunisasi") String tanggal_imunisasi);
}
