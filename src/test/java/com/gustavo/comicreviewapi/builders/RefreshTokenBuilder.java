package com.gustavo.comicreviewapi.builders;

import java.time.Instant;

import com.gustavo.comicreviewapi.entities.RefreshToken;
import com.gustavo.comicreviewapi.entities.User;

public class RefreshTokenBuilder {
	
	private RefreshToken refreshToken;
	
	private RefreshTokenBuilder() {}
	
	public static RefreshTokenBuilder aRefreshToken() {
		RefreshTokenBuilder builder = new RefreshTokenBuilder();
		builder.refreshToken = new RefreshToken();
		builder.refreshToken.setExpiryDate(Instant.now());
		builder.refreshToken.setToken("a6c77d54-a20b-4597-b8b0-315e7c05ab36");
		return builder;
	}
	
	public RefreshTokenBuilder withId(Long id) {
		refreshToken.setId(id);
		return this;
	}
	
	public RefreshTokenBuilder withToken(String token) {
		refreshToken.setToken(token);
		return this;
	}
	
	public RefreshTokenBuilder withExpiryDate(Instant instant) {
		refreshToken.setExpiryDate(instant);
		return this;
	}
	
	public RefreshTokenBuilder withUser(User user) {
		refreshToken.setUser(user);
		return this;
	}
	
	public RefreshToken now() {
		return refreshToken;
	}

}
