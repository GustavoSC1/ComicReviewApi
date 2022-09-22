package com.gustavo.comicreviewapi.services;

import java.util.Optional;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.gustavo.comicreviewapi.dtos.UserDTO;
import com.gustavo.comicreviewapi.dtos.UserNewDTO;
import com.gustavo.comicreviewapi.dtos.UserUpdateDTO;
import com.gustavo.comicreviewapi.entities.User;
import com.gustavo.comicreviewapi.repositories.UserRepository;
import com.gustavo.comicreviewapi.security.UserSS;
import com.gustavo.comicreviewapi.services.exceptions.AuthorizationException;
import com.gustavo.comicreviewapi.services.exceptions.BusinessException;
import com.gustavo.comicreviewapi.services.exceptions.ObjectNotFoundException;

@Service
public class UserService {
	
	private UserRepository userRepository;
	
	private BCryptPasswordEncoder pe;
	
	public UserService(UserRepository userRepository, BCryptPasswordEncoder pe) {
		this.userRepository = userRepository;
		this.pe = pe;
	}
	
	public UserDTO save(UserNewDTO userDto) {
		
		if(userRepository.existsByEmail(userDto.getEmail())) {
			throw new BusinessException("E-mail already registered!");
		}
				
		User user = new User(null, userDto.getName(), userDto.getBirthDate(), userDto.getPhone(), userDto.getEmail(), pe.encode(userDto.getPassword()));
		
		user = userRepository.save(user);
		
		return new UserDTO(user);
	}
	
	public UserDTO find(Long id) {		
		User user = findById(id);
		
		return new UserDTO(user);
	}
	
	public UserDTO update(Long id, UserUpdateDTO userDto) {
			
		UserSS userAuthenticated = authenticated();		
		if(userAuthenticated==null || !id.equals(userAuthenticated.getId())) {
			throw new AuthorizationException("Access denied");
		}
		
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
	
	public void delete(Long id) {
		User foundUser = findById(id);
				
		userRepository.delete(foundUser);
	}
	
	public static UserSS authenticated() {
		try {
			// Obtem o usuário atual logado
			return (UserSS) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		} catch (Exception e) {
			return null;
		}
	}
	
}
