package com.gustavo.comicreviewapi.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gustavo.comicreviewapi.dtos.CredentialsDTO;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	
	private AuthenticationManager authenticationManager;
    
    private JWTUtil jwtUtil;
    
    public JWTAuthenticationFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil) {
    	setAuthenticationFailureHandler(new JWTAuthenticationFailureHandler());
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }
	
    //Tenta autenticar
    @Override
	public Authentication attemptAuthentication(HttpServletRequest req, 
												HttpServletResponse res) throws AuthenticationException {
		try {
			//Vai pegar os dados que vieram na requisição e converter para CredenciaisDTO
			CredentialsDTO creds = new ObjectMapper()
	                .readValue(req.getInputStream(), CredentialsDTO.class);
						
	        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(creds.getEmail(), creds.getPassword(), new ArrayList<>());
	        
	        //Método que vai verificar usuário e senha com base no que foi implementado (UserDetail | UserDetailService)
	        //https://spring.io/guides/topicals/spring-security-architecture
	        Authentication auth = authenticationManager.authenticate(authToken);
	        //Objeto que informa para o spring security se autenticação ocorreu com sucesso
	        return auth;
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}	
	}
	
	//Se a autenticação ocorrer com sucesso
	//Vai gerar um token e acrescentar na resposta da requisição
	@Override
	protected void successfulAuthentication(HttpServletRequest req,
											HttpServletResponse res,
											FilterChain chain,
											Authentication auth) throws IOException, ServletException {
		//Pega email da pessoa que fez login
		String username = ((UserSS) auth.getPrincipal()).getUsername();
		//Gera token
	    String token = jwtUtil.generateToken(username);
	    //Retorna o token no cabeçalho na resposta da requisição
	    res.addHeader("Authorization", "Bearer " + token);
	    // Expondo o header Authorization nas respostas.
        res.addHeader("access-control-expose-headers", "Authorization");
	}
	
	//Personalizar o que vai acontecer caso a autenticação falhe
	private class JWTAuthenticationFailureHandler implements AuthenticationFailureHandler {
		 
        @Override
        public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
                throws IOException, ServletException {
            response.setStatus(401);
            response.setContentType("application/json"); 
            response.getWriter().append(json());
        }
        
        private String json() {
            long date = new Date().getTime();
            return "{\"timestamp\": " + date + ", "
                + "\"status\": 401, "
                + "\"error\": \"Not authorized\", "
                + "\"message\": \"Invalid email or password\", "
                + "\"path\": \"/login\"}";
        }
    }

}
