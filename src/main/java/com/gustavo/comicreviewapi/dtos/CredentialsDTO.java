package com.gustavo.comicreviewapi.dtos;

import java.io.Serializable;

public class CredentialsDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String email;
	private String password;
	
	public CredentialsDTO() {
		
	}
	
	public String getEmail() {
		return email;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
}
