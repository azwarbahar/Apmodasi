package com.skripsi.apmodasi.data.model;

import com.google.gson.annotations.SerializedName;

public class RiwayatKader {

    @SerializedName("id_riwayat_kader")
    private String id_riwayat_kader;

    @SerializedName("kader_id")
    private String kader_id;

    @SerializedName("bayi_id")
    private String bayi_id;

    @SerializedName("usia_bayi")
    private String usia_bayi;

    @SerializedName("jenis_input")
    private String jenis_input;

    @SerializedName("value_input")
    private String value_input;

    @SerializedName("catatan")
    private String catatan;

    @SerializedName("tanggal_riwayat")
    private String tanggal_riwayat;

    @SerializedName("crated_at")
    private String crated_at;

    @SerializedName("update_at")
    private String update_at;

    public String getId_riwayat_kader() {
        return id_riwayat_kader;
    }

    public String getKader_id() {
        return kader_id;
    }

    public String getBayi_id() {
        return bayi_id;
    }

    public String getUsia_bayi() {
        return usia_bayi;
    }

    public String getJenis_input() {
        return jenis_input;
    }

    public String getValue_input() {
        return value_input;
    }

    public String getCatatan() {
        return catatan;
    }

    public String getTanggal_riwayat() {
        return tanggal_riwayat;
    }

    public String getCrated_at() {
        return crated_at;
    }

    public String getUpdate_at() {
        return update_at;
    }
}
