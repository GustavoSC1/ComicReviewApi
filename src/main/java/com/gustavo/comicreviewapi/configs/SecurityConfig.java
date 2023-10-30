package com.gustavo.comicreviewapi.configs;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.gustavo.comicreviewapi.filters.JwtRequestFilter;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class SecurityConfig {
	
	@Autowired
	JwtRequestFilter jwtRequestFilter;
	
	@Autowired
    private AccessDeniedHandler accessDeniedHandler;
	
	@Autowired
	private AuthenticationEntryPoint authenticationEntryPoint;
		
	@Bean
	public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http, HandlerMappingIntrospector introspector) throws Exception {
		MvcRequestMatcher.Builder mvcMatcherBuilder = new MvcRequestMatcher.Builder(introspector);
				
		http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
		.cors(cors -> cors.configurationSource(new CorsConfigurationSource() {
			@Override
			public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
				CorsConfiguration config = new CorsConfiguration();
				config.setAllowedOrigins(Collections.singletonList("*"));
				config.setAllowedMethods(Collections.singletonList("*"));
				config.setAllowCredentials(true);
				config.setAllowedHeaders(Collections.singletonList("*"));
				config.setExposedHeaders(Arrays.asList("Authorization"));
				config.setMaxAge(3600L);
				return config;
			}
		})).csrf(csrf -> csrf.disable())
        .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
        .authorizeHttpRequests(requests -> requests		
        		
				.requestMatchers(mvcMatcherBuilder.pattern(HttpMethod.POST, "/users/**")).permitAll()
				.requestMatchers(mvcMatcherBuilder.pattern(HttpMethod.GET, "/comics/**")).permitAll()
				.requestMatchers(mvcMatcherBuilder.pattern(HttpMethod.GET, "/reviews/**")).permitAll()
				.requestMatchers(mvcMatcherBuilder.pattern(HttpMethod.GET, "/comments/**")).permitAll()
				.requestMatchers(mvcMatcherBuilder.pattern(HttpMethod.GET, "/users/**")).permitAll()
				.requestMatchers(mvcMatcherBuilder.pattern(HttpMethod.POST, "/auth/**")).permitAll()
				.requestMatchers(mvcMatcherBuilder.pattern("/h2-console/**")).permitAll()
				.requestMatchers(mvcMatcherBuilder.pattern("/v3/api-docs.yaml")).permitAll()
				.requestMatchers(mvcMatcherBuilder.pattern("/v3/api-docs/**")).permitAll()
				.requestMatchers(mvcMatcherBuilder.pattern("/swagger-ui/**")).permitAll()
				.requestMatchers(mvcMatcherBuilder.pattern("/swagger-ui.html")).permitAll()
        		.anyRequest().authenticated())
        		.exceptionHandling(exception -> 
        			exception.authenticationEntryPoint(authenticationEntryPoint)
        			.accessDeniedHandler(accessDeniedHandler));
		
		
		//https://stackoverflow.com/questions/65894268/how-does-headers-frameoptions-disable-work
		//X-Frame-Options: https://developer.mozilla.org/pt-BR/docs/Web/HTTP/Headers/X-Frame-Options
				http.headers(cabeçalhos -> cabeçalhos
		         .frameOptions(frameOptions -> frameOptions
		                 .sameOrigin()
		             )
		         );
        		return http.build();		
	}
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
