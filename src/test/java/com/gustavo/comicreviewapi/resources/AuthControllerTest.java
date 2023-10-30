package com.gustavo.comicreviewapi.resources;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gustavo.comicreviewapi.builders.AuthenticationDtoBuilder;
import com.gustavo.comicreviewapi.builders.LoginResponseDtoBuilder;
import com.gustavo.comicreviewapi.configs.SecurityConfig;
import com.gustavo.comicreviewapi.dtos.AuthenticationDTO;
import com.gustavo.comicreviewapi.dtos.LoginResponseDTO;
import com.gustavo.comicreviewapi.dtos.TokenRefreshRequestDTO;
import com.gustavo.comicreviewapi.dtos.TokenRefreshResponseDTO;
import com.gustavo.comicreviewapi.filters.JWTAccessDeniedHandler;
import com.gustavo.comicreviewapi.filters.JWTAuthenticationEntryPoint;
import com.gustavo.comicreviewapi.services.RefreshTokenService;
import com.gustavo.comicreviewapi.services.UserService;
import com.gustavo.comicreviewapi.services.exceptions.AuthenticationErrorException;
import com.gustavo.comicreviewapi.services.exceptions.TokenRefreshException;
import com.gustavo.comicreviewapi.utils.JwtUtil;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = AuthController.class)
@Import({SecurityConfig.class, JWTAccessDeniedHandler.class, JWTAuthenticationEntryPoint.class})
@AutoConfigureMockMvc
public class AuthControllerTest {
	
	static String AUTH_API = "/auth";
	
	@Autowired
	MockMvc mvc;
	
	@MockBean
	UserService userService;
	
	@MockBean
	UserDetailsService userDetailsService;
	
	@MockBean
	RefreshTokenService refreshTokenService;
	
	@MockBean
	JwtUtil jwtUtil;
	
