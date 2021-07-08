package com.skripsi.apmodasi.data.model;

import com.google.gson.annotations.SerializedName;

public class TinggiBadan {

    @SerializedName("id_tb")
    private String id_tb;

    @SerializedName("bayi_id")
    private String bayi_id;

    @SerializedName("nilai_tb")
    private String nilai_tb;

    @SerializedName("catatan_tb")
    private String catatan_tb;

    @SerializedName("kader_id")
    private String kader_id;

    @SerializedName("tanggal_tb")
    private String tanggal_tb;

    @SerializedName("created_at")
    private String created_at;

    @SerializedName("update_at")
    private String update_at;

    public String getId_tb() {
        return id_tb;
    }

    public String getBayi_id() {
        return bayi_id;
    }

    public String getNilai_tb() {
        return nilai_tb;
    }

    public String getCatatan_tb() {
        return catatan_tb;
    }

    public String getKader_id() {
        return kader_id;
    }

    public String getTanggal_tb() {
        return tanggal_tb;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdate_at() {
        return update_at;
    }
}
