package com.gustavo.comicreviewapi.repositories;

import java.time.LocalDateTime;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.gustavo.comicreviewapi.entities.Comment;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
public class CommentRepositoryTest {
	
	@Autowired
	CommentRepository commentRepository;
	
	@Test
	@DisplayName("Must save a comment")
	public void saveCommentTest() {
		// Scenario
		Comment newComment = new Comment(null, "Ótimo review", LocalDateTime.now(), "Parabéns pelo review, com certeza irei adquirir essa HQ!", null, null);
	
		// Execution
		Comment savedComment = commentRepository.save(newComment);
		
		// Verification
		Assertions.assertThat(savedComment.getId()).isEqualTo(1l);
	}

}
