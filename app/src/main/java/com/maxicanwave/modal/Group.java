package com.maxicanwave.modal;

public class Group {
	
	private int id;
	private String groupname;
	private String u_id;
	

	public Group() {
		
	}


	public Group(int id, String groupname, String u_id) {
		super();
		this.id = id;
		this.groupname = groupname;
		this.u_id = u_id;
	}
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getGroupname() {
		return groupname;
	}
	public void setGroupname(String groupname) {
		this.groupname = groupname;
	}
	public String getU_id() {
		return u_id;
	}
	public void setU_id(String u_id) {
		this.u_id = u_id;
	}
	
	
	

}
