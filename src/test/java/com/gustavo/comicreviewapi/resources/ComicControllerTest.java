package com.gustavo.comicreviewapi.resources;

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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gustavo.comicreviewapi.builders.ComicDtoBuilder;
import com.gustavo.comicreviewapi.builders.ReviewDtoBuilder;
import com.gustavo.comicreviewapi.dtos.AuthorDTO;
import com.gustavo.comicreviewapi.dtos.CharacterDTO;
import com.gustavo.comicreviewapi.dtos.ComicDTO;
import com.gustavo.comicreviewapi.dtos.ComicNewDTO;
import com.gustavo.comicreviewapi.dtos.RateNewDTO;
import com.gustavo.comicreviewapi.dtos.ReviewDTO;
import com.gustavo.comicreviewapi.entities.Comic;
import com.gustavo.comicreviewapi.services.ComicService;
import com.gustavo.comicreviewapi.services.RateService;
import com.gustavo.comicreviewapi.services.ReviewService;
import com.gustavo.comicreviewapi.services.exceptions.ObjectNotFoundException;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = ComicController.class)
@AutoConfigureMockMvc
public class ComicControllerTest {
	
	static String COMIC_API = "/comics";
	
	@Autowired
	MockMvc mvc;
	
	@MockBean
	ComicService comicService;
	
	@MockBean
	ReviewService reviewService;
	
	@MockBean
	RateService rateService;
	
	@Test
	@DisplayName("Must save a comic")
	public void saveComicTest() throws Exception {
		// Scenario
		Integer id = 1;
		
		ComicNewDTO newComic = new ComicNewDTO(id);
		ComicDTO savedComic = ComicDtoBuilder.aComicDTO().withCharactersDtoList(new CharacterDTO(null, "Homem Aranha"))
				.withAuthorsList(new AuthorDTO(null, "Stefan Petrucha")).withId(Long.valueOf(id)).now();
		
		BDDMockito.given(comicService.save(Mockito.any(ComicNewDTO.class))).willReturn(savedComic);
		
		String json = new ObjectMapper().writeValueAsString(newComic);
		
		// Execution
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
													.post(COMIC_API)
													.contentType(MediaType.APPLICATION_JSON)
													.accept(MediaType.APPLICATION_JSON)
													.content(json);
		
		// Verification
		mvc
		.perform(request)
		.andExpect( MockMvcResultMatchers.status().isCreated() )
		.andExpect( MockMvcResultMatchers.header().string(HttpHeaders.LOCATION, Matchers.containsString("/comics/"+id)) );
	}
	
	
	
	@Test
	@DisplayName("Should throw validation error when there is not enough data for comic creation")
	public void createInvalidComicTest() throws Exception {
		// Scenario
		ComicNewDTO newComic = new ComicNewDTO();
				
		String json = new ObjectMapper().writeValueAsString(newComic);
		
		// Execution
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
													.post(COMIC_API)
													.contentType(MediaType.APPLICATION_JSON)
													.accept(MediaType.APPLICATION_JSON)
													.content(json);
		// Verification
		mvc.perform(request)
			.andExpect( MockMvcResultMatchers.status().isUnprocessableEntity() )
			.andExpect( MockMvcResultMatchers.jsonPath("errors", Matchers.hasSize(1)) );
	}
	
