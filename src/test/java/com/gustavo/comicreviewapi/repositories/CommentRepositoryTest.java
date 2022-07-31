package com.gustavo.comicreviewapi.repositories;

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

import com.gustavo.comicreviewapi.builders.CommentBuilder;
import com.gustavo.comicreviewapi.entities.Comment;

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
		Assertions.assertThat(savedComment.getId()).isEqualTo(1l);
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

}
