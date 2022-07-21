package com.gustavo.comicreviewapi.resources;

import java.time.LocalDateTime;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gustavo.comicreviewapi.dtos.CommentDTO;
import com.gustavo.comicreviewapi.dtos.CommentNewDTO;
import com.gustavo.comicreviewapi.entities.Comment;
import com.gustavo.comicreviewapi.services.CommentService;
import com.gustavo.comicreviewapi.services.exceptions.ObjectNotFoundException;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = CommentController.class)
@AutoConfigureMockMvc
public class CommentControllerTest {
	
	static String COMMENT_API = "/comments";
	
	@Autowired
	MockMvc mvc;
	
	@MockBean
	CommentService commentService;
	
	@Test
	@DisplayName("Must save a comment")
	public void saveCommentTest() throws Exception {
		// Scenario
		long id = 2l;
		
		CommentNewDTO newComment = createCommentNewDTO();
		CommentDTO savedComment = createCommentDTO();
		savedComment.setId(id);
		
		BDDMockito.given(commentService.save(Mockito.any(CommentNewDTO.class))).willReturn(savedComment);
		
		String json = new ObjectMapper().writeValueAsString(newComment);
				
		// Execution
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
													.post(COMMENT_API)
													.contentType(MediaType.APPLICATION_JSON)
													.accept(MediaType.APPLICATION_JSON)
													.content(json);	
		
		// Verification
		mvc
			.perform(request)
			.andExpect( MockMvcResultMatchers.status().isCreated() )
			.andExpect( MockMvcResultMatchers.header().string(HttpHeaders.LOCATION, Matchers.containsString("/comments/"+id)));
	}
	
	@Test
	@DisplayName("Should throw validation error when there is not enough data for comment creation")
	public void saveInvalidCommentTest() throws Exception {
		// Scenario
		CommentNewDTO newComment = new CommentNewDTO();
		
		String json = new ObjectMapper().writeValueAsString(newComment);
		
		// Execution
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
													.post(COMMENT_API)
													.contentType(MediaType.APPLICATION_JSON)
													.accept(MediaType.APPLICATION_JSON)
													.content(json);
		
		// Verification
		mvc.perform(request)	
				.andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
				.andExpect(MockMvcResultMatchers.jsonPath("errors", Matchers.hasSize(4)));
	}
	
	@Test
	@DisplayName("Must get one comment per id")
	public void findCommentTest() throws Exception {
		// Scenario
		Long id = 2l;
		
		CommentDTO comment = createCommentDTO();
		comment.setId(id);
		
		BDDMockito.given(commentService.find(id)).willReturn(comment);
		
		// Execution
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
													.get(COMMENT_API.concat("/"+id))
													.accept(MediaType.APPLICATION_JSON);
		// Verification
		mvc.perform(request)
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("id").value(id))
		.andExpect(MockMvcResultMatchers.jsonPath("title").value("Ótimo review"))
		.andExpect(MockMvcResultMatchers.jsonPath("date").value("2022-11-20T22:10:00"))
		.andExpect(MockMvcResultMatchers.jsonPath("content").value("Parabéns pelo review, com certeza irei adquirir essa HQ!"));
	}
	
	@Test
	@DisplayName("Should return error when trying to get a non-existent comment")
	public void commentNotFoundByIdTest() throws Exception {
		// Scenario
		Long id = 2l;
		
		BDDMockito.given(commentService.find(id)).willThrow(new ObjectNotFoundException("Object not found! Id: " + id + ", Type: " + Comment.class.getName()));
		
		// Execution
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
													.get(COMMENT_API.concat("/"+id))
													.accept(MediaType.APPLICATION_JSON);
		
		// Verification
		mvc.perform(request)
			.andExpect(MockMvcResultMatchers.status().isNotFound())
			.andExpect(MockMvcResultMatchers.jsonPath("error").value("Not found"))
			.andExpect(MockMvcResultMatchers.jsonPath("message").value("Object not found! Id: " + id + ", Type: " + Comment.class.getName()));
	}
	
	private CommentNewDTO createCommentNewDTO() {
		return new CommentNewDTO("Ótimo review", "Parabéns pelo review, com certeza irei adquirir essa HQ!", 1l, 1l);
	}
	
	private CommentDTO createCommentDTO() {
		return new CommentDTO(null, "Ótimo review", LocalDateTime.of(2022, 11, 20, 22, 10), "Parabéns pelo review, com certeza irei adquirir essa HQ!");
	}

}
