package com.gustavo.comicreviewapi.filters;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import com.gustavo.comicreviewapi.utils.JwtUtil;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {
	
	//@Autowired
	private JwtUtil jwtUtil;
	
	//@Autowired
    private HandlerExceptionResolver handlerExceptionResolver;
        
    private UserDetailsService userDetailsService;
    	
	public JwtRequestFilter(JwtUtil jwtUtil, HandlerExceptionResolver handlerExceptionResolver, UserDetailsService userDetailsService) {
		super();
		this.jwtUtil = jwtUtil;
		this.handlerExceptionResolver = handlerExceptionResolver;
		this.userDetailsService = userDetailsService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {	
		// Obtem o valor do cabeçalho Authorization
		String authHeader = request.getHeader("Authorization");
		try {
			if(null != authHeader && authHeader.startsWith("Bearer ")) {
				String token = authHeader.substring(7);
				Claims claims = jwtUtil.validateToken(token);
				UserDetails user = userDetailsService.loadUserByUsername((String)claims.get("username"));
				
				// Cria o objeto Authentication com o username e as authorities obtidos do token
				Authentication auth = new UsernamePasswordAuthenticationToken(user, null, 
						user.getAuthorities());
				SecurityContextHolder.getContext().setAuthentication(auth);	
				
			}
			filterChain.doFilter(request, response);		
		} catch ( Exception exception) {			
			// Resolverá exceção lançada pela aplicação. Também permitirá implementar um 
			// mecanismo uniforme de tratamento de exceções na API REST.
			handlerExceptionResolver.resolveException(request, response, null, exception);
		}		
		
	}
	
	@Override
	// O filtro só não deve ser executado na operação de login
	protected boolean shouldNotFilter(HttpServletRequest request) {
		return request.getServletPath().equals("/auth/login");
	}

}
