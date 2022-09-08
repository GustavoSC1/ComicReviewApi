package com.gustavo.comicreviewapi.resources;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gustavo.comicreviewapi.security.JWTUtil;
import com.gustavo.comicreviewapi.security.UserSS;
import com.gustavo.comicreviewapi.services.UserService;

@RestController
@RequestMapping("/auth")
public class AuthController {
	
	@Autowired
	private JWTUtil jwtUtil;
	
	// Atualiza o token - O usuário precisa estra logado para acessar
	@PostMapping("/refresh_token")
	public ResponseEntity<Void> refreshToken(HttpServletResponse response) {
		UserSS user = UserService.authenticated();
		String token = jwtUtil.generateToken(user.getUsername());
		response.addHeader("Authorization", "Bearer " + token);
		// Expondo o header Authorization nas respostas.
		response.addHeader("access-control-expose-headers", "Authorization");
		return ResponseEntity.noContent().build();
	}

}
