package com.gustavo.comicreviewapi.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.gustavo.comicreviewapi.builders.AuthenticationDtoBuilder;
import com.gustavo.comicreviewapi.builders.RefreshTokenBuilder;
import com.gustavo.comicreviewapi.builders.UserBuilder;
import com.gustavo.comicreviewapi.builders.UserNewDtoBuilder;
import com.gustavo.comicreviewapi.dtos.AuthenticationDTO;
import com.gustavo.comicreviewapi.dtos.LoginResponseDTO;
import com.gustavo.comicreviewapi.dtos.UserDTO;
import com.gustavo.comicreviewapi.dtos.UserNewDTO;
import com.gustavo.comicreviewapi.dtos.UserUpdateDTO;
import com.gustavo.comicreviewapi.entities.RefreshToken;
import com.gustavo.comicreviewapi.entities.User;
import com.gustavo.comicreviewapi.entities.enums.Profile;
import com.gustavo.comicreviewapi.repositories.UserRepository;
import com.gustavo.comicreviewapi.services.exceptions.AuthenticationErrorException;
import com.gustavo.comicreviewapi.services.exceptions.BusinessException;
import com.gustavo.comicreviewapi.services.exceptions.ObjectNotFoundException;
import com.gustavo.comicreviewapi.utils.JwtUtil;
import com.gustavo.comicreviewapi.utils.UserSS;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class UserServiceTest {
	
	UserService userService;
	
	@MockBean
	UserRepository userRepository;
	
	@MockBean
	BCryptPasswordEncoder pe;
	
	@MockBean
	AuthenticationManager authenticationManager;
	
	@MockBean
	RefreshTokenService refreshTokenService;
    
	@MockBean
    JwtUtil jwtUtil;
	
	@BeforeEach
	public void setUp() {
		this.userService= Mockito.spy(new UserService(userRepository, pe, authenticationManager, refreshTokenService, jwtUtil));
	}
	
	@Test
	@DisplayName("Must login and return the token")
	public void loginTest() {
		// Scenario
		String token = "eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJDb21pYyBSZXZpZXcgQXBpIiwic3ViIjoiSldUIFRva2VuIiwidXNlcm5hbWUiOiJndS5jcnV6MTdAaG90bWFpbC5jb20iLCJpYXQiOjE2OTUzODU3MDMsImV4cCI6MTY5NTQxNTcwM30.Ksery3op3UJ79HOflaJ9EEb9YI_Zv74UaQfL7MJ8sQXBYvBJ3ESEwcG4edCY5CjEKM1WxyvU3PSYJCc5JobWRg";
				
		AuthenticationDTO authenticationDTO = AuthenticationDtoBuilder.aAuthenticationDto().now();
		
		UserSS userDetails = new UserSS(1l, "gu.cruz17@hotmail.com", "Password1.", Set.of(Profile.USER));
		
		RefreshToken refreshToken = RefreshTokenBuilder.aRefreshToken().withId(1l).withUser(UserBuilder.aUser().withId(1l).now()).now();
		
		Authentication authentication = new UsernamePasswordAuthenticationToken(
				userDetails, userDetails.getPassword(), userDetails.getAuthorities());
				
		Mockito.when(authenticationManager.authenticate(Mockito.any(Authentication.class))).thenReturn(authentication);
		
		Mockito.when(jwtUtil.generateToken(Mockito.any(UserSS.class))).thenReturn(token);
		
		Mockito.when(refreshTokenService.createRefreshToken(Mockito.anyLong())).thenReturn(refreshToken);
		
		// Execution
		LoginResponseDTO loginResponseDto = userService.login(authenticationDTO);
		
		// Verification
		Assertions.assertThat(loginResponseDto.getToken()).isEqualTo(token);
		Assertions.assertThat(loginResponseDto.getId()).isEqualTo(userDetails.getId());
		Assertions.assertThat(loginResponseDto.getEmail()).isEqualTo(userDetails.getUsername());
		Assertions.assertThat(loginResponseDto.getRefreshToken()).isEqualTo(refreshToken.getToken());
		Assertions.assertThat(loginResponseDto.getType()).isEqualTo("Bearer");
		Assertions.assertThat(loginResponseDto.getProfiles().contains("ROLE_USER")).isTrue();
	}
	
	@Test
	@DisplayName("Should generate an Authentication Error when login is unsuccessful")
	public void incorrectLoginTest() {
		// Scenario
		AuthenticationDTO authenticationDTO = AuthenticationDtoBuilder.aAuthenticationDto().now();
						
		Mockito.when(authenticationManager.authenticate(Mockito.any(Authentication.class))).thenThrow(BadCredentialsException.class);
				
		// Execution and Verification
		Exception exception = assertThrows(AuthenticationErrorException.class, () -> {userService.login(authenticationDTO);});
		
		String expectedMessage = "Incorrect username or password!";
		String actualMessage = exception.getMessage();
		
		Assertions.assertThat(actualMessage).isEqualTo(expectedMessage);
	}
	
	@Test
	@DisplayName("Must save a user")
	public void saveUserTest() {
		// Scenario
		Long id = 2l;
		
		UserNewDTO newUser = UserNewDtoBuilder.aUserNewDTO().now();
		User savedUser = UserBuilder.aUser().withId(id).now();
		
		Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(savedUser);
		Mockito.when(pe.encode(newUser.getPassword())).thenReturn("$2a$10$EvfcP4blF9DqkG3we9Kbp.yP3Bl3haMpltdEcc0AG53jTAlwNE");
		
		// Execution
		UserDTO savedUserDto = userService.save(newUser);
		
		// Verification
		Assertions.assertThat(savedUserDto.getId()).isEqualTo(id);
		Assertions.assertThat(savedUserDto.getName()).isEqualTo("Gustavo Silva Cruz");
		Assertions.assertThat(savedUserDto.getBirthDate()).isEqualTo(LocalDate.of(1996, 10, 17));
		Assertions.assertThat(savedUserDto.getPhone()).isEqualTo("998123899");		
		Assertions.assertThat(savedUserDto.getEmail()).isEqualTo("gu.cruz17@hotmail.com");
		Assertions.assertThat(savedUserDto.getProfiles().contains(Profile.USER.getDescription())).isTrue();
		Mockito.verify(pe, Mockito.times(1)).encode(newUser.getPassword());
	}
	
	@Test
	@DisplayName("Should throw business error when trying to save a user with duplicate email")
	public void shouldNotSaveAUserWithDuplicatedEmail() {
		// Scenario
		UserNewDTO newUser = UserNewDtoBuilder.aUserNewDTO().now();
		
		Mockito.when(userRepository.existsByEmail(newUser.getEmail())).thenReturn(true);
		
		// Execution and Verification
		Exception exception = assertThrows(BusinessException.class, () -> {userService.save(newUser);});
	
		String expectedMessage = "E-mail already registered!";
		String actualMessage = exception.getMessage();
		
		Assertions.assertThat(actualMessage).isEqualTo(expectedMessage);
	}
	
	@Test
	@DisplayName("Must get one user per id")
	public void findByIdTest() {		
		// Scenario
		Long id = 2l;
		
		User user = UserBuilder.aUser().withId(id).now();
			
		Mockito.when(userRepository.findById(id)).thenReturn(Optional.of(user));
		
		// Execution
		User foundUser = userService.findById(id);
		
		// Verification
		Assertions.assertThat(foundUser.getId()).isEqualTo(id);
		Assertions.assertThat(foundUser.getName()).isEqualTo("Gustavo Silva Cruz");
		Assertions.assertThat(foundUser.getBirthDate()).isEqualTo(LocalDate.of(1996, 10, 17));
		Assertions.assertThat(foundUser.getPhone()).isEqualTo("998123899");		
		Assertions.assertThat(foundUser.getEmail()).isEqualTo("gu.cruz17@hotmail.com");
		Assertions.assertThat(foundUser.getProfiles().contains(Profile.USER)).isTrue();		
		
	}
	
	@Test
	@DisplayName("Should return error when trying to get a non-existent user")
	public void userNotFoundByIdTest() {
		// Scenario
		Long id = 1l;
				
		Mockito.when(userRepository.findById(id)).thenReturn(Optional.empty());
		
		// Execution and Verification
		Exception exception = assertThrows(ObjectNotFoundException.class, () -> {userService.findById(id);});
		
		String expectedMessage = "User not found! Id: " + id;
		String actualMessage = exception.getMessage();
		
		Assertions.assertThat(actualMessage).isEqualTo(expectedMessage);			
	}
	
	@Test
	@DisplayName("Must call findById method and return User to controller")
	public void findTest() {
		// Scenario
		long id = 2l;
		
		User user = UserBuilder.aUser().withId(id).now();
		
		Mockito.doReturn(user).when(userService).findById(id);
		
		// Execution
		UserDTO foundUser = userService.find(id);
		
		// Verification
		Assertions.assertThat(foundUser.getId()).isEqualTo(id);
		Assertions.assertThat(foundUser.getName()).isEqualTo("Gustavo Silva Cruz");
		Assertions.assertThat(foundUser.getBirthDate()).isEqualTo(LocalDate.of(1996, 10, 17));
		Assertions.assertThat(foundUser.getPhone()).isEqualTo("998123899");		
		Assertions.assertThat(foundUser.getEmail()).isEqualTo("gu.cruz17@hotmail.com");
		Assertions.assertThat(foundUser.getProfiles().contains(Profile.USER.getDescription())).isTrue();
	}
	
	@Test
	@DisplayName("Must update a user")
	public void updateUserTest() {
		// Possibilita simular invocações para chamadas de método estático. Este método retorna um objeto MockedStatic para nosso tipo, 
		// que é um objeto simulado com escopo definido.
		// Portanto, a variável de utilitários representa uma simulação com um escopo explícito de thread local, garantindo que a simulação 
		// estática permaneça temporária. 
		// https://www.baeldung.com/mockito-mock-static-methods		
		try(MockedStatic<UserService> mockedStatic = Mockito.mockStatic(UserService.class)) {
			// Scenario
			Long id = 2l;
			
			UserUpdateDTO userDto = new UserUpdateDTO("Fulano Cauê Calebe Jesus", LocalDate.of(1996, 10, 17), "988078805", "fulano-jesus87@hotmail.com.br");
			
			User foundUser = UserBuilder.aUser().withId(id).now();
			
			User updatedUser = new User(id, "Fulano Cauê Calebe Jesus", LocalDate.of(1996, 10, 17), "988078805", "fulano-jesus87@hotmail.com.br", "Password1.");
			
			UserSS userSS = new UserSS(id, foundUser.getEmail(), foundUser.getPassword(), foundUser.getProfiles());
			
			mockedStatic.when(UserService::authenticated).thenReturn(userSS);
			
			Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(updatedUser);
			Mockito.doReturn(foundUser).when(userService).findById(id);
			
			// Execution
			UserDTO updatedUserDto = userService.update(id, userDto);
			
			// Verification
			Assertions.assertThat(updatedUserDto.getId()).isEqualTo(id);
			Assertions.assertThat(updatedUserDto.getName()).isEqualTo("Fulano Cauê Calebe Jesus");
			Assertions.assertThat(updatedUserDto.getBirthDate()).isEqualTo(LocalDate.of(1996, 10, 17));
			Assertions.assertThat(updatedUserDto.getPhone()).isEqualTo("988078805");
			Assertions.assertThat(updatedUserDto.getEmail()).isEqualTo("fulano-jesus87@hotmail.com.br");	
			Assertions.assertThat(foundUser.getProfiles().contains(Profile.USER)).isTrue();
		}
	}
	
	@Test
	@DisplayName("Must delete a user")
	public void deleteUserTest() {
		// Scenario
		long id = 2l;
		
		User foundUser = UserBuilder.aUser().withId(id).now();
		
		Mockito.doReturn(foundUser).when(userService).findById(id);
		
		// Execution
		userService.delete(id);
		
		// Verification
		Mockito.verify(userRepository, Mockito.times(1)).delete(foundUser);
	}
		
	@Test
	@DisplayName("Must get birthday users of the day")
	public void findBirthdayUsersTest() {
		// Scenario		
		User user = UserBuilder.aUser().withId(1l).now();
		List<User> users = new ArrayList<User>();
		users.add(user);
		
		Mockito.doReturn(LocalDate.of(2022, 10, 17)).when(userService).getDate();
		Mockito.when(userRepository.findByDayAndMonthOfBirth(17, 10)).thenReturn(users);
		
		// Execution
		List<User> foundUsers = userService.findBirthdayUsers();
		
		// Verification
		Assertions.assertThat(foundUsers.size()).isEqualTo(1);
		Assertions.assertThat(foundUsers.get(0).getName()).isEqualTo("Gustavo Silva Cruz");
		Assertions.assertThat(foundUsers.get(0).getBirthDate()).isEqualTo(LocalDate.of(1996, 10, 17));
		Assertions.assertThat(foundUsers.get(0).getPhone()).isEqualTo("998123899");		
		Assertions.assertThat(foundUsers.get(0).getEmail()).isEqualTo("gu.cruz17@hotmail.com");
		Assertions.assertThat(foundUsers.get(0).getProfiles().contains(Profile.USER)).isTrue();	
	}
	
}
