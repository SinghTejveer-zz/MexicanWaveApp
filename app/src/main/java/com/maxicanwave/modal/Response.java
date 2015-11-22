package com.maxicanwave.modal;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Response implements Serializable{

	private int id;

	private String status;

	private String msg;


	public Response() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Response(String status, String msg, int id) {
		super();
		this.id = id;
		this.status = status;
		this.msg = msg;
	}



	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	

}
