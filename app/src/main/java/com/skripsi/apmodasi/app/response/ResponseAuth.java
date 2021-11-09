package com.skripsi.apmodasi.app.response;

import com.skripsi.apmodasi.data.model.AuthResult;

public class ResponseAuth{

	private AuthResult auth_result;

	private String kode;

	private String pesan;

	public AuthResult getAuthResult(){
		return auth_result;
	}

	public String getKode() {
		return kode;
	}

	public String getPesan() {
		return pesan;
	}
}