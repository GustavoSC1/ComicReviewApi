package com.gustavo.comicreviewapi.resources;

import java.time.LocalDateTime;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
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
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.gustavo.comicreviewapi.dtos.ReviewDTO;
import com.gustavo.comicreviewapi.dtos.ReviewNewDTO;
import com.gustavo.comicreviewapi.dtos.ReviewUpdateDTO;
import com.gustavo.comicreviewapi.entities.Review;
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
	
	ObjectMapper mapper;	

	@BeforeEach
	public void setUp() {
		mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
	}
	
	@Test
	@DisplayName("Must save a review")
	public void saveReviewTest() throws Exception {
		// Scenario
		long id = 2l;
		
		ReviewNewDTO newReview = createReviewNewDTO();
		ReviewDTO savedReview = createReviewDTO();
		savedReview.setId(id);
		
		BDDMockito.given(reviewService.save(Mockito.any(ReviewNewDTO.class))).willReturn(savedReview);
		
		String json = mapper.writeValueAsString(newReview);
		
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
	@DisplayName("Should throw validation error when there is not enough data for review creation")
	public void saveInvalidReviewTest() throws Exception {
		// Scenario
		ReviewNewDTO newReview = new ReviewNewDTO();
		
		String json = mapper.writeValueAsString(newReview);
				
		// Execution
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
													.post(REVIEW_API)
													.contentType(MediaType.APPLICATION_JSON)
													.accept(MediaType.APPLICATION_JSON)
													.content(json);
		// Verification
		mvc.perform(request)	
			.andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
			.andExpect(MockMvcResultMatchers.jsonPath("errors", Matchers.hasSize(4)));
	}
	
	@Test
	@DisplayName("Must get one review per id")
	public void findReviewTest() throws Exception {
		// Scenario
		Long id = 2l;
		
		ReviewDTO review = createReviewDTO();
		review.setId(id);
		
		BDDMockito.given(reviewService.find(id)).willReturn(review);
		
		// Execution
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
													.get(REVIEW_API.concat("/"+id))
													.accept(MediaType.APPLICATION_JSON);
		
		// Verification
		mvc.perform(request)
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.jsonPath("id").value(id))
			.andExpect(MockMvcResultMatchers.jsonPath("title").value("??tima hist??ria"))
			.andExpect(MockMvcResultMatchers.jsonPath("date").value("2022-11-20T21:50:00"))
			.andExpect(MockMvcResultMatchers.jsonPath("content").value("A HQ mostra o Homem-Aranha em sua ess??ncia: "
					+ "cheio de problemas, tentando fazer o que ?? certo enquanto luta para manter sua identidade secreta em "
					+ "segredo, com um turbilh??o de coisas acontecendo ao mesmo tempo, na escola, no namoro, no trabalho, em "
					+ "fam??lia. ?? maravilhoso ver a determina????o do her??i e imposs??vel n??o se identificar com ele, n??o se agoniar "
					+ "com seus problemas e torcer pela sua vit??ria. ?? tudo que se espera de uma boa aventura de super-her??is e "
					+ "um roteiro perfeito para um filme do Aracn??deo."));
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
	@DisplayName("Must update a review")
	public void updateReviewTest() throws Exception {
		// Scenario
		Long id = 2l;
		
		ReviewUpdateDTO reviewUpdateDto = new ReviewUpdateDTO("Hist??ria fraca", "A HQ n??o mostra quase nada sobre o Homem-Aranha: "
				+ "deveria mostrar mais sobre os seus problemas, ele tentando fazer o que ?? certo enquanto luta para manter sua identidade secreta em "
				+ "segredo, com um turbilh??o de coisas acontecendo ao mesmo tempo, na escola, no namoro, no trabalho, em "
				+ "fam??lia.");
		
		ReviewDTO updatedReview = new ReviewDTO(id, "Hist??ria fraca", LocalDateTime.of(2022, 11, 21, 19, 29), 
				"A HQ n??o mostra quase nada sobre o Homem-Aranha: "
				+ "deveria mostrar mais sobre os seus problemas, ele tentando fazer o que ?? certo enquanto luta para manter sua identidade secreta em "
				+ "segredo, com um turbilh??o de coisas acontecendo ao mesmo tempo, na escola, no namoro, no trabalho, em "
				+ "fam??lia.");
		
		BDDMockito.given(reviewService.update(Mockito.anyLong(), Mockito.any(ReviewUpdateDTO.class))).willReturn(updatedReview);
		
		String json = mapper.writeValueAsString(reviewUpdateDto);
		
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
			.andExpect(MockMvcResultMatchers.jsonPath("title").value("Hist??ria fraca"))
			.andExpect(MockMvcResultMatchers.jsonPath("date").value("2022-11-21T19:29:00"))
			.andExpect(MockMvcResultMatchers.jsonPath("content").value("A HQ n??o mostra quase nada sobre o Homem-Aranha: "
					+ "deveria mostrar mais sobre os seus problemas, ele tentando fazer o que ?? certo enquanto luta para manter sua identidade secreta em "
					+ "segredo, com um turbilh??o de coisas acontecendo ao mesmo tempo, na escola, no namoro, no trabalho, em "
					+ "fam??lia."));
	}
	
	@Test
	@DisplayName("Should throw validation error when there is not enough data for review updating")
	public void updateInvalidReviewTest() throws Exception {
		// Scenario
		Long id = 2l;
		
		ReviewUpdateDTO review = new ReviewUpdateDTO();
		
		String json = mapper.writeValueAsString(review);
		
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
	
	public ReviewNewDTO createReviewNewDTO() {
		return new ReviewNewDTO("??tima hist??ria", "A HQ mostra o Homem-Aranha em sua ess??ncia: "
				+ "cheio de problemas, tentando fazer o que ?? certo enquanto luta para manter sua identidade secreta em "
				+ "segredo, com um turbilh??o de coisas acontecendo ao mesmo tempo, na escola, no namoro, no trabalho, em "
				+ "fam??lia. ?? maravilhoso ver a determina????o do her??i e imposs??vel n??o se identificar com ele, n??o se agoniar "
				+ "com seus problemas e torcer pela sua vit??ria. ?? tudo que se espera de uma boa aventura de super-her??is e "
				+ "um roteiro perfeito para um filme do Aracn??deo.", 1l, 1l);
	}
	
	private ReviewDTO createReviewDTO() {					
		return new ReviewDTO(null,"??tima hist??ria", LocalDateTime.of(2022, 11, 20, 21, 50), "A HQ mostra o Homem-Aranha em sua ess??ncia: "
				+ "cheio de problemas, tentando fazer o que ?? certo enquanto luta para manter sua identidade secreta em "
				+ "segredo, com um turbilh??o de coisas acontecendo ao mesmo tempo, na escola, no namoro, no trabalho, em "
				+ "fam??lia. ?? maravilhoso ver a determina????o do her??i e imposs??vel n??o se identificar com ele, n??o se agoniar "
				+ "com seus problemas e torcer pela sua vit??ria. ?? tudo que se espera de uma boa aventura de super-her??is e "
				+ "um roteiro perfeito para um filme do Aracn??deo.");		
	}
	
}
