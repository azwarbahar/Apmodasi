package com.skripsi.apmodasi.data.model;

import com.google.gson.annotations.SerializedName;

public class BeratBadan {

    @SerializedName("id_bb")
    private String id_bb;

    @SerializedName("bayi_id")
    private String bayi_id;

    @SerializedName("nilai_bb")
    private String nilai_bb;

    @SerializedName("catatan_bb")
    private String catatan_bb;

    @SerializedName("kader_id")
    private String kader_id;

    @SerializedName("tanggal_bb")
    private String tanggal_bb;

    @SerializedName("created_at")
    private String created_at;

    @SerializedName("update_at")
    private String update_at;

    public String getId_bb() {
        return id_bb;
    }

    public String getBayi_id() {
        return bayi_id;
    }

    public String getNilai_bb() {
        return nilai_bb;
    }

    public String getCatatan_bb() {
        return catatan_bb;
    }

    public String getKader_id() {
        return kader_id;
    }

    public String getTanggal_bb() {
        return tanggal_bb;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdate_at() {
        return update_at;
    }
}