	@Test
	@DisplayName("Must get one comic per id")
	public void findComicTest() throws Exception {
		// Scenario
		Long id = 2l;
		
		ComicDTO comic = ComicDtoBuilder.aComicDTO().withCharactersDtoList(new CharacterDTO(null, "Homem Aranha"))
				.withAuthorsList(new AuthorDTO(null, "Stefan Petrucha")).withId(Long.valueOf(id)).now();
		
		BDDMockito.given(comicService.find(id)).willReturn(comic);
		
		// Execution
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
													.get(COMIC_API.concat("/"+id))
													.accept(MediaType.APPLICATION_JSON);
		
		// Verification
		mvc.perform(request)
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("id").value(id))
		.andExpect(MockMvcResultMatchers.jsonPath("title").value("Homem-Aranha: Eternamente jovem"))
		.andExpect(MockMvcResultMatchers.jsonPath("isbn").value("9786555612752"))
		.andExpect(MockMvcResultMatchers.jsonPath("description").value("Na esperança de obter algumas fotos de seu alter "
				+ "ego aracnídeo em ação, Peter Parker "
				+ "sai em busca de problemas – e os encontra na forma de uma placa de pedra misteriosa e "
				+ "mítica cobiçada pelo Rei do Crime e pelos facínoras da Maggia, o maior sindicato criminal "
				+ "da cidade."))
		.andExpect(MockMvcResultMatchers.jsonPath("averageRating").value(0.0))
		.andExpect(MockMvcResultMatchers.jsonPath("characters[0].name").value("Homem Aranha"))
		.andExpect(MockMvcResultMatchers.jsonPath("authors[0].name").value("Stefan Petrucha"));
	}
	
	@Test
	@DisplayName("Should return error when trying to get a non-existent comic")
	public void comicNotFoundByIdTest() throws Exception {
		// Scenario
		Long id = 2l;
		
		BDDMockito.given(comicService.find(id)).willThrow(new ObjectNotFoundException("Object not found! Id: " + id + ", Type: " + Comic.class.getName()));
		
		// Execution
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
													.get(COMIC_API.concat("/"+id))
													.accept(MediaType.APPLICATION_JSON);
		
		// Verification
		mvc
		.perform(request)
		.andExpect(MockMvcResultMatchers.status().isNotFound())
		.andExpect(MockMvcResultMatchers.jsonPath("error").value("Not found"))
		.andExpect(MockMvcResultMatchers.jsonPath("message").value("Object not found! Id: " + id + ", Type: " + Comic.class.getName()));
	}
	
	@Test
	@DisplayName("Must filter comics")
	public void findByTitleTest() throws Exception {
		// Scenario
		Long id = 2l;
		
		ComicDTO comic = ComicDtoBuilder.aComicDTO().withCharactersDtoList(new CharacterDTO(null, "Homem Aranha"))
				.withAuthorsList(new AuthorDTO(null, "Stefan Petrucha")).withId(Long.valueOf(id)).now();
		
		List<ComicDTO> list = Arrays.asList(comic);
		
		PageRequest pageRequest = PageRequest.of(0, 24);
		
		Page<ComicDTO> page = new PageImpl<ComicDTO>(list, pageRequest, list.size());
		
		BDDMockito.given(comicService.findByTitle("Eternamente", 0, 24, "title", "ASC")).willReturn(page);
		
		String queryString = String.format("?title=%s&page=0&linesPerPage=24&orderBy=%s&direction=%s", "Eternamente", "title", "ASC");
		
		// Execution
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get(COMIC_API.concat(queryString))
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
	@DisplayName("Must filter reviews by comic")
	public void findReviewsByComicTest() throws Exception {
		// Scenario
		Long id = 2l;
		
		ReviewDTO review = ReviewDtoBuilder.aReviewDTO().withId(id).now();
		
		List<ReviewDTO> list = Arrays.asList(review);
		
		PageRequest pageRequest = PageRequest.of(0, 24);
		
		Page<ReviewDTO> page = new PageImpl<ReviewDTO>(list, pageRequest, list.size());
		
		BDDMockito.given(reviewService.findReviewsByComic(id, 0, 24, "date", "DESC")).willReturn(page);
		
		// Execution
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get(COMIC_API.concat("/"+id+"/reviews"))
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
	@DisplayName("Must save a rate")
	public void saveRateTest() throws Exception {
		// Scenario
		Long id = 1l;
		
		RateNewDTO newRate = new RateNewDTO(id, 4);
		
		String json = new ObjectMapper().writeValueAsString(newRate);
		
		// Execution
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
													.post(COMIC_API.concat("/"+id+"/ratings"))
													.contentType(MediaType.APPLICATION_JSON)
													.accept(MediaType.APPLICATION_JSON)
													.content(json);
		
		// Verification
		mvc
		.perform(request)
		.andExpect(MockMvcResultMatchers.status().isOk());
		
		Mockito.verify(rateService, Mockito.times(1)).save(Mockito.anyLong(), Mockito.any(RateNewDTO.class));
	}
	
	@Test
	@DisplayName("Should throw validation error when there is not enough data for rate creation")
	public void saveInvalidRateTest() throws Exception {
		// Scenario
		Long id = 1l;
		
		RateNewDTO newRate = new RateNewDTO();
		newRate.setRate(6);
		
		String json = new ObjectMapper().writeValueAsString(newRate);
		
		// Execution
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
													.post(COMIC_API.concat("/"+id+"/ratings"))
													.contentType(MediaType.APPLICATION_JSON)
													.accept(MediaType.APPLICATION_JSON)
													.content(json);
		
		// Verification
		mvc.perform(request)
		 .andExpect( MockMvcResultMatchers.status().isUnprocessableEntity() )
		 .andExpect( MockMvcResultMatchers.jsonPath("errors", Matchers.hasSize(2)) );
	}
	
}
