package com.skripsi.apmodasi.data.model;

import com.google.gson.annotations.SerializedName;

public class Imunisasi {

    @SerializedName("id_imunisasi")
    private String id_imunisasi;

    @SerializedName("bayi_id")
    private String bayi_id;

    @SerializedName("no_imunisasi")
    private String no_imunisasi;

    @SerializedName("nama_imunisasi")
    private String nama_imunisasi;

    @SerializedName("usia_bayi_imunisasi")
    private String usia_bayi_imunisasi;

    @SerializedName("kader_id")
    private String kader_id;

    @SerializedName("status_imunisasi")
    private String status_imunisasi;

    @SerializedName("catatan_imunisasi")
    private String catatan_imunisasi;

    @SerializedName("keterangan_imunisasi")
    private String keterangan_imunisasi;

    @SerializedName("interval_imunisasi")
    private String interval_imunisasi;

    @SerializedName("tanggal_imunisasi")
    private String tanggal_imunisasi;

    @SerializedName("created_at")
    private String created_at;

    @SerializedName("update_at")
    private String update_at;

    public String getId_imunisasi() {
        return id_imunisasi;
    }

    public String getBayi_id() {
        return bayi_id;
    }

    public String getNo_imunisasi() {
        return no_imunisasi;
    }

    public String getNama_imunisasi() {
        return nama_imunisasi;
    }

    public String getUsia_bayi_imunisasi() {
        return usia_bayi_imunisasi;
    }

    public String getKader_id() {
        return kader_id;
    }

    public String getStatus_imunisasi() {
        return status_imunisasi;
    }

    public String getCatatan_imunisasi() {
        return catatan_imunisasi;
    }

    public String getKeterangan_imunisasi() {
        return keterangan_imunisasi;
    }

    public String getInterval_imunisasi() {
        return interval_imunisasi;
    }

    public String getTanggal_imunisasi() {
        return tanggal_imunisasi;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdate_at() {
        return update_at;
    }
}
