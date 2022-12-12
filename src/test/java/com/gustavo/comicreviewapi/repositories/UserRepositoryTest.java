package com.gustavo.comicreviewapi.repositories;

import java.time.LocalDate;
import java.util.List;
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

import com.gustavo.comicreviewapi.builders.ComicBuilder;
import com.gustavo.comicreviewapi.builders.CommentBuilder;
import com.gustavo.comicreviewapi.builders.ReviewBuilder;
import com.gustavo.comicreviewapi.builders.UserBuilder;
import com.gustavo.comicreviewapi.entities.Author;
import com.gustavo.comicreviewapi.entities.Character;
import com.gustavo.comicreviewapi.entities.Comic;
import com.gustavo.comicreviewapi.entities.Comment;
import com.gustavo.comicreviewapi.entities.Review;
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
		User newUser = UserBuilder.aUser().now();
		
		// Execution
		User savedUser = userRepository.save(newUser);
		
		// Verification
		Assertions.assertThat(savedUser.getId()).isNotNull();
	}
	
	@Test
	@DisplayName("Must check if there is a user with the email provided")
	public void existsByEmailTest() {
		// Scenario
		User newUser = UserBuilder.aUser().now();
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
		User user = UserBuilder.aUser().now();
		entityManager.persist(user);
		
		// Execution
		Optional<User> foundUser = userRepository.findById(user.getId());
		
		// Verification
		Assertions.assertThat(foundUser.isPresent()).isTrue();
	}
	
	@Test
	@DisplayName("Must delete a user")
	public void deleteUserTest() {
		// Scenario
		User user = UserBuilder.aUser().now();
		user = entityManager.persist(user);
		
		Comic comic = ComicBuilder.aComic().withAuthorsList(new Author(null, "Stefan Petrucha"))
				.withCharactersList(new Character(null, "Homem Aranha")).withId(1l).now();
		comic = entityManager.persist(comic);
		
		Review review = ReviewBuilder.aReview().withComic(comic).withUser(user).now();
		review = entityManager.persist(review);
		
		Comment comment = CommentBuilder.aComment().withReview(review).withUser(user).now();		
		comment = entityManager.persist(comment);
		
		User foundUser = entityManager.find(User.class, user.getId());
		
		// Execution
		userRepository.delete(foundUser);
		
		User deletedUser = entityManager.find(User.class, user.getId());
		Review foundReview = entityManager.find(Review.class, review.getId());
		Comment foundComment = entityManager.find(Comment.class, comment.getId());
		
		// Verification
		Assertions.assertThat(deletedUser).isNull();
		Assertions.assertThat(foundReview).isNotNull(); // Os reviews do usuário não devem ser apagados
		Assertions.assertThat(foundComment).isNotNull(); // Os comentários do usuário não devem ser apagados
	}
	
	@Test
	@DisplayName("Must filter user per day and month of birth")
	public void findByDayAndMonthOfBirthTest() {
		// Scenario
		User user = UserBuilder.aUser().now();
		entityManager.persist(user);
		
		User user2 = UserBuilder.aUser().now();
		user2.setBirthDate(LocalDate.of(1996, 10, 18));
		entityManager.persist(user2);
		
		// Execution
		List<User> foundUsers = userRepository.findByDayAndMonthOfBirth(17, 10);
		
		// Verification
		Assertions.assertThat(foundUsers).hasSize(1).contains(user);		
	}
	
}
