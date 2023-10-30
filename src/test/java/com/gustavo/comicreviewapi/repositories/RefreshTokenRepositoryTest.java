package com.gustavo.comicreviewapi.repositories;

import java.time.Instant;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.gustavo.comicreviewapi.builders.RefreshTokenBuilder;
import com.gustavo.comicreviewapi.builders.UserBuilder;
import com.gustavo.comicreviewapi.entities.RefreshToken;
import com.gustavo.comicreviewapi.entities.User;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
public class RefreshTokenRepositoryTest {
	
	@Autowired
	RefreshTokenRepository refreshTokenRepository;
	
	@Autowired
	TestEntityManager entityManager;
	
	@Test
	@DisplayName("Must save a refresh token")
	public void saveRefreshTokenTest() {
		// Scenario
		RefreshToken refreshToken = RefreshTokenBuilder.aRefreshToken().now();
				
		// Execution
		RefreshToken savedRefreshToken = refreshTokenRepository.save(refreshToken);
		
		// Verification
		Assertions.assertThat(savedRefreshToken.getId()).isNotNull();
	}
	
	@Test
	@DisplayName("Must delete a refresh token")
	public void deleteRefreshTokenTest() {
		// Scenario
		RefreshToken refreshToken = RefreshTokenBuilder.aRefreshToken().now();
		entityManager.persist(refreshToken);
		
		RefreshToken foundRefreshToken = entityManager.find(RefreshToken.class, refreshToken.getId());
		
		// Execution
		refreshTokenRepository.delete(foundRefreshToken);
		
		RefreshToken deletedRefreshToken = entityManager.find(RefreshToken.class, refreshToken.getId());
		
		// Verification
		Assertions.assertThat(deletedRefreshToken).isNull();
	}
	
	@Test
	@DisplayName("Must get one refresh token per token")
	public void findByTokenTest() {
		// Scenario
		String token = "a6c77d54-a20b-4597-b8b0-315e7c05ab36";
		
		RefreshToken refreshToken = RefreshTokenBuilder.aRefreshToken().now();
		entityManager.persist(refreshToken);
		
		RefreshToken refreshToken2 = RefreshTokenBuilder.aRefreshToken().withToken("b4577d54-a20b-4597-b8b0-315e7c05ab41").now();
		entityManager.persist(refreshToken2);
		
		// Execution
		Optional<RefreshToken> foundRefreshToken = refreshTokenRepository.findByToken(token);
		
		// Verification
		Assertions.assertThat(foundRefreshToken.isPresent()).isTrue();
		Assertions.assertThat(foundRefreshToken.get().getToken()).isEqualTo(token);		
	}
	
	@Test
	@DisplayName("Must delete one refresh token per user")
	public void deleteByUserTest() {
		// Scenario
		User user = UserBuilder.aUser().now();
		user = entityManager.persist(user);
		
		RefreshToken refreshToken = RefreshTokenBuilder.aRefreshToken().withUser(user).now();
		entityManager.persist(refreshToken);
		
		// Execution
		refreshTokenRepository.deleteByUser(user);
		
		RefreshToken deletedRefreshToken = entityManager.find(RefreshToken.class, refreshToken.getId());
		
		// Verification
		Assertions.assertThat(deletedRefreshToken).isNull();
	}
	
	@Test
	@DisplayName("Must delete all refresh tokens with expired dates")
	public void deleteByExpiryDateBefore() {
		// Scenario
		RefreshToken refreshToken = RefreshTokenBuilder.aRefreshToken().withExpiryDate(Instant.now().plusMillis(120000)).now();
		entityManager.persist(refreshToken);
		
		RefreshToken refreshToken2 = RefreshTokenBuilder.aRefreshToken()
				.withToken("b4577d54-a20b-4597-b8b0-315e7c05ab41").withExpiryDate(Instant.now().minusMillis(120000)).now();
		entityManager.persist(refreshToken2);
		
		RefreshToken refreshToken3 = RefreshTokenBuilder.aRefreshToken()
				.withToken("c3809d54-a20b-4597-b8b0-315e7c05ab41").withExpiryDate(Instant.now().minusMillis(120000)).now();
		entityManager.persist(refreshToken3);
		
		// Execution
		refreshTokenRepository.deleteByExpiryDateBefore(Instant.now());
		
		RefreshToken notDeletedRefreshToken = entityManager.find(RefreshToken.class, refreshToken.getId());
		RefreshToken deletedRefreshToken2 = entityManager.find(RefreshToken.class, refreshToken2.getId());
		RefreshToken deletedRefreshToken3 = entityManager.find(RefreshToken.class, refreshToken3.getId());
				
		// Verification
		Assertions.assertThat(notDeletedRefreshToken).isNotNull();
		Assertions.assertThat(notDeletedRefreshToken.getToken()).isEqualTo(refreshToken.getToken());	
		Assertions.assertThat(deletedRefreshToken2).isNull();
		Assertions.assertThat(deletedRefreshToken3).isNull();
	}

}
