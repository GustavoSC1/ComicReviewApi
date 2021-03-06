package com.gustavo.comicreviewapi.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

import com.gustavo.comicreviewapi.dtos.CommentDTO;
import com.gustavo.comicreviewapi.dtos.CommentNewDTO;
import com.gustavo.comicreviewapi.entities.Comment;
import com.gustavo.comicreviewapi.entities.Review;
import com.gustavo.comicreviewapi.entities.User;
import com.gustavo.comicreviewapi.repositories.CommentRepository;
import com.gustavo.comicreviewapi.services.exceptions.ObjectNotFoundException;

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
		this.commentService = Mockito.spy(new CommentService(commentRepository, userService, reviewService));
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
		Assertions.assertThat(savedCommentDto.getTitle()).isEqualTo("??timo review");
		Assertions.assertThat(savedCommentDto.getDate()).isEqualTo(LocalDateTime.of(2022, 11, 20, 22, 10));
		Assertions.assertThat(savedCommentDto.getContent()).isEqualTo("Parab??ns pelo review, com certeza irei adquirir essa HQ!");
	}
	
	@Test
	@DisplayName("Must get one comment per id")
	public void findByIdTest() {
		// Scenario
		Long id = 2l;
		
		Comment comment = createComment();
		comment.setId(id);
		
		Mockito.when(commentRepository.findById(id)).thenReturn(Optional.of(comment));
		
		// Execution
		Comment foundComment = commentService.findById(id);
		
		// Verification
		Assertions.assertThat(foundComment.getId()).isEqualTo(id);
		Assertions.assertThat(foundComment.getTitle()).isEqualTo("??timo review");
		Assertions.assertThat(foundComment.getDate()).isEqualTo(LocalDateTime.of(2022, 11, 20, 22, 10));
		Assertions.assertThat(foundComment.getContent()).isEqualTo("Parab??ns pelo review, com certeza irei adquirir essa HQ!");		
	}
	
	@Test
	@DisplayName("Should return error when trying to get a non-existent comment")
	public void commentNotFoundByIdTest() {
		// Scenario
		Long id = 1l;
		Mockito.when(commentRepository.findById(id)).thenReturn(Optional.empty());
		
		// Execution and Verification
		Exception exception = assertThrows(ObjectNotFoundException.class, () -> {commentService.findById(id);});
	
		String expectedMessage = "Object not found! Id: " + id + ", Type: " + Comment.class.getName();
		String actualMessage = exception.getMessage();
		
		Assertions.assertThat(actualMessage).isEqualTo(expectedMessage);		
	}
	
	@Test
	@DisplayName("Must call findById method and return Comment to controller")
	public void findTest() {
		// Scenario
		long id = 2l;
		
		Comment comment = createComment();
		comment.setId(id);
		
		Mockito.doReturn(comment).when(commentService).findById(id);
		
		// Execution
		CommentDTO foundComment = commentService.find(id);
		
		// Verification
		Assertions.assertThat(foundComment.getId()).isEqualTo(id);
		Assertions.assertThat(foundComment.getTitle()).isEqualTo("??timo review");
		Assertions.assertThat(foundComment.getDate()).isEqualTo(LocalDateTime.of(2022, 11, 20, 22, 10));
		Assertions.assertThat(foundComment.getContent()).isEqualTo("Parab??ns pelo review, com certeza irei adquirir essa HQ!");		
	}
	
	private Review createReview() {					
		Review review = new Review(null,"??tima hist??ria", LocalDateTime.of(2022, 11, 20, 21, 50), "A HQ mostra o Homem-Aranha em sua ess??ncia: "
				+ "cheio de problemas, tentando fazer o que ?? certo enquanto luta para manter sua identidade secreta em "
				+ "segredo, com um turbilh??o de coisas acontecendo ao mesmo tempo, na escola, no namoro, no trabalho, em "
				+ "fam??lia. ?? maravilhoso ver a determina????o do her??i e imposs??vel n??o se identificar com ele, n??o se agoniar "
				+ "com seus problemas e torcer pela sua vit??ria. ?? tudo que se espera de uma boa aventura de super-her??is e "
				+ "um roteiro perfeito para um filme do Aracn??deo.", null, null);
		
		return review;
	}
	
	private CommentNewDTO createCommentNewDTO() {
		return new CommentNewDTO("??timo review", "Parab??ns pelo review, com certeza irei adquirir essa HQ!", 1l, 1l);
	}
	
	private User createUser() {
		return new User(null,"Gustavo Silva Cruz", LocalDate.of(1996, 10, 17), "998123899", "gu.cruz17@hotmail.com");
	}
	
	private Comment createComment() {
		return new Comment(null, "??timo review", LocalDateTime.of(2022, 11, 20, 22, 10), "Parab??ns pelo review, com certeza irei adquirir essa HQ!", null, null);
	}
	
}
