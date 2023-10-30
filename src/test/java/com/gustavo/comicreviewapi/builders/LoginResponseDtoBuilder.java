package com.gustavo.comicreviewapi.builders;

import java.util.List;

import com.gustavo.comicreviewapi.dtos.LoginResponseDTO;

public class LoginResponseDtoBuilder {
	
	private LoginResponseDTO loginResponseDto;
	
	private LoginResponseDtoBuilder() {}
	
	public static LoginResponseDtoBuilder aLoginResponseDTO() {
		LoginResponseDtoBuilder builder = new LoginResponseDtoBuilder();
		builder.loginResponseDto = new LoginResponseDTO();
		builder.loginResponseDto.setId(1l);
		builder.loginResponseDto.setToken("eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJDb21pYyBSZXZpZXcgQXBpIiwic3ViIjoiSldUIFRva2VuIiwidXNlcm5hbWUiOiJndS5jcnV6MTdAaG90bWFpbC5jb20iLCJpYXQiOjE2OTUzODU3MDMsImV4cCI6MTY5NTQxNTcwM30.Ksery3op3UJ79HOflaJ9EEb9YI_Zv74UaQfL7MJ8sQXBYvBJ3ESEwcG4edCY5CjEKM1WxyvU3PSYJCc5JobWRg");
		builder.loginResponseDto.setType("Bearer");
		builder.loginResponseDto.setRefreshToken("a6c77d54-a20b-4597-b8b0-315e7c05ab36");
		builder.loginResponseDto.setEmail("gu.cruz17@hotmail.com");
		builder.loginResponseDto.setProfiles(List.of("ROLE_USER"));
		
		return builder;
	}
	
	public LoginResponseDtoBuilder withId(Long id) {
		loginResponseDto.setId(id);
		return this;
	}
	
	public LoginResponseDtoBuilder withToken(String token) {
		loginResponseDto.setToken(token);
		return this;
	}
	
	public LoginResponseDtoBuilder withRefreshToken(String refreshToken) {
		loginResponseDto.setRefreshToken(refreshToken);
		return this;
	}
	
	public LoginResponseDtoBuilder withEmail(String email) {
		loginResponseDto.setEmail(email);
		return this;
	}
	
	public LoginResponseDtoBuilder withProfile(List<String> profiles) {
		loginResponseDto.setProfiles(profiles);
		return this;
	}
	
	public LoginResponseDTO now() {
		return loginResponseDto;
	}

}
