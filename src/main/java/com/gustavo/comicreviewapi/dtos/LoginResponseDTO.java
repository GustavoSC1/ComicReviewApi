package com.gustavo.comicreviewapi.dtos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class LoginResponseDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String token;
	private String type = "Bearer";
	private String refreshToken;
	private Long id;
	private String email;
	private List<String> profiles = new ArrayList<>();
	
	public LoginResponseDTO() {

	}

	public LoginResponseDTO(String token, String refreshToken, Long id, String email,
			List<String> profiles) {
		super();
		this.token = token;
		this.refreshToken = refreshToken;
		this.id = id;
		this.email = email;
		this.profiles = profiles;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<String> getProfiles() {
		return profiles;
	}

	public void setProfiles(List<String> profiles) {
		this.profiles = profiles;
	}
	
}
