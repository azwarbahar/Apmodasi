package com.skripsi.apmodasi.app.response;

import com.google.gson.annotations.SerializedName;
import com.skripsi.apmodasi.data.model.Kader;
import com.skripsi.apmodasi.data.model.RiwayatKader;

import java.util.List;

public class ResponseKader {

    @SerializedName("kode")
    private String kode;

    @SerializedName("pesan")
    private String pesan;

    @SerializedName("result_kader")
    private Kader kader;

    @SerializedName("riwayat_kader")
    private List<RiwayatKader> riwayat_kader;

    public String getKode() {
        return kode;
    }

    public String getPesan() {
        return pesan;
    }

    public Kader getKader() {
        return kader;
    }

    public List<RiwayatKader> getRiwayat_kader() {
        return riwayat_kader;
    }
}
