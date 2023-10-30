package com.gustavo.comicreviewapi.services;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.gustavo.comicreviewapi.dtos.TokenRefreshRequestDTO;
import com.gustavo.comicreviewapi.dtos.TokenRefreshResponseDTO;
import com.gustavo.comicreviewapi.entities.RefreshToken;
import com.gustavo.comicreviewapi.repositories.RefreshTokenRepository;
import com.gustavo.comicreviewapi.repositories.UserRepository;
import com.gustavo.comicreviewapi.services.exceptions.TokenRefreshException;
import com.gustavo.comicreviewapi.utils.JwtUtil;
import com.gustavo.comicreviewapi.utils.UserSS;

@Service
public class RefreshTokenService {
	
	private Long refreshTokenDurationMs;
	
	private RefreshTokenRepository refreshTokenRepository;
	
	private UserRepository userRepository;
	
	private JwtUtil jwtUtil;
			
	public RefreshTokenService(@Value("${jwt.refreshExpiration}") Long refreshTokenDurationMs, RefreshTokenRepository refreshTokenRepository,
			UserRepository userRepository, JwtUtil jwtUtil) {
		this.refreshTokenDurationMs = refreshTokenDurationMs;
		this.refreshTokenRepository = refreshTokenRepository;
		this.userRepository = userRepository;
		this.jwtUtil = jwtUtil;
	}

	public TokenRefreshResponseDTO refreshToken(TokenRefreshRequestDTO request) {
		String requestRefreshToken = request.getRefreshToken();
		
		return findByToken(requestRefreshToken).map(x -> verifyExpiration(x))
				.map(RefreshToken::getUser)
				.map(user -> {
					String token = jwtUtil.generateTokenFromUsername(user.getEmail());
					return new TokenRefreshResponseDTO(token, requestRefreshToken);
				}).orElseThrow(() -> new TokenRefreshException("Refresh token is not in database!"));
				
	}
	
	public void logoutUser() {
		UserSS userAuthenticated = UserService.authenticated();		
		Long userId = userAuthenticated.getId();
		deleteByUserId(userId);
	}
	
	public Optional<RefreshToken> findByToken(String token) {
		return refreshTokenRepository.findByToken(token);
	}
	
	public RefreshToken createRefreshToken(Long userId) {
		RefreshToken refreshToken = new RefreshToken();
		
		refreshToken.setUser(userRepository.findById(userId).get());
		refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
		refreshToken.setToken(UUID.randomUUID().toString());
		
		refreshToken = refreshTokenRepository.save(refreshToken);
		return refreshToken;
	}
	
	public RefreshToken verifyExpiration(RefreshToken token) {
		if(token.getExpiryDate().compareTo(Instant.now()) < 0) {
			refreshTokenRepository.delete(token);
			throw new TokenRefreshException("Refresh token was expired. Please make a new signin request");
		}
		
		return token;
	}
	
	public void deleteByUserId(Long userId) {
		refreshTokenRepository.deleteByUser(userRepository.findById(userId).get());
	}

}
