package com.gustavo.comicreviewapi.services;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.gustavo.comicreviewapi.dtos.AuthenticationDTO;
import com.gustavo.comicreviewapi.dtos.UserDTO;
import com.gustavo.comicreviewapi.dtos.UserNewDTO;
import com.gustavo.comicreviewapi.dtos.UserUpdateDTO;
import com.gustavo.comicreviewapi.dtos.LoginResponseDTO;
import com.gustavo.comicreviewapi.entities.RefreshToken;
import com.gustavo.comicreviewapi.entities.User;
import com.gustavo.comicreviewapi.repositories.UserRepository;
import com.gustavo.comicreviewapi.services.exceptions.AuthenticationErrorException;
import com.gustavo.comicreviewapi.services.exceptions.AuthorizationException;
import com.gustavo.comicreviewapi.services.exceptions.BusinessException;
import com.gustavo.comicreviewapi.services.exceptions.ObjectNotFoundException;
import com.gustavo.comicreviewapi.utils.JwtUtil;
import com.gustavo.comicreviewapi.utils.UserSS;

@Service
public class UserService {
	
	private UserRepository userRepository;
	
	private PasswordEncoder pe;
	
    private AuthenticationManager authenticationManager;
    
    private RefreshTokenService refreshTokenService;
    
    private JwtUtil jwtUtil;
		
	public UserService(UserRepository userRepository, PasswordEncoder pe, AuthenticationManager authenticationManager, 
			RefreshTokenService refreshTokenService, JwtUtil jwtUtil) {
		this.userRepository = userRepository;
		this.pe = pe;
		this.authenticationManager = authenticationManager;
		this.refreshTokenService = refreshTokenService;
		this.jwtUtil = jwtUtil;
	}
	
	public LoginResponseDTO login(AuthenticationDTO authenticationDTO) {	
		Authentication authentication;
		
		UsernamePasswordAuthenticationToken usernamePassword = new UsernamePasswordAuthenticationToken(authenticationDTO.getEmail(), authenticationDTO.getPassword());
		try {
			authentication = authenticationManager.authenticate(usernamePassword);
		} catch (BadCredentialsException | InternalAuthenticationServiceException  e) {
	    	throw new AuthenticationErrorException("Incorrect username or password!");
	    } 
		
		UserSS userDetails = (UserSS) authentication.getPrincipal();
		
		String token = jwtUtil.generateToken(userDetails);
		
		List<String> profiles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
		        .collect(Collectors.toList());
		
		RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());
		
		return new LoginResponseDTO(token, refreshToken.getToken(), userDetails.getId(), userDetails.getUsername(), profiles);
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
		
		if(!id.equals(userAuthenticated.getId())) {
			throw new AuthorizationException("Access denied");
		}
				
		User user = findById(id);
		
		// Se o email já estiver cadastrado e não for nesse usuário, então não deve ser permitida a alteração
		if(!user.getEmail().equals(userDto.getEmail()) && userRepository.existsByEmail(userDto.getEmail())) {
			throw new BusinessException("E-mail already registered!");
		}
				
		user.setName(userDto.getName());
		user.setEmail(userDto.getEmail());
		user.setPhone(userDto.getPhone());
		user.setBirthDate(userDto.getBirthDate());
		
		user = userRepository.save(user);
		
		return new UserDTO(user);
	}
		
	public User findById(Long id) {

		Optional<User> userOptional = userRepository.findById(id);

		User user = userOptional.orElseThrow(() -> new ObjectNotFoundException("User not found! Id: " + id));

		return user;
	}
	
	public void delete(Long id) {
		User foundUser = findById(id);
				
		userRepository.delete(foundUser);
	}
	
	public List<User> findBirthdayUsers() {
		LocalDate date = getDate();
		List<User> foundUsers = userRepository.findByDayAndMonthOfBirth(date.getDayOfMonth(), date.getMonthValue());
		
		return foundUsers;
	}
	
	public static UserSS authenticated() {
		try {
			// Obtem o usuário atual logado
			return (UserSS) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		} catch (Exception e) {
			return null;
		}
	}
	
	public LocalDate getDate() {		
		return LocalDate.now();
	}
	
}
