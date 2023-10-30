package com.gustavo.comicreviewapi.services;

import static org.junit.jupiter.api.Assertions.assertThrows;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.gustavo.comicreviewapi.builders.UserBuilder;
import com.gustavo.comicreviewapi.entities.User;
import com.gustavo.comicreviewapi.entities.enums.Profile;
import com.gustavo.comicreviewapi.repositories.UserRepository;
import com.gustavo.comicreviewapi.utils.UserSS;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class AuthorizationServiceTest {
	
	AuthorizationService authorizationService;
	
	@MockBean
	UserRepository userRepository;
	
	@BeforeEach
	public void setUp() {
		this.authorizationService = new AuthorizationService(userRepository);
	}
	
	@Test
	@DisplayName("Must get one UserDetails(UserSS00) per username(email)")
	public void loadUserByUsernameTest() {
		// Scenario
		String username = "gu.cruz17@hotmail.com";
		User foundUser = UserBuilder.aUser().withId(2l).now();
		
		Mockito.when(userRepository.findByEmail(username)).thenReturn(foundUser);
		
		// Execution
		UserSS foundUserDetails = (UserSS) authorizationService.loadUserByUsername(username);
		
		// Verification
		Assertions.assertThat(foundUserDetails.getUsername()).isEqualTo(username);
		Assertions.assertThat(foundUserDetails.getPassword()).isEqualTo("Password1.");
		Assertions.assertThat(foundUserDetails.hasRole(Profile.USER)).isTrue();
	}
	
	@Test
	@DisplayName("Should return error when trying to get a non-existent user")
	public void userNotFoundByEmailTest() {
		// Scenario
		String username = "gu.cruz17@hotmail.com";
		
		Mockito.when(userRepository.findByEmail(username)).thenReturn(null);
		
		// Execution and Verification
		Exception exception = assertThrows(UsernameNotFoundException.class, () -> {authorizationService.loadUserByUsername(username);});
	
		String expectedMessage = "gu.cruz17@hotmail.com";
		String actualMessage = exception.getMessage();
		
		Assertions.assertThat(actualMessage).isEqualTo(expectedMessage);		
	}

}
