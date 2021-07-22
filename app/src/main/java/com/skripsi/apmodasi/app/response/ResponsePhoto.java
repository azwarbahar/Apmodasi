package com.skripsi.apmodasi.app.response;

import com.google.gson.annotations.SerializedName;

public class ResponsePhoto{

	@SerializedName("url")
	private String url;

	@SerializedName("error")
	private boolean error;

	@SerializedName("status")
	private String status;

	@SerializedName("message")
	private String message;

	public String getUrl() {
		return url;
	}

	public boolean isError() {
		return error;
	}

	public String getStatus() {
		return status;
	}

	public String getMessage() {
		return message;
	}
}
