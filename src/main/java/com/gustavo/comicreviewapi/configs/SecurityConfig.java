package com.gustavo.comicreviewapi.configs;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.gustavo.comicreviewapi.security.JWTAuthenticationFilter;
import com.gustavo.comicreviewapi.security.JWTAuthorizationFilter;
import com.gustavo.comicreviewapi.security.JWTUtil;

@Configuration
@EnableWebSecurity
//Permite por anotações de pré autorização nos endpoints
//https://developer.okta.com/blog/2019/06/20/spring-preauthorize
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	//O Spring vai buscar a implementação UserDetailsServideImpl
	@Autowired
	private UserDetailsService userDetailsService;
	
	//Interface que representa o ambiente em que o aplicativo atual está sendo executado.
	@Autowired
	private Environment env;
	
	@Autowired
	private JWTUtil jwtUtil;
	
	public static final String[] PUBLIC_MATCHERS = {
		"/h2-console/**"
	};
	
	public static final String[] PUBLIC_MATCHERS_GET = {
		"/comics/**",
		"/reviews/**",
		"/comments/**"
	};
	
	public static final String[] PUBLIC_MATCHERS_POST = {
			"/users/**"
	};
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		//Importante para o h2 funcionar
		if (Arrays.asList(env.getActiveProfiles()).contains("test")) {
			http.headers().frameOptions().disable();
		}
		
		http.cors().and().csrf().disable();
		http.authorizeRequests()
			.antMatchers(HttpMethod.POST, PUBLIC_MATCHERS_POST).permitAll()
			.antMatchers(HttpMethod.GET, PUBLIC_MATCHERS_GET).permitAll()
			.antMatchers(PUBLIC_MATCHERS).permitAll()
			.anyRequest().authenticated();
		http.addFilter(new JWTAuthenticationFilter(authenticationManager(), jwtUtil));
		http.addFilter(new JWTAuthorizationFilter(authenticationManager(), jwtUtil, userDetailsService));
		//nenhuma sessão será criada ou usada pelo Spring Security
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}
	
	// Determina qual é o userDetailsService utilizado e o algoritmo de codificação
	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
	}
	
	//CORS é uma especificação de uma tecnologia de navegadores que define meios para um servidor permitir que 
	//seus recursos sejam acessados por uma página web de um domínio diferente.
	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration().applyPermitDefaultValues();
		// Lista de métodos permitidos pelo CORS
		configuration.setAllowedMethods(Arrays.asList("POST", "GET", "PUT", "DELETE", "OPTIONS"));
		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}
	
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
