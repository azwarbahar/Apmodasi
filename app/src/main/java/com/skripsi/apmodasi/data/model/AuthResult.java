package com.skripsi.apmodasi.data.model;


public class AuthResult{

	private String id_auth;
	private String user_kode;
	private String username;
	private String password;
	private String status;
	private String role;
	private String created_at;
	private String update_at;

	public String getId_auth() {
		return id_auth;
	}

	public String getUser_kode() {
		return user_kode;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public String getStatus() {
		return status;
	}

	public String getRole() {
		return role;
	}

	public String getCreated_at() {
		return created_at;
	}

	public String getUpdate_at() {
		return update_at;
	}
}