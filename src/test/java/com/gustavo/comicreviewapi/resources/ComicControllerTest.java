package com.gustavo.comicreviewapi.resources;

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
import com.gustavo.comicreviewapi.dtos.AuthorDTO;
import com.gustavo.comicreviewapi.dtos.CharacterDTO;
import com.gustavo.comicreviewapi.dtos.ComicDTO;
import com.gustavo.comicreviewapi.dtos.ComicNewDTO;
import com.gustavo.comicreviewapi.services.ComicService;

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
	
	@Test
	@DisplayName("Must save a comic")
	public void saveComicTest() throws Exception {
		// Scenario
		Integer id = 1;
		
		ComicNewDTO newComic = new ComicNewDTO(id);
		ComicDTO savedComic = createComicDTO();
		savedComic.setId(Long.valueOf(id));
		
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
	
	private ComicDTO createComicDTO() {
		AuthorDTO authorDto = new AuthorDTO(null, "Stefan Petrucha");
		CharacterDTO characterDto = new CharacterDTO(null, "Homem Aranha");
		
		ComicDTO comicDto = new ComicDTO(null, "Homem-Aranha: Eternamente jovem", 38.61F, "9786555612752", 
				"Na esperança de obter algumas fotos de seu alter "
				+ "ego aracnídeo em ação, Peter Parker "
				+ "sai em busca de problemas – e os encontra na forma de uma placa de pedra misteriosa e "
				+ "mítica cobiçada pelo Rei do Crime e pelos facínoras da Maggia, o maior sindicato criminal "
				+ "da cidade.");
		
		comicDto.getAuthors().add(authorDto);
		comicDto.getCharacters().add(characterDto);
		
		return comicDto;
	}

}
