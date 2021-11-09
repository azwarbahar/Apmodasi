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

    @GET("login/getAuth.php")
    Call<ResponseAuth> getAuth(@Query("user_id") String user_id,
                               @Query("role") String role);

    @GET("login/auth.php")
    Call<ResponseAuth> authLogin(@Query("username") String username,
                                 @Query("password") String password);

    @FormUrlEncoded
    @POST("login/registrasi_akun.php")
    Call<ResponseAuth> registrasi_akun(@Field("nik_bunda") String nik_bunda,
                                       @Field("nama_bunda") String nama_bunda,
                                       @Field("kontak_bunda") String kontak_bunda,
                                       @Field("alamat_bunda") String alamat_bunda,
                                       @Field("kelurahan_bunda") String kelurahan_bunda,
                                       @Field("password_bunda") String password_bunda);


    // KADER
    @GET("kader/getRiwayatKader.php")
    Call<ResponseKader> getRiwayatKader(@Query("kader_id") String kader_id);

    @GET("kader/getKaderId.php")
    Call<ResponseKader> getKaderId(@Query("id_kader") String id_kader);

    @FormUrlEncoded
    @POST("kader/editFotoKader.php")
    Call<ResponseKader> editFotoKader(@Field("id_kader") String id_kader,
                                      @Field("foto_kader") String foto_kader);

    @FormUrlEncoded
    @POST("kader/editKader.php")
    Call<ResponseKader> editKader(@Field("id_kader") String id_kader,
                                  @Field("nama_kader") String nama_kader,
                                  @Field("kontak_kader") String kontak_kader,
                                  @Field("alamat_kader") String alamat_kader);

    @GET("kader/editPasswordKader.php")
    Call<ResponseKader> editPasswordKader(@Query("id_kader") String id_kader,
                                          @Query("password_lama") String password_lama,
                                          @Query("password_baru") String password_baru);


    // BUNDA
    @GET("bunda/getBundaId.php")
    Call<ResponseBunda> getBundaId(@Query("id_bunda") String id_bunda);

    @FormUrlEncoded
    @POST("bunda/editFotoBunda.php")
    Call<ResponseBunda> editFotoBunda(@Field("id_bunda") String id_bunda,
                                      @Field("foto_bunda") String foto_bunda);

    @GET("bunda/getAllBunda.php")
    Call<ResponseBunda> getAllBunda();

    @FormUrlEncoded
    @POST("bunda/editBunda.php")
    Call<ResponseBunda> editBunda(@Field("id_bunda") String id_bunda,
                                  @Field("nama_bunda") String nama_bunda,
                                  @Field("kontak_bunda") String kontak_bunda,
                                  @Field("alamat_bunda") String alamat_bunda);

    @GET("bunda/editPasswordBunda.php")
    Call<ResponseBunda> editPasswordBunda(@Query("id_bunda") String id_bunda,
                                          @Query("password_lama") String password_lama,
                                          @Query("password_baru") String password_baru);


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
    Call<ResponseBayi> getBayiBunda(@Query("bunda_id") String bunda_id,
                                    @Query("status_bayi") String status_bayi);

    @GET("bayi/getAllBayi.php")
    Call<ResponseBayi> getAllBayi();

    @GET("bayi/getBayiId.php")
    Call<ResponseBayi> getBayiId(@Query("id_bayi") String id_bayi);

    @GET("bayi/getRiwayatBayi.php")
    Call<ResponseKader> getRiwayatBayi(@Query("bayi_id") String bayi_id);

    @GET("bayi/getBayiNomor.php")
    Call<ResponseBayi> getBayiNomor(@Query("nomor_bayi") String nomor_bayi);

    @FormUrlEncoded
    @POST("bayi/editBayi.php")
    Call<ResponseBayi> editBayi(@Field("id_bayi") String id_bayi,
                                @Field("nama_bayi") String nama_bayi,
                                @Field("tanggal_lahir_bayi") String tanggal_lahir_bayi,
                                @Field("jenis_kelamin_bayi") String jenis_kelamin_bayi);

    @FormUrlEncoded
    @POST("bayi/editFotoBayi.php")
    Call<ResponseBayi> editFotoBayi(@Field("id_bayi") String id_bayi,
                                    @Field("foto_bayi") String foto_bayi);


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
