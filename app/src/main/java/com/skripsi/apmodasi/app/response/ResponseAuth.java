package com.skripsi.apmodasi.app.response;

import com.google.gson.annotations.SerializedName;
import com.skripsi.apmodasi.data.model.AuthResult;

public class ResponseAuth{

	@SerializedName("auth_result")
	private AuthResult authResult;

	@SerializedName("kode")
	private String kode;

	@SerializedName("pesan")
	private String pesan;

	public AuthResult getAuthResult(){
		return authResult;
	}

	public String getKode() {
		return kode;
	}

	public String getPesan() {
		return pesan;
	}
}