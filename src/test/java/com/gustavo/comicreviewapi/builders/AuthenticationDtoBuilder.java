package com.gustavo.comicreviewapi.builders;

import com.gustavo.comicreviewapi.dtos.AuthenticationDTO;

public class AuthenticationDtoBuilder {
	
	private AuthenticationDTO authenticationDto;
	
	private AuthenticationDtoBuilder() {}
	
	public static AuthenticationDtoBuilder aAuthenticationDto() {
		AuthenticationDtoBuilder builder = new AuthenticationDtoBuilder();
		builder.authenticationDto = new AuthenticationDTO();
		builder.authenticationDto.setEmail("gu.cruz17@hotmail.com");
		builder.authenticationDto.setPassword("Password1.");
		
		return builder;
	}
	
	public AuthenticationDtoBuilder withEmail(String email) {
		authenticationDto.setEmail(email);		
		return this;
	}
	
	public AuthenticationDtoBuilder withPassword(String password) {
		authenticationDto.setPassword(password);		
		return this;
	}
	
	public AuthenticationDTO now() {
		return authenticationDto;
	}

}
