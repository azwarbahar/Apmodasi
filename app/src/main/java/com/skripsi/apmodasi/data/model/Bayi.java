package com.skripsi.apmodasi.data.model;

import com.google.gson.annotations.SerializedName;

public class Bayi {

    @SerializedName("nomor_bayi")
    private String nomorBayi;

    @SerializedName("gambar_qr_bayi")
    private String gambarQrBayi;

    @SerializedName("bunda_id")
    private String bundaId;

    @SerializedName("tanggal_lahir_bayi")
    private String tanggalLahirBayi;

    @SerializedName("jenis_kelamin_bayi")
    private String jenisKelaminBayi;

    @SerializedName("status_bayi")
    private String statusBayi;

    @SerializedName("foto_bayi")
    private String fotoBayi;

    @SerializedName("id_bayi")
    private String idBayi;

    @SerializedName("nik_bunda")
    private String nik_bunda;

    @SerializedName("nama_bayi")
    private String namaBayi;

    @SerializedName("update_at")
    private String updateAt;

    @SerializedName("created_at")
    private String createdAt;

    public String getNik_bunda() {
        return nik_bunda;
    }

    public String getNomorBayi() {
        return nomorBayi;
    }

    public String getGambarQrBayi() {
        return gambarQrBayi;
    }

    public String getBundaId() {
        return bundaId;
    }

    public String getTanggalLahirBayi() {
        return tanggalLahirBayi;
    }

    public String getJenisKelaminBayi() {
        return jenisKelaminBayi;
    }

    public String getStatusBayi() {
        return statusBayi;
    }

    public String getFotoBayi() {
        return fotoBayi;
    }

    public String getIdBayi() {
        return idBayi;
    }

    public String getNamaBayi() {
        return namaBayi;
    }

    public String getUpdateAt() {
        return updateAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }
}