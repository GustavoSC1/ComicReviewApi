package com.gustavo.comicreviewapi.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.gustavo.comicreviewapi.security.UserSS;
import com.gustavo.comicreviewapi.entities.User;
import com.gustavo.comicreviewapi.repositories.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	
	@Autowired
	private UserRepository userRepository;
	
	// Classe de serviço conforme contrato do Spring Security (implements UserDetailsService). 
	// Permite a busca do usuário pelo email.
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = userRepository.findByEmail(email);
		if (user == null) {
			throw new UsernameNotFoundException(email);
		}
		
		return new UserSS(user.getId(), user.getEmail(), user.getPassword(), user.getProfiles());
	}

}
