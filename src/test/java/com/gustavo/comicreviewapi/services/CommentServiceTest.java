package com.gustavo.comicreviewapi.services;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.gustavo.comicreviewapi.dtos.CommentDTO;
import com.gustavo.comicreviewapi.dtos.CommentNewDTO;
import com.gustavo.comicreviewapi.entities.Comment;
import com.gustavo.comicreviewapi.entities.Review;
import com.gustavo.comicreviewapi.entities.User;
import com.gustavo.comicreviewapi.repositories.CommentRepository;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class CommentServiceTest {
	
	CommentService commentService;
	
	@MockBean
	CommentRepository commentRepository;
	
	@MockBean
	UserService userService;
	
	@MockBean
	ReviewService reviewService;
	
	@BeforeEach
	public void setUp() {
		this.commentService = new CommentService(commentRepository, userService, reviewService);
	}
	
	@Test
	@DisplayName("Must save a comment")
	public void saveCommentTest() {
		// Scenario
		Long id = 1l;
		
		CommentNewDTO newComment = createCommentNewDTO();
		User user = createUser();
		user.setId(id);
		Review review = createReview();
		review.setId(id);
		Comment savedComment = createComment();
		savedComment.setId(id);
		
		Mockito.when(userService.findById(id)).thenReturn(user);
		Mockito.when(reviewService.findById(id)).thenReturn(review);
		Mockito.doReturn(LocalDateTime.of(2022, 11, 20, 22, 10)).when(reviewService).getDateTime();
		Mockito.when(commentRepository.save(Mockito.any(Comment.class))).thenReturn(savedComment);
		
		// Execution
		CommentDTO savedCommentDto = commentService.save(newComment);
		
		// Verification
		Assertions.assertThat(savedCommentDto.getId()).isEqualTo(id);
		Assertions.assertThat(savedCommentDto.getTitle()).isEqualTo("Ótimo review");
		Assertions.assertThat(savedCommentDto.getDate()).isEqualTo(LocalDateTime.of(2022, 11, 20, 22, 10));
		Assertions.assertThat(savedCommentDto.getContent()).isEqualTo("Parabéns pelo review, com certeza irei adquirir essa HQ!");
	}
	
	private Review createReview() {					
		Review review = new Review(null,"Ótima história", LocalDateTime.of(2022, 11, 20, 21, 50), "A HQ mostra o Homem-Aranha em sua essência: "
				+ "cheio de problemas, tentando fazer o que é certo enquanto luta para manter sua identidade secreta em "
				+ "segredo, com um turbilhão de coisas acontecendo ao mesmo tempo, na escola, no namoro, no trabalho, em "
				+ "família. É maravilhoso ver a determinação do herói e impossível não se identificar com ele, não se agoniar "
				+ "com seus problemas e torcer pela sua vitória. É tudo que se espera de uma boa aventura de super-heróis e "
				+ "um roteiro perfeito para um filme do Aracnídeo.", null, null);
		
		return review;
	}
	
	private CommentNewDTO createCommentNewDTO() {
		return new CommentNewDTO("Ótimo review", "Parabéns pelo review, com certeza irei adquirir essa HQ!", 1l, 1l);
	}
	
	private User createUser() {
		return new User(null,"Gustavo Silva Cruz", LocalDate.of(1996, 10, 17), "998123899", "gu.cruz17@hotmail.com");
	}
	
	private Comment createComment() {
		return new Comment(null, "Ótimo review", LocalDateTime.of(2022, 11, 20, 22, 10), "Parabéns pelo review, com certeza irei adquirir essa HQ!", null, null);
	}
	
}