	@Test
	@DisplayName("Must login and return the token")
	public void loginTest() throws Exception {
		// Scenario		
		LoginResponseDTO loginResponseDto = LoginResponseDtoBuilder.aLoginResponseDTO().now();
		
		AuthenticationDTO authenticationDTO = AuthenticationDtoBuilder.aAuthenticationDto().now();
		
		BDDMockito.given(userService.login(Mockito.any(AuthenticationDTO.class))).willReturn(loginResponseDto);
		
		String json = new ObjectMapper().writeValueAsString(authenticationDTO);
		
		// Execution
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
													.post(AUTH_API.concat("/login"))
													.contentType(MediaType.APPLICATION_JSON)
													.accept(MediaType.APPLICATION_JSON)
													.content(json);
		
		// Verification
		mvc
		.perform(request)
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("token").value(loginResponseDto.getToken()))
		.andExpect(MockMvcResultMatchers.jsonPath("type").value(loginResponseDto.getType()))
		.andExpect(MockMvcResultMatchers.jsonPath("refreshToken").value(loginResponseDto.getRefreshToken()))
		.andExpect(MockMvcResultMatchers.jsonPath("id").value(loginResponseDto.getId()))
		.andExpect(MockMvcResultMatchers.jsonPath("email").value(loginResponseDto.getEmail()))
		.andExpect(MockMvcResultMatchers.jsonPath("profiles").value(loginResponseDto.getProfiles().get(0)));
	}
	
	@Test
	@DisplayName("Should generate an Authentication Error when login is unsuccessful")
	public void incorrectLoginTest() throws Exception {
		// Scenario
		AuthenticationDTO authenticationDTO = AuthenticationDtoBuilder.aAuthenticationDto().now();
			
		BDDMockito.given(userService.login(Mockito.any(AuthenticationDTO.class))).willThrow(new AuthenticationErrorException("Incorrect username or password!"));
		
		String json = new ObjectMapper().writeValueAsString(authenticationDTO);
		
		// Execution
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
													.post(AUTH_API.concat("/login"))
													.contentType(MediaType.APPLICATION_JSON)
													.accept(MediaType.APPLICATION_JSON)
													.content(json);
		
		// Verification
		mvc
		.perform(request)
		.andExpect(MockMvcResultMatchers.status().isForbidden())
		.andExpect(MockMvcResultMatchers.jsonPath("error").value("Access denied"))
		.andExpect(MockMvcResultMatchers.jsonPath("message").value("Incorrect username or password!"));	
	}
	
	@Test
	@DisplayName("Must generate a new access token when a valid refresh token is provided")
	public void refreshTokenTest() throws Exception {
		// Scenario
		String token = "eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJDb21pYyBSZXZpZXcgQXBpIiwic3ViIjoiSldUIFRva2VuIiwidXNlcm5hbWUiOiJndS5jcnV6MTdAaG90bWFpbC5jb20iLCJpYXQiOjE2OTUzODU3MDMsImV4cCI6MTY5NTQxNTcwM30.Ksery3op3UJ79HOflaJ9EEb9YI_Zv74UaQfL7MJ8sQXBYvBJ3ESEwcG4edCY5CjEKM1WxyvU3PSYJCc5JobWRg";
		
		TokenRefreshRequestDTO tokenRefreshRequestDto = new TokenRefreshRequestDTO("a6c77d54-a20b-4597-b8b0-315e7c05ab36");
		
		TokenRefreshResponseDTO tokenRefreshResponseDto = 
				new TokenRefreshResponseDTO(token, tokenRefreshRequestDto.getRefreshToken());
		
		BDDMockito.given(refreshTokenService.refreshToken(Mockito.any(TokenRefreshRequestDTO.class))).willReturn(tokenRefreshResponseDto);
		
		String json = new ObjectMapper().writeValueAsString(tokenRefreshRequestDto);
		
		// Execution
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
													.post(AUTH_API.concat("/refreshtoken"))
													.contentType(MediaType.APPLICATION_JSON)
													.accept(MediaType.APPLICATION_JSON)
													.content(json);
		
		// Verification
		mvc
		.perform(request)
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("accessToken").value(tokenRefreshResponseDto.getAccessToken()))
		.andExpect(MockMvcResultMatchers.jsonPath("refreshToken").value(tokenRefreshResponseDto.getRefreshToken()))
		.andExpect(MockMvcResultMatchers.jsonPath("tokenType").value(tokenRefreshResponseDto.getTokenType()));
	}
	
	@Test
	@DisplayName("Should not generate an access token and should return an exception when the refresh token is not valid")
	public void invalidRefreshToken() throws Exception {
		// Scenario		
		TokenRefreshRequestDTO tokenRefreshRequestDto = new TokenRefreshRequestDTO("a6c77d54-a20b-4597-b8b0-315e7c05ab36");
		
		BDDMockito.given(refreshTokenService.refreshToken(Mockito.any(TokenRefreshRequestDTO.class))).willThrow(new TokenRefreshException("Refresh token is not in database!"));
				
		String json = new ObjectMapper().writeValueAsString(tokenRefreshRequestDto);
		
		// Execution
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.post(AUTH_API.concat("/refreshtoken"))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(json);
		
		// Verification
		mvc
		.perform(request)
		.andExpect(MockMvcResultMatchers.status().isForbidden())
		.andExpect(MockMvcResultMatchers.jsonPath("error").value("Access denied"))
		.andExpect(MockMvcResultMatchers.jsonPath("message").value("Refresh token is not in database!"));
	}
	
	@Test
	@DisplayName("Must get the authenticated user and invoke the deleteByUserId method to delete their refresh token")
	public void logoutUser() throws Exception {					
		// Execution
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.post(AUTH_API.concat("/signout"))
				.accept(MediaType.APPLICATION_JSON);
				
		// Verification
		mvc
		.perform(request)
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$").value("Log out successful!"));
	}

}
