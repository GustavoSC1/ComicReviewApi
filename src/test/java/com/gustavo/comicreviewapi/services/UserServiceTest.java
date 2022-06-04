package com.gustavo.comicreviewapi.services;

import java.time.LocalDate;

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

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class UserServiceTest {
	
	UserService userService;
	
	@MockBean
	UserRepository userRepository;
	
	@BeforeEach
	public void setUp() {
		this.userService= new UserService(userRepository);
	}
	
	@Test
	@DisplayName("Must save a user")
	public void saveUserTest() {
		// Scenario
		Long id = 2l;
		UserNewDTO newUser = new UserNewDTO("Gustavo da Silva Cruz", LocalDate.of(1996, 10, 17), "998123456", "gu.cruz17@hotmail.com");
		User savedUser = new User(null,"Gustavo da Silva Cruz", LocalDate.of(1996, 10, 17), "998123456", "gu.cruz17@hotmail.com");
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
	
}
