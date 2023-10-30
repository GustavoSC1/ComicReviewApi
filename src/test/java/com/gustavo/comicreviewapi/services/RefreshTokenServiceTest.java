package com.gustavo.comicreviewapi.services;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.gustavo.comicreviewapi.builders.RefreshTokenBuilder;
import com.gustavo.comicreviewapi.builders.UserBuilder;
import com.gustavo.comicreviewapi.dtos.TokenRefreshRequestDTO;
import com.gustavo.comicreviewapi.dtos.TokenRefreshResponseDTO;
import com.gustavo.comicreviewapi.entities.RefreshToken;
import com.gustavo.comicreviewapi.entities.User;
import com.gustavo.comicreviewapi.repositories.RefreshTokenRepository;
import com.gustavo.comicreviewapi.repositories.UserRepository;
import com.gustavo.comicreviewapi.services.exceptions.TokenRefreshException;
import com.gustavo.comicreviewapi.utils.JwtUtil;
import com.gustavo.comicreviewapi.utils.UserSS;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class RefreshTokenServiceTest {
	
	RefreshTokenService refreshTokenService;
	
	@MockBean
	RefreshTokenRepository refreshTokenRepository;
	
	@MockBean
	UserRepository userRepository;
	
	@MockBean
	JwtUtil jwtUtil;
	
	@BeforeEach
	public void setUp() {
		this.refreshTokenService = Mockito.spy(new RefreshTokenService(120000l, refreshTokenRepository, 
				userRepository, jwtUtil));
	}
	
	@Test
	@DisplayName("Must generate a new access token when a valid refresh token is provided")
	public void refreshTokenTest() {
		// Scenario
		Long id = 1l;
		
		String token = "eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJDb21pYyBSZXZpZXcgQXBpIiwic3ViIjoiSldUIFRva2VuIiwidXNlcm5hbWUiOiJndS5jcnV6MTdAaG90bWFpbC5jb20iLCJpYXQiOjE2OTUzODU3MDMsImV4cCI6MTY5NTQxNTcwM30.Ksery3op3UJ79HOflaJ9EEb9YI_Zv74UaQfL7MJ8sQXBYvBJ3ESEwcG4edCY5CjEKM1WxyvU3PSYJCc5JobWRg";
		
		TokenRefreshRequestDTO tokenRefreshRequestDto = new TokenRefreshRequestDTO("a6c77d54-a20b-4597-b8b0-315e7c05ab36");
		
		User user = UserBuilder.aUser().withId(id).now();
		
		RefreshToken refreshToken = RefreshTokenBuilder.aRefreshToken().withId(id).withUser(user).now();
		
		Mockito.doReturn(Optional.of(refreshToken)).when(refreshTokenService).findByToken(tokenRefreshRequestDto.getRefreshToken());
		
		Mockito.doReturn(refreshToken).when(refreshTokenService).verifyExpiration(refreshToken);
		
		Mockito.when(jwtUtil.generateTokenFromUsername(user.getEmail())).thenReturn(token);
		
		// Execution
		TokenRefreshResponseDTO tokenRefreshResponseDto = refreshTokenService.refreshToken(tokenRefreshRequestDto);
		
		// Verification
		Assertions.assertThat(tokenRefreshResponseDto.getAccessToken()).isEqualTo(token);
		Assertions.assertThat(tokenRefreshResponseDto.getRefreshToken()).isEqualTo(tokenRefreshRequestDto.getRefreshToken());
		Assertions.assertThat(tokenRefreshResponseDto.getTokenType()).isEqualTo("Bearer");
	}
	
	@Test
	@DisplayName("Should not generate an access token and should return an exception when the refresh token is not valid")
	public void notGenerateAccessTokenWhenRefreshTokenIsNotValid() {
		// Scenario
		TokenRefreshRequestDTO tokenRefreshRequestDto = new TokenRefreshRequestDTO("a6c77d54-a20b-4597-b8b0-315e7c05ab36");
		
		Mockito.doReturn(Optional.empty()).when(refreshTokenService).findByToken(tokenRefreshRequestDto.getRefreshToken());
		
		// Execution and Verification
		Exception exception = assertThrows(TokenRefreshException.class, () -> {refreshTokenService.refreshToken(tokenRefreshRequestDto);});
		
		String expectedMessage = "Refresh token is not in database!";
		String actualMessage = exception.getMessage();
		
		Assertions.assertThat(actualMessage).isEqualTo(expectedMessage);		
	}
	
	@Test
	@DisplayName("Must get the authenticated user and invoke the deleteByUserId method to delete their refresh token")
	public void logoutUserTest() {
		try(MockedStatic<UserService> mockedStatic = Mockito.mockStatic(UserService.class)) {
			// Scenario
			Long id = 1l;
			
			User user = UserBuilder.aUser().withId(id).now();
			
			UserSS userSS = new UserSS(user.getId(), user.getEmail(), user.getPassword(), user.getProfiles());
			
			mockedStatic.when(UserService::authenticated).thenReturn(userSS);
			Mockito.doNothing().when(refreshTokenService).deleteByUserId(id);
			
			// Execution
			org.junit.jupiter.api.Assertions.assertDoesNotThrow(() -> refreshTokenService.logoutUser());
			
			// Verification
			Mockito.verify(refreshTokenService, Mockito.times(1)).deleteByUserId(user.getId());
		}
	}
	
	@Test
	@DisplayName("It should return a refresh token object for the value of your token")
	public void findByTokenTest() {
		// Scenario		
		Long id = 1l;
		String token = "a6c77d54-a20b-4597-b8b0-315e7c05ab36";
		
		User user = UserBuilder.aUser().withId(id).now();
		RefreshToken refreshToken = RefreshTokenBuilder.aRefreshToken().withId(id).withUser(user).now();
		
		Mockito.when(refreshTokenRepository.findByToken(token)).thenReturn(Optional.of(refreshToken));
		
		// Execution
		Optional<RefreshToken> foundRefreshToken = refreshTokenService.findByToken(token);
		
		// Verification
		Assertions.assertThat(foundRefreshToken.isPresent()).isTrue();
		Assertions.assertThat(foundRefreshToken.get().getId()).isEqualTo(refreshToken.getId());
		Assertions.assertThat(foundRefreshToken.get().getToken()).isEqualTo(refreshToken.getToken());
		Assertions.assertThat(foundRefreshToken.get().getExpiryDate()).isEqualTo(refreshToken.getExpiryDate());
		Assertions.assertThat(foundRefreshToken.get().getUser()).isEqualTo(refreshToken.getUser());
	}
	
	@Test
	@DisplayName("Must generate a new refresh token for the user with an expiration date and save it in the database")
	public void createRefreshTokenTest() {
		String instantExpected = "2023-10-30T11:30:30.54Z";
		
		// A classe Clock é responsável por providenciar o tempo atual, que no Java é chamado de 
		// instante (instant). Quando fazemos, por exemplo, um LocalDate.now(), o Java lê o instante 
		// do relógio do seu computador. Porém, podemos manipular este instante colocando um Clock 
		// diferente no lugar dele.
	    Clock clock = Clock.fixed(Instant.parse(instantExpected), ZoneId.of("UTC"));
	    
		Instant expiryDate = Instant.now(clock);
		UUID token = UUID.fromString("8fcb78db-229d-40ee-b4ef-86d415755ec0");
		
		MockedStatic<Instant> mockedInstantStatic = null;
		MockedStatic<UUID> mockedUuidStatic = null;
		try {
			mockedInstantStatic = Mockito.mockStatic(Instant.class);
			mockedUuidStatic = Mockito.mockStatic(UUID.class);
					
			// Scenario
			Long id = 1l;
			
			User user = UserBuilder.aUser().withId(id).now();
			
			RefreshToken refreshToken = RefreshTokenBuilder.aRefreshToken().withId(id).withExpiryDate(expiryDate).withToken(token.toString()).withUser(user).now();
			
			Mockito.when(userRepository.findById(id)).thenReturn(Optional.of(user));
			
			mockedInstantStatic.when(Instant::now).thenReturn(expiryDate);
			
			mockedUuidStatic.when(UUID::randomUUID).thenReturn(token);
			
			Mockito.when(refreshTokenRepository.save(Mockito.any(RefreshToken.class))).thenReturn(refreshToken);
			
			// Execution
			RefreshToken savedRefreshToken = refreshTokenService.createRefreshToken(id);
			
			// Verification
			System.out.println(expiryDate.toString());
			Assertions.assertThat(savedRefreshToken.getId()).isEqualTo(refreshToken.getId());
			Assertions.assertThat(savedRefreshToken.getToken()).isEqualTo(token.toString());
			Assertions.assertThat(savedRefreshToken.getExpiryDate()).isEqualTo(expiryDate);
			Assertions.assertThat(savedRefreshToken.getUser()).isEqualTo(refreshToken.getUser());
		} finally {
			mockedInstantStatic.close();
			mockedUuidStatic.close();
		}
	}
	
	@Test
	@DisplayName("It must check whether the refresh token has already expired and, if not, it must return the same object")
	public void verifyExpirationTest() {
		// Scenario
		RefreshToken refreshToken = RefreshTokenBuilder.aRefreshToken().withId(1l)
				.withExpiryDate(Instant.now().plusMillis(120000)).now();
		
		// Execution
		RefreshToken refreshTokenVerified = refreshTokenService.verifyExpiration(refreshToken);
		
		// Verification
		Assertions.assertThat(refreshTokenVerified.getId()).isEqualTo(refreshToken.getId());	
		Assertions.assertThat(refreshTokenVerified.getToken()).isEqualTo(refreshToken.getToken());
		Assertions.assertThat(refreshTokenVerified.getExpiryDate()).isEqualTo(refreshToken.getExpiryDate());	
	}
	
	@Test
	@DisplayName("It should return an error when the refresh token has already expired")
	public void refreshTokenExpiredTest() {
		// Scenario
		RefreshToken refreshToken = RefreshTokenBuilder.aRefreshToken().withId(1l)
				.withExpiryDate(Instant.now().minusMillis(120000)).now();
		
		// Execution and Verification
		Exception exception = assertThrows(TokenRefreshException.class, () -> {refreshTokenService.verifyExpiration(refreshToken);});
	
		String expectedMessage = "Refresh token was expired. Please make a new signin request";
		String actualMessage = exception.getMessage();
		
		Assertions.assertThat(actualMessage).isEqualTo(expectedMessage);		
	}
	
	@Test
	@DisplayName("Must get a user by id and delete the refresh token for that user")
	public void deleteByUserIdTest() {
		// Scenario
		Long id = 1l;
		
		User user = UserBuilder.aUser().withId(id).now();
		
		Mockito.when(userRepository.findById(id)).thenReturn(Optional.of(user));
		
		// Execution
		org.junit.jupiter.api.Assertions.assertDoesNotThrow(() -> refreshTokenService.deleteByUserId(id));
		
		// Verification
		Mockito.verify(refreshTokenRepository, Mockito.times(1)).deleteByUser(user);
	}

}
