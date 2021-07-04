package com.skripsi.apmodasi.data.model;

import com.google.gson.annotations.SerializedName;

public class Bunda {

	@SerializedName("nama_bunda")
	private String namaBunda;

	@SerializedName("kontak_bunda")
	private String kontakBunda;

	@SerializedName("id_bunda")
	private String idBunda;

	@SerializedName("update_at")
	private String updateAt;

	@SerializedName("alamat_bunda")
	private String alamatBunda;

	@SerializedName("foto_bunda")
	private String fotoBunda;

	@SerializedName("creatde_at")
	private String creatdeAt;

	public String getNamaBunda(){
		return namaBunda;
	}

	public String getKontakBunda(){
		return kontakBunda;
	}

	public String getIdBunda(){
		return idBunda;
	}

	public String getUpdateAt(){
		return updateAt;
	}

	public String getAlamatBunda(){
		return alamatBunda;
	}

	public String getFotoBunda(){
		return fotoBunda;
	}

	public String getCreatdeAt(){
		return creatdeAt;
	}
}