package com.gustavo.comicreviewapi.dtos;

import java.io.Serializable;

import jakarta.validation.constraints.NotEmpty;

public class TokenRefreshRequestDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@NotEmpty(message="The token field is required")
	private String refreshToken;
	
	public TokenRefreshRequestDTO() {
		
	}

	public TokenRefreshRequestDTO(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

}
