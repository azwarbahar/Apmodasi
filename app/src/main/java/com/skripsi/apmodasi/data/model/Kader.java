package com.skripsi.apmodasi.data.model;

import com.google.gson.annotations.SerializedName;

public class Kader {

    @SerializedName("id_kader")
    private String id_kader;

    @SerializedName("nip_kader")
    private String nip_kader;

    @SerializedName("nama_kader")
    private String nama_kader;

    @SerializedName("jenis_kelamin_kader")
    private String jenis_kelamin_kader;

    @SerializedName("kontak_kader")
    private String kontak_kader;

    @SerializedName("alamat_kader")
    private String alamat_kader;

    @SerializedName("foto_kader")
    private String foto_kader;

    @SerializedName("status_kader")
    private String status_kader;

    @SerializedName("created_at")
    private String created_at;

    @SerializedName("update_at")
    private String update_at;

    public String getId_kader() {
        return id_kader;
    }

    public String getNip_kader() {
        return nip_kader;
    }

    public String getNama_kader() {
        return nama_kader;
    }

    public String getJenis_kelamin_kader() {
        return jenis_kelamin_kader;
    }

    public String getKontak_kader() {
        return kontak_kader;
    }

    public String getAlamat_kader() {
        return alamat_kader;
    }

    public String getFoto_kader() {
        return foto_kader;
    }

    public String getStatus_kader() {
        return status_kader;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdate_at() {
        return update_at;
    }
}
