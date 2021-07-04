package com.skripsi.apmodasi.app.response;

import com.google.gson.annotations.SerializedName;
import com.skripsi.apmodasi.data.model.Bunda;

public class ResponseBunda{

	@SerializedName("result_bunda")
	private Bunda bunda;

	@SerializedName("kode")
	private String kode;

	@SerializedName("pesan")
	private String pesan;

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