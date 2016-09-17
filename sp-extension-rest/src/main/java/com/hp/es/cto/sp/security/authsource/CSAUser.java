package com.hp.es.cto.sp.security.authsource;

public class CSAUser {
	private String id;
	private String email;
	
	public CSAUser(String id, String email){
		this.id=id;
		this.email=email;
	}
	
	public String getId(){
		return id;
	}
	
	public String getEmail(){
		return email;
	}

}
