package com.gustavo.comicreviewapi.services;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.gustavo.comicreviewapi.entities.User;
import com.gustavo.comicreviewapi.entities.enums.Profile;
import com.gustavo.comicreviewapi.repositories.UserRepository;

@Service
public class DBService {
	
	@Autowired
	private BCryptPasswordEncoder pe;
	
	@Autowired
	private UserRepository userRepository;
	
	public void instantiateTestDataBase() {
		
		User user = new User(null, "João Pedro", LocalDate.of(1995, 11, 21), "998123899", "joao_admin@hotmail.com", pe.encode("Admin123"));
		user.addProfile(Profile.ADMIN);
		
		userRepository.save(user);		
	}

}
