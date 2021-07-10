package com.skripsi.apmodasi.app.response;

import com.google.gson.annotations.SerializedName;
import com.skripsi.apmodasi.data.model.BeratBadan;
import com.skripsi.apmodasi.data.model.Imunisasi;
import com.skripsi.apmodasi.data.model.TinggiBadan;

import java.util.List;

public class ResponseImunisasi {

    @SerializedName("kode")
    private String kode;

    @SerializedName("pesan")
    private String pesan;

    @SerializedName("imunisasi_bayi")
    private List<Imunisasi> imunisasi_bayi;

    @SerializedName("berat_bayi")
    private List<BeratBadan> berat_bayi;

    @SerializedName("tinggi_bayi")
    private List<TinggiBadan> tinggi_bayi;

    @SerializedName("tinggi_bayi_data")
    private TinggiBadan tinggi_bayi_data;

    @SerializedName("berat_bayi_data")
    private BeratBadan berat_bayi_data;

    public TinggiBadan getTinggi_bayi_data() {
        return tinggi_bayi_data;
    }

    public BeratBadan getBerat_bayi_data() {
        return berat_bayi_data;
    }

    public List<TinggiBadan> getTinggi_bayi() {
        return tinggi_bayi;
    }

    public List<BeratBadan> getBerat_bayi() {
        return berat_bayi;
    }

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
