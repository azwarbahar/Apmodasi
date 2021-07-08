package com.skripsi.apmodasi.app.response;

import com.google.gson.annotations.SerializedName;
import com.skripsi.apmodasi.data.model.Imunisasi;

import java.util.List;

public class ResponseImunisasi {

    @SerializedName("kode")
    private String kode;

    @SerializedName("pesan")
    private String pesan;

    @SerializedName("imunisasi_bayi")
    private List<Imunisasi> imunisasi_bayi;

    public String getKode() {
        return kode;
    }

    public String getPesan() {
        return pesan;
    }

    public List<Imunisasi> getImunisasi_bayi() {
        return imunisasi_bayi;
    }
}
