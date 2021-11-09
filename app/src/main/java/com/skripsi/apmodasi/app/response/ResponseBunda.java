package com.skripsi.apmodasi.app.response;

import com.skripsi.apmodasi.data.model.Bunda;

import java.util.List;

public class ResponseBunda{

	private String kode;

	private String pesan;

	private Bunda result_bunda;

	private List<Bunda> bunda_data;

	public String getKode() {
		return kode;
	}

	public String getPesan() {
		return pesan;
	}

	public Bunda getResult_bunda() {
		return result_bunda;
	}

	public List<Bunda> getBunda_data() {
		return bunda_data;
	}
}