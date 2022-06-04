package com.gustavo.comicreviewapi.repositories;

import java.time.LocalDate;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.gustavo.comicreviewapi.entities.User;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
public class UserRepositoryTest {
	
	@Autowired
	UserRepository userRepository;
	
	@Test
	@DisplayName("Must save a user")
	public void saveUserTest() {
		// Scenario
		User newUser = new User(null,"Gustavo da Silva Cruz", LocalDate.of(1996, 10, 17), "998123456", "gu.cruz17@hotmail.com");
		
		// Execution
		User savedUser = userRepository.save(newUser);
		
		// Verification
		Assertions.assertThat(savedUser.getId()).isNotNull();
	}
	
}
