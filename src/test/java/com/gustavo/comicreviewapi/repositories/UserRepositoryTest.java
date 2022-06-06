package com.gustavo.comicreviewapi.repositories;

import java.time.LocalDate;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.gustavo.comicreviewapi.entities.User;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
public class UserRepositoryTest {
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	TestEntityManager entityManager;
	
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
	
	@Test
	@DisplayName("Must check if there is a user with the email provided")
	public void existsByEmailTest() {
		// Scenario
		User newUser = new User(null,"Gustavo da Silva Cruz", LocalDate.of(1996, 10, 17), "998123456", "gu.cruz17@hotmail.com");
		entityManager.persist(newUser);
		
		// Execution
		boolean foundUser = userRepository.existsByEmail(newUser.getEmail());
		
		// Verification
		Assertions.assertThat(foundUser).isTrue();
	}
	
	@Test
	@DisplayName("Must get one user per id")
	public void findByIdTest() {
		// Scenario
		User user = new User(null,"Gustavo da Silva Cruz", LocalDate.of(1996, 10, 17), "998123456", "gu.cruz17@hotmail.com");
		entityManager.persist(user);
		
		// Execution
		Optional<User> foundUser = userRepository.findById(user.getId());
		
		// Verification
		Assertions.assertThat(foundUser.isPresent()).isTrue();
	}
	
}
