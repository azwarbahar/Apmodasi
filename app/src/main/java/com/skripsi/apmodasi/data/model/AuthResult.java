package com.skripsi.apmodasi.data.model;

import com.google.gson.annotations.SerializedName;

public class AuthResult{

	@SerializedName("password")
	private String password;

	@SerializedName("role")
	private String role;

	@SerializedName("user_id")
	private String userId;

	@SerializedName("id_auth")
	private String idAuth;

	@SerializedName("update_at")
	private String updateAt;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("username")
	private String username;

	@SerializedName("status")
	private String status;

	public String getPassword(){
		return password;
	}

	public String getRole(){
		return role;
	}

	public String getUserId(){
		return userId;
	}

	public String getIdAuth(){
		return idAuth;
	}

	public String getUpdateAt(){
		return updateAt;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public String getUsername(){
		return username;
	}

	public String getStatus(){
		return status;
	}
}