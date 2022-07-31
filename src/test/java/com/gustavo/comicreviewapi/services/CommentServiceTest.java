package com.gustavo.comicreviewapi.services;

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

import com.gustavo.comicreviewapi.builders.ReviewBuilder;
import com.gustavo.comicreviewapi.builders.UserBuilder;
import com.gustavo.comicreviewapi.dtos.CommentDTO;
import com.gustavo.comicreviewapi.dtos.CommentNewDTO;
import com.gustavo.comicreviewapi.dtos.CommentUpdateDTO;
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
		User user = UserBuilder.aUser().withId(id).now();
		Review review = ReviewBuilder.aReview().withId(id).now();
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
		Assertions.assertThat(foundComment.getTitle()).isEqualTo("Ótimo review");
		Assertions.assertThat(foundComment.getDate()).isEqualTo(LocalDateTime.of(2022, 11, 20, 22, 10));
		Assertions.assertThat(foundComment.getContent()).isEqualTo("Parabéns pelo review, com certeza irei adquirir essa HQ!");		
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
		Assertions.assertThat(foundComment.getTitle()).isEqualTo("Ótimo review");
		Assertions.assertThat(foundComment.getDate()).isEqualTo(LocalDateTime.of(2022, 11, 20, 22, 10));
		Assertions.assertThat(foundComment.getContent()).isEqualTo("Parabéns pelo review, com certeza irei adquirir essa HQ!");		
	}
	
	@Test
	@DisplayName("Must update a comment")
	public void updateCommentTest() {
		// Scenario
		Long id = 2l;
		
		CommentUpdateDTO commentDto = new CommentUpdateDTO("Review maneiro", "Review muito interessante, talvez um dia eu adquira essa a HQ!");
		
		Comment foundComment = createComment();
		foundComment.setId(id);
		
		Comment updatedComment = new Comment(id, "Review maneiro", LocalDateTime.of(2022, 11, 22, 20, 12), 
									"Review muito interessante, talvez um dia eu adquira essa a HQ!", null, null);
		
		Mockito.when(commentRepository.save(Mockito.any(Comment.class))).thenReturn(updatedComment);
		Mockito.doReturn(LocalDateTime.of(2022, 11, 22, 20, 12)).when(reviewService).getDateTime();
		Mockito.doReturn(foundComment).when(commentService).findById(id);
		
		// Execution
		CommentDTO updatedCommentDto = commentService.update(id, commentDto);
		
		// Verification
		Assertions.assertThat(updatedCommentDto.getId()).isEqualTo(id);
		Assertions.assertThat(updatedCommentDto.getTitle()).isEqualTo("Review maneiro");
		Assertions.assertThat(updatedCommentDto.getDate()).isEqualTo(LocalDateTime.of(2022, 11, 22, 20, 12));
		Assertions.assertThat(updatedCommentDto.getContent()).isEqualTo("Review muito interessante, talvez um dia eu adquira essa a HQ!");			
	}
		
	private CommentNewDTO createCommentNewDTO() {
		return new CommentNewDTO("Ótimo review", "Parabéns pelo review, com certeza irei adquirir essa HQ!", 1l, 1l);
	}
	
	private Comment createComment() {
		return new Comment(null, "Ótimo review", LocalDateTime.of(2022, 11, 20, 22, 10), "Parabéns pelo review, com certeza irei adquirir essa HQ!", null, null);
	}
	
}
