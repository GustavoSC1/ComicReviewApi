package com.gustavo.comicreviewapi.resources;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gustavo.comicreviewapi.builders.CommentDtoBuilder;
import com.gustavo.comicreviewapi.builders.ReviewDtoBuilder;
import com.gustavo.comicreviewapi.builders.ReviewNewDtoBuilder;
import com.gustavo.comicreviewapi.dtos.CommentDTO;
import com.gustavo.comicreviewapi.dtos.LikeNewDTO;
import com.gustavo.comicreviewapi.dtos.ReviewDTO;
import com.gustavo.comicreviewapi.dtos.ReviewNewDTO;
import com.gustavo.comicreviewapi.dtos.ReviewUpdateDTO;
import com.gustavo.comicreviewapi.entities.Review;
import com.gustavo.comicreviewapi.security.JWTUtil;
import com.gustavo.comicreviewapi.services.CommentService;
import com.gustavo.comicreviewapi.services.LikeService;
import com.gustavo.comicreviewapi.services.ReviewService;
import com.gustavo.comicreviewapi.services.exceptions.ObjectNotFoundException;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = ReviewController.class)
@AutoConfigureMockMvc
public class ReviewControllerTest {
	
	static String REVIEW_API = "/reviews";
	
	@Autowired
	MockMvc mvc;
	
	@MockBean
	ReviewService reviewService;
	
	@MockBean
	CommentService commentService;
	
	@MockBean
	JWTUtil jwtUtil;
	
	@MockBean
	UserDetailsService userDetailsService;
	
	@MockBean
	LikeService likeService;
		
	@Test
	@WithMockUser(username = "gu.cruz17@hotmail.com", roles = {"USER"})
	@DisplayName("Must save a review")
	public void saveReviewTest() throws Exception {
		// Scenario
		long id = 2l;
		
		ReviewNewDTO newReview = ReviewNewDtoBuilder.aReviewNewDTO().now();
		ReviewDTO savedReview = ReviewDtoBuilder.aReviewDTO().withId(id).now();
		
		BDDMockito.given(reviewService.save(Mockito.any(ReviewNewDTO.class))).willReturn(savedReview);
		
		String json = new ObjectMapper().writeValueAsString(newReview);
		
		// Execution
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
													.post(REVIEW_API)
													.contentType(MediaType.APPLICATION_JSON)
													.accept(MediaType.APPLICATION_JSON)
													.content(json);		
		
		// Verification
		mvc
		.perform(request)
		.andExpect( MockMvcResultMatchers.status().isCreated() )
		.andExpect( MockMvcResultMatchers.header().string(HttpHeaders.LOCATION, Matchers.containsString("/reviews/"+id)));
	}
	
	@Test
	@WithMockUser(username = "gu.cruz17@hotmail.com", roles = {"USER"})
	@DisplayName("Should throw validation error when there is not enough data for review creation")
	public void saveInvalidReviewTest() throws Exception {
		// Scenario
		ReviewNewDTO newReview = new ReviewNewDTO();
		
		String json = new ObjectMapper().writeValueAsString(newReview);
				
		// Execution
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
													.post(REVIEW_API)
													.contentType(MediaType.APPLICATION_JSON)
													.accept(MediaType.APPLICATION_JSON)
													.content(json);
		// Verification
		mvc.perform(request)	
			.andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
			.andExpect(MockMvcResultMatchers.jsonPath("errors", Matchers.hasSize(3)));
	}
	
