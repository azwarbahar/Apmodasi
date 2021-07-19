package com.skripsi.apmodasi.app.response;

import com.google.gson.annotations.SerializedName;
import com.skripsi.apmodasi.data.model.Bunda;

import java.util.List;

public class ResponseBunda{

	@SerializedName("kode")
	private String kode;

	@SerializedName("pesan")
	private String pesan;

	@SerializedName("result_bunda")
	private Bunda bunda;

	@SerializedName("bunda_data")
	private List<Bunda> bunda_data;

	public List<Bunda> getBunda_data() {
		return bunda_data;
	}

	public Bunda getBunda(){
		return bunda;
	}

	public String getKode(){
		return kode;
	}

	public String getPesan() {
		return pesan;
	}
}