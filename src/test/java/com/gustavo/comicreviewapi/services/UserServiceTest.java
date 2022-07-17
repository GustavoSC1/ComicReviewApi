package com.gustavo.comicreviewapi.services;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.gustavo.comicreviewapi.dtos.UserDTO;
import com.gustavo.comicreviewapi.dtos.UserNewDTO;
import com.gustavo.comicreviewapi.entities.User;
import com.gustavo.comicreviewapi.repositories.UserRepository;
import com.gustavo.comicreviewapi.services.exceptions.BusinessException;
import com.gustavo.comicreviewapi.services.exceptions.ObjectNotFoundException;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class UserServiceTest {
	
	UserService userService;
	
	@MockBean
	UserRepository userRepository;
	
	@BeforeEach
	public void setUp() {
		this.userService= Mockito.spy(new UserService(userRepository));
	}
	
	@Test
	@DisplayName("Must save a user")
	public void saveUserTest() {
		// Scenario
		Long id = 2l;
		
		UserNewDTO newUser = createUserNewDTO();
		User savedUser = createUser();
		savedUser.setId(id);
		
		Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(savedUser);
		
		// Execution
		UserDTO savedUserDto = userService.save(newUser);
		
		// Verification
		Assertions.assertThat(savedUserDto.getId()).isEqualTo(id);
		Assertions.assertThat(savedUserDto.getName()).isEqualTo("Gustavo da Silva Cruz");
		Assertions.assertThat(savedUserDto.getBirthDate()).isEqualTo(LocalDate.of(1996, 10, 17));
		Assertions.assertThat(savedUserDto.getPhone()).isEqualTo("998123456");		
		Assertions.assertThat(savedUserDto.getEmail()).isEqualTo("gu.cruz17@hotmail.com");
	}
	
	@Test
	@DisplayName("Should throw business error when trying to save a user with duplicate email")
	public void shouldNotSaveAUserWithDuplicatedEmail() {
		// Scenario
		UserNewDTO newUser = createUserNewDTO();
		
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
		
		User user = createUser();
		user.setId(id);
		
		Mockito.when(userRepository.findById(id)).thenReturn(Optional.of(user));
		
		// Execution
		User foundUser = userService.findById(id);
		
		// Verification
		Assertions.assertThat(foundUser.getId()).isEqualTo(id);
		Assertions.assertThat(foundUser.getName()).isEqualTo("Gustavo da Silva Cruz");
		Assertions.assertThat(foundUser.getBirthDate()).isEqualTo(LocalDate.of(1996, 10, 17));
		Assertions.assertThat(foundUser.getPhone()).isEqualTo("998123456");		
		Assertions.assertThat(foundUser.getEmail()).isEqualTo("gu.cruz17@hotmail.com");
	}
	
	@Test
	@DisplayName("Should return error when trying to get a non-existent user")
	public void userNotFoundByIdTest() {
		// Scenario
		Long id = 1l;
		Mockito.when(userRepository.findById(id)).thenReturn(Optional.empty());
		
		// Execution and Verification
		Exception exception = assertThrows(ObjectNotFoundException.class, () -> {userService.findById(id);});
		
		String expectedMessage = "Object not found! Id: " + id + ", Type: " + User.class.getName();
		String actualMessage = exception.getMessage();
		
		Assertions.assertThat(actualMessage).isEqualTo(expectedMessage);	
	}
	
	@Test
	@DisplayName("Must call findById method and return User to controller")
	public void findTest() {
		// Scenario
		long id = 2l;
		
		User user = createUser();
		user.setId(id);
		
		Mockito.doReturn(user).when(userService).findById(id);
		
		// Execution
		UserDTO foundUser = userService.find(id);
		
		// Verification
		Assertions.assertThat(foundUser.getId()).isEqualTo(id);
		Assertions.assertThat(foundUser.getName()).isEqualTo("Gustavo da Silva Cruz");
		Assertions.assertThat(foundUser.getBirthDate()).isEqualTo(LocalDate.of(1996, 10, 17));
		Assertions.assertThat(foundUser.getPhone()).isEqualTo("998123456");		
		Assertions.assertThat(foundUser.getEmail()).isEqualTo("gu.cruz17@hotmail.com");
	}
	
	@Test
	@DisplayName("Must update a user")
	public void updateUserTest() {
		// Scenario
		Long id = 2l;
		
		UserDTO userDto = new UserDTO(id, "Daniel Cauê Calebe Jesus", LocalDate.of(1996, 10, 17), "988078805", "daniel-jesus87@cvc.com.br");
		
		User foundUser = createUser();
		foundUser.setId(id);
		
		User updatedUser = new User(id, "Daniel Cauê Calebe Jesus", LocalDate.of(1996, 10, 17), "988078805", "daniel-jesus87@cvc.com.br");
				
		Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(updatedUser);
		Mockito.doReturn(foundUser).when(userService).findById(id);
		
		// Execution
		UserDTO updatedUserDto = userService.update(id, userDto);
		
		// Verification
		Assertions.assertThat(updatedUserDto.getId()).isEqualTo(id);
		Assertions.assertThat(updatedUserDto.getName()).isEqualTo("Daniel Cauê Calebe Jesus");
		Assertions.assertThat(updatedUserDto.getBirthDate()).isEqualTo(LocalDate.of(1996, 10, 17));
		Assertions.assertThat(updatedUserDto.getPhone()).isEqualTo("988078805");
		Assertions.assertThat(updatedUserDto.getEmail()).isEqualTo("daniel-jesus87@cvc.com.br");		
	}
	

	private User createUser() {
		return new User(null,"Gustavo da Silva Cruz", LocalDate.of(1996, 10, 17), "998123456", "gu.cruz17@hotmail.com");
	}
	
	private UserNewDTO createUserNewDTO() {
		return new UserNewDTO("Gustavo da Silva Cruz", LocalDate.of(1996, 10, 17), "998123456", "gu.cruz17@hotmail.com");
	}
	
}
