package com.skripsi.apmodasi.app.response;

import java.util.List;
import com.google.gson.annotations.SerializedName;
import com.skripsi.apmodasi.data.model.Bayi;

public class ResponseBayi{

	@SerializedName("kode")
	private String kode;

	@SerializedName("pesan")
	private String pesan;

	@SerializedName("bayi_data")
	private List<Bayi> bayiData;

	@SerializedName("bayi")
	private List<Bayi> bayi;

	@SerializedName("result_bayi")
	private Bayi result_bayi;

	public List<Bayi> getBayi() {
		return bayi;
	}

	public Bayi getResult_bayi() {
		return result_bayi;
	}

	public String getKode(){
		return kode;
	}

	public List<Bayi> getBayiData(){
		return bayiData;
	}

	public String getPesan() {
		return pesan;
	}
}