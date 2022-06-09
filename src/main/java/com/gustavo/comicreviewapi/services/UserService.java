package com.gustavo.comicreviewapi.services;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.gustavo.comicreviewapi.dtos.UserDTO;
import com.gustavo.comicreviewapi.dtos.UserNewDTO;
import com.gustavo.comicreviewapi.entities.User;
import com.gustavo.comicreviewapi.repositories.UserRepository;
import com.gustavo.comicreviewapi.services.exceptions.BusinessException;
import com.gustavo.comicreviewapi.services.exceptions.ObjectNotFoundException;

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
	
	public UserDTO find(Long id) {
		User user = findById(id);
		
		return new UserDTO(user);
	}
	
	public UserDTO update(Long id, UserDTO userDto) {
		User user = findById(id);
		
		user.setName(userDto.getName());
		user.setEmail(userDto.getEmail());
		user.setPhone(userDto.getPhone());
		user.setBirthDate(userDto.getBirthDate());
		
		user = userRepository.save(user);
		
		return new UserDTO(user);
	}
	
	public User findById(Long id) {
		Optional<User> userOptional = userRepository.findById(id);
		User user = userOptional.orElseThrow(() -> new ObjectNotFoundException("Object not found! Id: " + id + ", Type: " + User.class.getName()));
	
		return user;
	}
	
}
