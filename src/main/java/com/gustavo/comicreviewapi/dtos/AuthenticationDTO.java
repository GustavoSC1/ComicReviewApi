package com.gustavo.comicreviewapi.dtos;

import java.io.Serializable;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public class AuthenticationDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@NotEmpty(message="The email field is required")
	@Email(message="Invalid email")
	private String email;
	
	@NotEmpty(message="The password field is required")
	private String password;
	 
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
}
