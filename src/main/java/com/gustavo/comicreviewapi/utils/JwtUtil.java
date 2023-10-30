package com.gustavo.comicreviewapi.utils;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.gustavo.comicreviewapi.services.exceptions.AuthorizationException;

import java.nio.charset.StandardCharsets;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtUtil {
	
	@Value("${jwt.secret}")
	private String secret;
	
	@Value("${jwt.expiration}")
	private Long expiration;
	
	public String generateToken(UserSS user){	
		return generateTokenFromUsername(user.getUsername());
	}
		
	public String generateTokenFromUsername(String username){
		return Jwts.builder().setIssuer("Comic Review Api").setSubject("JWT Token")
				.claim("username", username)
				.setIssuedAt(new Date())
				.setExpiration(new Date((new Date()).getTime() + expiration))
				.signWith(getSignKey()).compact();				
	}
	
	public Claims validateToken(String token){	
		try {
			return Jwts.parserBuilder()
					.setSigningKey(getSignKey())
					.build()
					.parseClaimsJws(token)
					.getBody();		
		} catch (JwtException e) {
			throw new AuthorizationException("Invalid/expired token");
		}
	}
	
	private SecretKey getSignKey() {
		return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }
	
}
