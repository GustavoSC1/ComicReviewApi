package com.gustavo.comicreviewapi.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gustavo.comicreviewapi.dtos.AuthenticationDTO;
import com.gustavo.comicreviewapi.dtos.LoginResponseDTO;
import com.gustavo.comicreviewapi.dtos.TokenRefreshRequestDTO;
import com.gustavo.comicreviewapi.dtos.TokenRefreshResponseDTO;
import com.gustavo.comicreviewapi.services.RefreshTokenService;
import com.gustavo.comicreviewapi.services.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {
		
	@Autowired
	private UserService userService;
	
	@Autowired
	private RefreshTokenService refreshTokenService;
	
	@PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody AuthenticationDTO authenticationDTO) {
		LoginResponseDTO token = userService.login(authenticationDTO);

		return ResponseEntity.ok().body(token);
    }
	
	@PostMapping("/refreshtoken")
	public ResponseEntity<TokenRefreshResponseDTO> refreshtoken(@Valid @RequestBody TokenRefreshRequestDTO request) {
		TokenRefreshResponseDTO token = refreshTokenService.refreshToken(request);
		 
		return ResponseEntity.ok().body(token);
	}
	
	@PostMapping("/signout")
	public ResponseEntity<String> logoutUser() {
		refreshTokenService.logoutUser();
		String message = "Log out successful!";
		
		return ResponseEntity.ok().body(message);
	}

}