	@Test
	@DisplayName("Must get one review per id")
	public void findReviewTest() throws Exception {
		// Scenario
		Long id = 2l;
		
		ReviewDTO review = ReviewDtoBuilder.aReviewDTO().withId(id).now();
		
		BDDMockito.given(reviewService.find(id)).willReturn(review);
		
		// Execution
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
													.get(REVIEW_API.concat("/"+id))
													.accept(MediaType.APPLICATION_JSON);
		
		// Verification
		mvc.perform(request)
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.jsonPath("id").value(id))
			.andExpect(MockMvcResultMatchers.jsonPath("title").value("Ótima história"))
			.andExpect(MockMvcResultMatchers.jsonPath("date").value("2022-11-20T21:50:00"))
			.andExpect(MockMvcResultMatchers.jsonPath("content").value("A HQ mostra o Homem-Aranha em sua essência: "
					+ "cheio de problemas, tentando fazer o que é certo enquanto luta para manter sua identidade secreta em "
					+ "segredo, com um turbilhão de coisas acontecendo ao mesmo tempo, na escola, no namoro, no trabalho, em "
					+ "família. É maravilhoso ver a determinação do herói e impossível não se identificar com ele, não se agoniar "
					+ "com seus problemas e torcer pela sua vitória. É tudo que se espera de uma boa aventura de super-heróis e "
					+ "um roteiro perfeito para um filme do Aracnídeo."));
	}
	
	@Test
	@DisplayName("Should return error when trying to get a non-existent review")
	public void reviewNotFoundByIdTest() throws Exception {
		// Scenario
		Long id = 2l;
		
		BDDMockito.given(reviewService.find(id)).willThrow(new ObjectNotFoundException("Object not found! Id: " + id + ", Type: " + Review.class.getName()));
		
		// Execution
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
													.get(REVIEW_API.concat("/"+id))
													.accept(MediaType.APPLICATION_JSON);
		
		// Verification
		mvc.perform(request)
			.andExpect(MockMvcResultMatchers.status().isNotFound())
			.andExpect(MockMvcResultMatchers.jsonPath("error").value("Not found"))
			.andExpect(MockMvcResultMatchers.jsonPath("message").value("Object not found! Id: " + id + ", Type: " + Review.class.getName()));
	}
	
	@Test
	@WithMockUser(username = "gu.cruz17@hotmail.com", roles = {"USER"})
	@DisplayName("Must update a review")
	public void updateReviewTest() throws Exception {
		// Scenario
		Long id = 2l;
		
		ReviewUpdateDTO reviewUpdateDto = new ReviewUpdateDTO("História fraca", "A HQ não mostra quase nada sobre o Homem-Aranha: "
				+ "deveria mostrar mais sobre os seus problemas, ele tentando fazer o que é certo enquanto luta para manter sua identidade secreta em "
				+ "segredo, com um turbilhão de coisas acontecendo ao mesmo tempo, na escola, no namoro, no trabalho, em "
				+ "família.");
		
		ReviewDTO updatedReview = new ReviewDTO(id, "História fraca", LocalDateTime.of(2022, 11, 21, 19, 29), 
				"A HQ não mostra quase nada sobre o Homem-Aranha: "
				+ "deveria mostrar mais sobre os seus problemas, ele tentando fazer o que é certo enquanto luta para manter sua identidade secreta em "
				+ "segredo, com um turbilhão de coisas acontecendo ao mesmo tempo, na escola, no namoro, no trabalho, em "
				+ "família.");
		
		BDDMockito.given(reviewService.update(Mockito.anyLong(), Mockito.any(ReviewUpdateDTO.class))).willReturn(updatedReview);
		
		String json = new ObjectMapper().writeValueAsString(reviewUpdateDto);
		
		// Execution
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
													.put(REVIEW_API.concat("/"+id))
													.contentType(MediaType.APPLICATION_JSON)
													.accept(MediaType.APPLICATION_JSON)
													.content(json);
		
		// Verification
		mvc.perform(request)
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.jsonPath("id").value(id))
			.andExpect(MockMvcResultMatchers.jsonPath("title").value("História fraca"))
			.andExpect(MockMvcResultMatchers.jsonPath("date").value("2022-11-21T19:29:00"))
			.andExpect(MockMvcResultMatchers.jsonPath("content").value("A HQ não mostra quase nada sobre o Homem-Aranha: "
					+ "deveria mostrar mais sobre os seus problemas, ele tentando fazer o que é certo enquanto luta para manter sua identidade secreta em "
					+ "segredo, com um turbilhão de coisas acontecendo ao mesmo tempo, na escola, no namoro, no trabalho, em "
					+ "família."));
	}
	
	@Test
	@WithMockUser(username = "gu.cruz17@hotmail.com", roles = {"USER"})
	@DisplayName("Should throw validation error when there is not enough data for review updating")
	public void updateInvalidReviewTest() throws Exception {
		// Scenario
		Long id = 2l;
		
		ReviewUpdateDTO review = new ReviewUpdateDTO();
		
		String json = new ObjectMapper().writeValueAsString(review);
		
		// Execution
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
													.put(REVIEW_API.concat("/"+id))
													.contentType(MediaType.APPLICATION_JSON)
													.accept(MediaType.APPLICATION_JSON)
													.content(json);		
		// Verification
		mvc.perform(request)
			.andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
			.andExpect(MockMvcResultMatchers.jsonPath("errors", Matchers.hasSize(2)));
	}
	
	@Test
	@WithMockUser(username = "gu.cruz17@hotmail.com", roles = {"USER"})
	@DisplayName("Must delete a review")
	public void deleteReviewTest() throws Exception {
		// Execution
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.delete(REVIEW_API.concat("/" + 1));
		
		// Verification
		mvc.perform(request)
			.andExpect(MockMvcResultMatchers.status().isNoContent());
	}
	
	@Test
	@DisplayName("Must filter comments by review")
	public void findCommentsByReviewTest() throws Exception {
		// Scenario
		Long id = 2l;
		
		CommentDTO comment = CommentDtoBuilder.aCommentDTO().withId(id).now();
		
		List<CommentDTO> list = Arrays.asList(comment);
		
		PageRequest pageRequest = PageRequest.of(0, 24);
		
		Page<CommentDTO> page = new PageImpl<CommentDTO>(list, pageRequest, list.size());
		
		BDDMockito.given(commentService.findCommentsByReview(id, 0, 24, "date", "DESC")).willReturn(page);
		
		// Execution
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get(REVIEW_API.concat("/"+id+"/comments"))
													.accept(MediaType.APPLICATION_JSON);
		
		// Verification
		mvc.perform(request)
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.jsonPath("content", Matchers.hasSize(1)))
			.andExpect(MockMvcResultMatchers.jsonPath("totalElements").value(1))
			.andExpect(MockMvcResultMatchers.jsonPath("pageable.pageSize").value(24))
			.andExpect(MockMvcResultMatchers.jsonPath("pageable.pageNumber").value(0));		
	}
	
	@Test
	@DisplayName("Must filter review")
	public void findByTitleTest() throws Exception {
		// Scenario
		Long id = 2l;
		
		ReviewDTO review = ReviewDtoBuilder.aReviewDTO().withId(id).now();
		
		List<ReviewDTO> list = Arrays.asList(review);
		
		PageRequest pageRequest = PageRequest.of(0, 24);
		
		Page<ReviewDTO> page = new PageImpl<ReviewDTO>(list, pageRequest, list.size());
		
		BDDMockito.given(reviewService.findByTitle("história", 0, 24, "date", "DESC")).willReturn(page);
		
		String queryString = String.format("?title=%s&page=0&linesPerPage=24&orderBy=%s&direction=%s", "história", "date", "DESC");
		
		// Execution
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get(REVIEW_API.concat(queryString))
														.accept(MediaType.APPLICATION_JSON);
		
		// Verification
		mvc.perform(request)
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.jsonPath("content", Matchers.hasSize(1)))
			.andExpect(MockMvcResultMatchers.jsonPath("totalElements").value(1))
			.andExpect(MockMvcResultMatchers.jsonPath("pageable.pageSize").value(24))
			.andExpect(MockMvcResultMatchers.jsonPath("pageable.pageNumber").value(0));
	}
	
	@Test
	@WithMockUser(username = "gu.cruz17@hotmail.com", roles = {"USER"})
	@DisplayName("Must save a like")
	public void saveLikeTest() throws Exception {
		// Scenario
		Long id = 1l;
		
		LikeNewDTO newLike = new LikeNewDTO(true);
		
		String json = new ObjectMapper().writeValueAsString(newLike);
		
		// Execution
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
													.post(REVIEW_API.concat("/"+id+"/likes"))
													.contentType(MediaType.APPLICATION_JSON)
													.accept(MediaType.APPLICATION_JSON)
													.content(json);
		
		// Verification
		mvc
		.perform(request)
		.andExpect(MockMvcResultMatchers.status().isOk());
		
		Mockito.verify(likeService, Mockito.times(1)).save(Mockito.anyLong(), Mockito.any(LikeNewDTO.class));
	}
			
}
