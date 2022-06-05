package com.gustavo.comicreviewapi.services;

import org.springframework.stereotype.Service;

import com.gustavo.comicreviewapi.dtos.UserDTO;
import com.gustavo.comicreviewapi.dtos.UserNewDTO;
import com.gustavo.comicreviewapi.entities.User;
import com.gustavo.comicreviewapi.repositories.UserRepository;
import com.gustavo.comicreviewapi.services.exceptions.BusinessException;

@Service
public class UserService {
	
	private UserRepository userRepository;
	
	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	public UserDTO save(UserNewDTO userDto) {
		
		if(userRepository.existsByEmail(userDto.getEmail())) {
			throw new BusinessException("E-mail already registered!");
		}
		
		User user = new User(null, userDto.getName(), userDto.getBirthDate(), userDto.getPhone(), userDto.getEmail());
		
		user = userRepository.save(user);
		
		return new UserDTO(user);
	}

}
