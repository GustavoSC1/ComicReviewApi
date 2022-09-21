package com.gustavo.comicreviewapi.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.gustavo.comicreviewapi.builders.CommentBuilder;
import com.gustavo.comicreviewapi.builders.CommentNewDtoBuilder;
import com.gustavo.comicreviewapi.builders.ReviewBuilder;
import com.gustavo.comicreviewapi.builders.UserBuilder;
import com.gustavo.comicreviewapi.dtos.CommentDTO;
import com.gustavo.comicreviewapi.dtos.CommentNewDTO;
import com.gustavo.comicreviewapi.dtos.CommentUpdateDTO;
import com.gustavo.comicreviewapi.entities.Comment;
import com.gustavo.comicreviewapi.entities.Review;
import com.gustavo.comicreviewapi.entities.User;
import com.gustavo.comicreviewapi.repositories.CommentRepository;
import com.gustavo.comicreviewapi.security.UserSS;
import com.gustavo.comicreviewapi.services.exceptions.ObjectNotFoundException;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class CommentServiceTest {
	
	CommentService commentService;
	
	@MockBean
	CommentRepository commentRepository;
	
	@MockBean
	ReviewService reviewService;
	
	@BeforeEach
	public void setUp() {
		this.commentService = Mockito.spy(new CommentService(commentRepository, reviewService));
	}
	
	@Test
	@DisplayName("Must save a comment")
	public void saveCommentTest() {
		try(MockedStatic<UserService> mockedStatic = Mockito.mockStatic(UserService.class)) {
			// Scenario
			Long id = 1l;
			
			CommentNewDTO newComment = CommentNewDtoBuilder.aCommentNewDTO().now();			
			Review review = ReviewBuilder.aReview().withId(id).now();
			Comment savedComment =  CommentBuilder.aComment().withId(id).now();
			User user = UserBuilder.aUser().withId(id).now();
			
			UserSS userSS = new UserSS(id, user.getEmail(), user.getPassword(), user.getProfiles());
			
			mockedStatic.when(UserService::authenticated).thenReturn(userSS);
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
	}
	
	@Test
	@DisplayName("Must get one comment per id")
	public void findByIdTest() {
		// Scenario
		Long id = 2l;
		
		Comment comment = CommentBuilder.aComment().withId(id).now();
		
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
		
		Comment comment = CommentBuilder.aComment().withId(id).now();
		
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
		try(MockedStatic<UserService> mockedStatic = Mockito.mockStatic(UserService.class)) {
			// Scenario
			Long id = 2l;
			
			CommentUpdateDTO commentDto = new CommentUpdateDTO("Review maneiro", "Review muito interessante, talvez um dia eu adquira essa a HQ!");
			
			User user = UserBuilder.aUser().withId(id).now();
						
			Comment foundComment = CommentBuilder.aComment().withId(id).now();
			foundComment.setUser(user);
			
			UserSS userSS = new UserSS(id, user.getEmail(), user.getPassword(), user.getProfiles());
						
			Comment updatedComment = new Comment(id, "Review maneiro", LocalDateTime.of(2022, 11, 22, 20, 12), 
										"Review muito interessante, talvez um dia eu adquira essa a HQ!", null, null);
			
			mockedStatic.when(UserService::authenticated).thenReturn(userSS);
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
	}
	
	@Test
	@DisplayName("Must filter comments by review")
	public void findCommentsByReviewTest() {
		// Scenario
		Long id = 2l;
		
		Comment comment = CommentBuilder.aComment().withId(id).now();
		
		List<Comment> list = Arrays.asList(comment);
		
		PageRequest pageRequest = PageRequest.of(0, 24);
		
		Page<Comment> page = new PageImpl<Comment>(list, pageRequest, list.size());
		
		Mockito.when(commentRepository.findCommentsByReview(Mockito.anyLong(), Mockito.any(PageRequest.class))).thenReturn(page);
		
		// Execution
		Page<CommentDTO> foundComments = commentService.findCommentsByReview(id, 0, 24, "date", "DESC");
		
		// Verification
		Assertions.assertThat(foundComments.getTotalElements()).isEqualTo(1);		
		Assertions.assertThat(foundComments.getPageable().getPageNumber()).isEqualTo(0);
		Assertions.assertThat(foundComments.getPageable().getPageSize()).isEqualTo(24);		
		Assertions.assertThat(foundComments.getContent().get(0).getId()).isEqualTo(id);
		Assertions.assertThat(foundComments.getContent().get(0).getTitle()).isEqualTo(comment.getTitle());
		Assertions.assertThat(foundComments.getContent().get(0).getContent()).isEqualTo(comment.getContent());
		Assertions.assertThat(foundComments.getContent().get(0).getDate()).isEqualTo(comment.getDate());
	}
	
	@Test
	@DisplayName("Must filter comments by user")
	public void findCommentsByUserTest() {
		// Scenario
		Long id = 2l;
		
		Comment comment = CommentBuilder.aComment().withId(id).now();
		
		List<Comment> list = Arrays.asList(comment);
		
		PageRequest pageRequest = PageRequest.of(0, 24);
		
		Page<Comment> page = new PageImpl<Comment>(list, pageRequest, list.size());
		
		Mockito.when(commentRepository.findCommentsByUser(Mockito.anyLong(), Mockito.any(PageRequest.class))).thenReturn(page);
		
		// Execution
		Page<CommentDTO> foundComments = commentService.findCommentsByUser(id, 0, 24, "date", "DESC");
		
		// Verification
		Assertions.assertThat(foundComments.getTotalElements()).isEqualTo(1);		
		Assertions.assertThat(foundComments.getPageable().getPageNumber()).isEqualTo(0);
		Assertions.assertThat(foundComments.getPageable().getPageSize()).isEqualTo(24);		
		Assertions.assertThat(foundComments.getContent().get(0).getId()).isEqualTo(id);
		Assertions.assertThat(foundComments.getContent().get(0).getTitle()).isEqualTo(comment.getTitle());
		Assertions.assertThat(foundComments.getContent().get(0).getContent()).isEqualTo(comment.getContent());
		Assertions.assertThat(foundComments.getContent().get(0).getDate()).isEqualTo(comment.getDate());
	}
	
	@Test
	@DisplayName("Must delete a comment")
	public void deleteCommentTest() {
		try(MockedStatic<UserService> mockedStatic = Mockito.mockStatic(UserService.class)) {
			// Scenario
			Long id = 2l;
			
			User user = UserBuilder.aUser().withId(id).now();
			
			Comment foundComment = CommentBuilder.aComment().withId(id).now();
			foundComment.setUser(user);
			
			UserSS userSS = new UserSS(id, user.getEmail(), user.getPassword(), user.getProfiles());
			
			mockedStatic.when(UserService::authenticated).thenReturn(userSS);
			Mockito.doReturn(foundComment).when(commentService).findById(id);
			
			// Execution
			commentService.delete(id);
			
			// Verification
			Mockito.verify(commentRepository, Mockito.times(1)).delete(foundComment);
		}
	}
			
}
