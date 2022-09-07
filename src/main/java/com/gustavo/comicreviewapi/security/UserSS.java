package com.gustavo.comicreviewapi.security;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.gustavo.comicreviewapi.entities.enums.Profile;

// Classe de usuário conforme contrato do Spring Security (implements UserDetails)
public class UserSS implements UserDetails {
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String email;
	private String password;
	private Collection<? extends GrantedAuthority> authorities;
	
	public UserSS() {		
	}
	
	public UserSS(Long id, String email, String password, Set<Profile> profiles) {
		super();
		this.id = id;
		this.email = email;
		this.password = password;
		this.authorities = profiles.stream().map(x -> new SimpleGrantedAuthority(x.getDescription())).collect(Collectors.toList());
	}
	
	public Long getId() {
		return id;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {		
		return authorities;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return email;
	}
	
	//A conta não está expirada
	@Override
	public boolean isAccountNonExpired() {		
		return true;
	}
	
	//A conta não está bloqueada
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}
	
	//As credencias não estão expiradas
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}
	
	//O usuário esta habilitado/ativo
	@Override
	public boolean isEnabled() {
		return true;
	}

}
