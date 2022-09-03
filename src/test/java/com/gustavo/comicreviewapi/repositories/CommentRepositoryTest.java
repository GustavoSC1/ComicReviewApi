package com.gustavo.comicreviewapi.repositories;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.gustavo.comicreviewapi.builders.CommentBuilder;
import com.gustavo.comicreviewapi.builders.ReviewBuilder;
import com.gustavo.comicreviewapi.entities.Comment;
import com.gustavo.comicreviewapi.entities.Review;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
public class CommentRepositoryTest {
	
	@Autowired
	CommentRepository commentRepository;
	
	@Autowired
	TestEntityManager entityManager;
	
	@Test
	@DisplayName("Must save a comment")
	public void saveCommentTest() {
		// Scenario
		Comment newComment = CommentBuilder.aComment().now();
	
		// Execution
		Comment savedComment = commentRepository.save(newComment);
		
		// Verification
		Assertions.assertThat(savedComment.getId()).isNotNull();
	}
	
	@Test
	@DisplayName("Must get one comment per id")
	public void findByIdTest() {
		// Scenario
		Comment comment = CommentBuilder.aComment().now();
		entityManager.persist(comment);
		
		// Execution
		Optional<Comment> foundComment = commentRepository.findById(comment.getId());
		
		// Verification
		Assertions.assertThat(foundComment.isPresent()).isTrue();		
	}
	
	@Test
	@DisplayName("Must filter comments by review")
	public void findCommentsByReviewTest() {
		// Scenario
		Long id = 1l;
		
		Review review = ReviewBuilder.aReview().now();
		review = entityManager.persist(review);
		
		PageRequest pageRequest = PageRequest.of(0, 24, Direction.valueOf("DESC"), "date");
						
		Comment comment = CommentBuilder.aComment().withReview(review).now();
		
		entityManager.persist(comment);
		
		// Execution
		Page<Comment> foundComments = commentRepository.findCommentsByReview(id, pageRequest);
		
		// Verification
		Assertions.assertThat(foundComments.getNumberOfElements()).isEqualTo(1);
		Assertions.assertThat(foundComments.getTotalElements()).isEqualTo(1);
		Assertions.assertThat(foundComments.getTotalPages()).isEqualTo(1);
	}

}
