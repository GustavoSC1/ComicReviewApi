package com.gustavo.comicreviewapi.services;

import java.time.Clock;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.gustavo.comicreviewapi.dtos.ComicNewDTO;
import com.gustavo.comicreviewapi.dtos.feignDtos.CharacterListDTO;
import com.gustavo.comicreviewapi.dtos.feignDtos.CharacterSummaryDTO;
import com.gustavo.comicreviewapi.dtos.feignDtos.ComicDTO;
import com.gustavo.comicreviewapi.dtos.feignDtos.ComicDataContainerDTO;
import com.gustavo.comicreviewapi.dtos.feignDtos.ComicPriceDTO;
import com.gustavo.comicreviewapi.dtos.feignDtos.CreatorListDTO;
import com.gustavo.comicreviewapi.dtos.feignDtos.CreatorSummaryDTO;
import com.gustavo.comicreviewapi.dtos.feignDtos.MarvelAPIModelDTO;
import com.gustavo.comicreviewapi.entities.Comic;
import com.gustavo.comicreviewapi.feignClients.MarvelClient;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class ComicServiceTest {
		
	ComicService comicService;
		
	@MockBean
	AuthorService authorService;
	
	@MockBean
	CharacterService characterService;
	
	@MockBean
	MarvelClient marvelClient;
	
	@MockBean
	Clock clock;
	
	@BeforeEach
	public void setUp() {
		this.comicService = Mockito.spy(new ComicService("ae78641e8976ffdf3fd4b71254a3b9bf", 
				"eb9fd0d8r8745cd0d554fb2c0e7896dab3bb745", authorService, characterService, marvelClient, clock));		
	}
	
	@Test
	@DisplayName("Must create a hash code in MD5")
	public void getHashTest() {		
		// Scenario
		String timeStemp = "1655238166";
		
		// Execution
		String hash = comicService.getHash(timeStemp);
		
		// Verification
		Assertions.assertThat(hash).isEqualTo("c6fc42667498ea8081a22f4570b42d03");
	}
	
	@Test
	@DisplayName("Must return a MarvelAPIModelDTO obtained by MarvelClient")
	public void getComicByApiTest() {
		// Scenario
		MarvelAPIModelDTO foundMarvelAPIModelDTO = createMarvelAPIModelDTO();
		
		Mockito.when(clock.millis()).thenReturn(1655238166000l);
		
		Mockito.doReturn("c6fc42667498ea8081a22f4570b42d03").when(comicService).getHash("1655238166");
				
		Mockito.when(marvelClient.getComic(1, "1655238166", "ae78641e8976ffdf3fd4b71254a3b9bf", "c6fc42667498ea8081a22f4570b42d03")).thenReturn(foundMarvelAPIModelDTO);
		
		// Execution
		MarvelAPIModelDTO marvelAPIModelDTO = comicService.getComicByApi(1);
		
		// Verification
		Assertions.assertThat(marvelAPIModelDTO.getData().getResults().get(0).getId()).isEqualTo(1);
		Assertions.assertThat(marvelAPIModelDTO.getData().getResults().get(0).getTitle()).isEqualTo("Homem-Aranha: Eternamente jovem");
		Assertions.assertThat(marvelAPIModelDTO.getData().getResults().get(0).getIsbn()).isEqualTo("9786555612752");
		Assertions.assertThat(marvelAPIModelDTO.getData().getResults().get(0).getDescription()).isEqualTo("Na esperança de obter algumas fotos de seu alter "
				+ "ego aracnídeo em ação, Peter Parker "
				+ "sai em busca de problemas – e os encontra na forma de uma placa de pedra misteriosa e "
				+ "mítica cobiçada pelo Rei do Crime e pelos facínoras da Maggia, o maior sindicato criminal "
				+ "da cidade.");
		Assertions.assertThat(marvelAPIModelDTO.getData().getResults().get(0).getPrices().get(0).getPrice()).isEqualTo(38.61F);
		Assertions.assertThat(marvelAPIModelDTO.getData().getResults().get(0).getCharacters().getItems().get(0).getName()).isEqualTo("Homem Aranha");
		Assertions.assertThat(marvelAPIModelDTO.getData().getResults().get(0).getCreators().getItems().get(0).getName()).isEqualTo("Stefan Petrucha");
	}
	
	@Test
	@DisplayName("Must pass the values of MarvelAPIModelDTO to a Comic")
	public void fromDTOTest() {
		// Scenario
		MarvelAPIModelDTO foundMarvelAPIModelDTO = createMarvelAPIModelDTO();
		
		ComicNewDTO comicNewDTO = new ComicNewDTO();
		comicNewDTO.setIdComicMarvel(1);
		
		Mockito.doReturn(foundMarvelAPIModelDTO).when(comicService).getComicByApi(comicNewDTO.getIdComicMarvel());
		Mockito.when(authorService.findByName("Homem Aranha")).thenReturn(null);
		Mockito.when(characterService.findByName("Stefan Petrucha")).thenReturn(null);
		
		// Execution
		Comic comic = comicService.fromDTO(comicNewDTO);
		
		// Verification
		Assertions.assertThat(comic.getId()).isEqualTo(1);
		Assertions.assertThat(comic.getTitle()).isEqualTo("Homem-Aranha: Eternamente jovem");
		Assertions.assertThat(comic.getIsbn()).isEqualTo("9786555612752");
		Assertions.assertThat(comic.getDescription()).isEqualTo("Na esperança de obter algumas fotos de seu alter "
				+ "ego aracnídeo em ação, Peter Parker "
				+ "sai em busca de problemas – e os encontra na forma de uma placa de pedra misteriosa e "
				+ "mítica cobiçada pelo Rei do Crime e pelos facínoras da Maggia, o maior sindicato criminal "
				+ "da cidade.");
		Assertions.assertThat(comic.getPrice()).isEqualTo(38.61F);
		Assertions.assertThat(comic.getCharacters().stream().findFirst().get().getName()).isEqualTo("Homem Aranha");
		Assertions.assertThat(comic.getAuthors().stream().findFirst().get().getName()).isEqualTo("Stefan Petrucha");
	}
	
	private MarvelAPIModelDTO createMarvelAPIModelDTO() {
		ComicPriceDTO comicPriceDTO = new ComicPriceDTO(38.61F);
		
		CreatorSummaryDTO creatorSummaryDTO = new CreatorSummaryDTO("Stefan Petrucha");
		
		CharacterSummaryDTO characterSummaryDTO = new CharacterSummaryDTO("Homem Aranha");
		
		CreatorListDTO creatorListDTO = new CreatorListDTO();
		creatorListDTO.getItems().add(creatorSummaryDTO);
		
		CharacterListDTO characterListDTO = new CharacterListDTO();
		characterListDTO.getItems().add(characterSummaryDTO);
		
		ComicDTO comicDTO = new ComicDTO(1, "Homem-Aranha: Eternamente jovem", "9786555612752", 
				"Na esperança de obter algumas fotos de seu alter "
				+ "ego aracnídeo em ação, Peter Parker "
				+ "sai em busca de problemas – e os encontra na forma de uma placa de pedra misteriosa e "
				+ "mítica cobiçada pelo Rei do Crime e pelos facínoras da Maggia, o maior sindicato criminal "
				+ "da cidade.");
		
		comicDTO.setCharacters(characterListDTO);
		comicDTO.setCreators(creatorListDTO);
		comicDTO.getPrices().add(comicPriceDTO);
		
		ComicDataContainerDTO comicDataContainerDTO = new ComicDataContainerDTO();
		comicDataContainerDTO.getResults().add(comicDTO);
		
		MarvelAPIModelDTO marvelAPIModelDTO = new MarvelAPIModelDTO(comicDataContainerDTO);
		
		return marvelAPIModelDTO;		
	}

}
