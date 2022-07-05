package com.gustavo.comicreviewapi.services;

import java.time.Clock;
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

import com.gustavo.comicreviewapi.dtos.ComicNewDTO;
import com.gustavo.comicreviewapi.dtos.feignDtos.CharacterListDTO;
import com.gustavo.comicreviewapi.dtos.feignDtos.CharacterSummaryDTO;
import com.gustavo.comicreviewapi.dtos.feignDtos.ComicDTO;
import com.gustavo.comicreviewapi.dtos.feignDtos.ComicDataContainerDTO;
import com.gustavo.comicreviewapi.dtos.feignDtos.ComicPriceDTO;
import com.gustavo.comicreviewapi.dtos.feignDtos.CreatorListDTO;
import com.gustavo.comicreviewapi.dtos.feignDtos.CreatorSummaryDTO;
import com.gustavo.comicreviewapi.dtos.feignDtos.MarvelAPIModelDTO;
import com.gustavo.comicreviewapi.entities.Author;
import com.gustavo.comicreviewapi.entities.Character;
import com.gustavo.comicreviewapi.entities.Comic;
import com.gustavo.comicreviewapi.feignClients.MarvelClient;
import com.gustavo.comicreviewapi.repositories.ComicRepository;
import com.gustavo.comicreviewapi.services.exceptions.BusinessException;
import com.gustavo.comicreviewapi.services.exceptions.ObjectNotFoundException;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class ComicServiceTest {
		
	ComicService comicService;
	
	@MockBean
	ComicRepository comicRepository;
		
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
				"eb9fd0d8r8745cd0d554fb2c0e7896dab3bb745", comicRepository, authorService, characterService, marvelClient, 
				clock));		
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
	
	@Test
	@DisplayName("Should throw business error when trying to save a comic with duplicate id")
	public void shouldNotSaveAComicWithDuplicatedId() {
		// Scenario
		ComicNewDTO comicNewDTO = new ComicNewDTO();
		comicNewDTO.setIdComicMarvel(1);
		
		Mockito.when(comicRepository.existsById(1l)).thenReturn(true);
		
		// Execution and Verification
		Exception exception = assertThrows(BusinessException.class, () -> {comicService.fromDTO(comicNewDTO);});
		
		String expectedMessage = "Comic already registered!";
		String actualMessage = exception.getMessage();
		
		Assertions.assertThat(actualMessage).isEqualTo(expectedMessage);
	}
	
	@Test
	@DisplayName("Must save a comic")
	public void saveComicTest() {
		// Scenario
		Integer id = 1;
		
		ComicNewDTO comicNewDto = new ComicNewDTO();
		comicNewDto.setIdComicMarvel(id);
		
		Comic comic = createComic();
		comic.setId(Long.valueOf(id));
		
		Mockito.doReturn(comic).when(comicService).fromDTO(comicNewDto);
		Mockito.when(comicRepository.save(Mockito.any(Comic.class))).thenReturn(comic);
		
		// Execution
		com.gustavo.comicreviewapi.dtos.ComicDTO savedComic = comicService.save(comicNewDto);
		
		// Verification
		Assertions.assertThat(savedComic.getId()).isEqualTo(1);
		Assertions.assertThat(savedComic.getTitle()).isEqualTo("Homem-Aranha: Eternamente jovem");
		Assertions.assertThat(savedComic.getIsbn()).isEqualTo("9786555612752");
		Assertions.assertThat(savedComic.getDescription()).isEqualTo("Na esperança de obter algumas fotos de seu alter "
				+ "ego aracnídeo em ação, Peter Parker "
				+ "sai em busca de problemas – e os encontra na forma de uma placa de pedra misteriosa e "
				+ "mítica cobiçada pelo Rei do Crime e pelos facínoras da Maggia, o maior sindicato criminal "
				+ "da cidade.");
		Assertions.assertThat(savedComic.getPrice()).isEqualTo(38.61F);
		Assertions.assertThat(savedComic.getCharacters().stream().findFirst().get().getName()).isEqualTo("Homem Aranha");
		Assertions.assertThat(savedComic.getAuthors().stream().findFirst().get().getName()).isEqualTo("Stefan Petrucha");
	}
	
	@Test
	@DisplayName("Must get one comic per id")
	public void findByIdTest() {
		// Scenario
		Long id = 2l;
		
		Comic comic = createComic();
		comic.setId(id);
		
		Mockito.when(comicRepository.findById(id)).thenReturn(Optional.of(comic));
		
		// Execution
		Comic foundComic = comicService.findById(id);
		
		// Verification
		Assertions.assertThat(foundComic.getId()).isEqualTo(2);
		Assertions.assertThat(foundComic.getTitle()).isEqualTo("Homem-Aranha: Eternamente jovem");
		Assertions.assertThat(foundComic.getIsbn()).isEqualTo("9786555612752");
		Assertions.assertThat(foundComic.getDescription()).isEqualTo("Na esperança de obter algumas fotos de seu alter "
				+ "ego aracnídeo em ação, Peter Parker "
				+ "sai em busca de problemas – e os encontra na forma de uma placa de pedra misteriosa e "
				+ "mítica cobiçada pelo Rei do Crime e pelos facínoras da Maggia, o maior sindicato criminal "
				+ "da cidade.");
		Assertions.assertThat(foundComic.getPrice()).isEqualTo(38.61F);
		Assertions.assertThat(foundComic.getCharacters().stream().findFirst().get().getName()).isEqualTo("Homem Aranha");
		Assertions.assertThat(foundComic.getAuthors().stream().findFirst().get().getName()).isEqualTo("Stefan Petrucha");
	}
	
	@Test
	@DisplayName("Should return error when trying to get a non-existent comic")
	public void comicNotFoundByIdTest() {
		// Scenario
		Long id = 1l;
		Mockito.when(comicRepository.findById(id)).thenReturn(Optional.empty());
		
		// Execution and Verification
		Exception exception = assertThrows(ObjectNotFoundException.class, () -> {comicService.findById(id);});
	
		String expectMessage = "Object not found! Id: " + id + ", Type: " + Comic.class.getName();
		String actualMessage = exception.getMessage();
		
		Assertions.assertThat(actualMessage).isEqualTo(expectMessage);
	}
	
	@Test
	@DisplayName("Must call findById method and return Comic to controller")
	public void findTest() {
		// Scenario
		Long id = 2l;
		
		Comic comic = createComic();
		comic.setId(id);
		
		Mockito.doReturn(comic).when(comicService).findById(id);
		
		// Execution
		com.gustavo.comicreviewapi.dtos.ComicDTO foundComic = comicService.find(id);
		
		// Verification
		Assertions.assertThat(foundComic.getId()).isEqualTo(2);
		Assertions.assertThat(foundComic.getTitle()).isEqualTo("Homem-Aranha: Eternamente jovem");
		Assertions.assertThat(foundComic.getIsbn()).isEqualTo("9786555612752");
		Assertions.assertThat(foundComic.getDescription()).isEqualTo("Na esperança de obter algumas fotos de seu alter "
				+ "ego aracnídeo em ação, Peter Parker "
				+ "sai em busca de problemas – e os encontra na forma de uma placa de pedra misteriosa e "
				+ "mítica cobiçada pelo Rei do Crime e pelos facínoras da Maggia, o maior sindicato criminal "
				+ "da cidade.");
		Assertions.assertThat(foundComic.getPrice()).isEqualTo(38.61F);
		Assertions.assertThat(foundComic.getCharacters().stream().findFirst().get().getName()).isEqualTo("Homem Aranha");
		Assertions.assertThat(foundComic.getAuthors().stream().findFirst().get().getName()).isEqualTo("Stefan Petrucha");
	}
	
	private Comic createComic() {
		Author author = new Author(null, "Stefan Petrucha");
		Character character = new Character(null, "Homem Aranha");
		
		Comic comic = new Comic(null, "Homem-Aranha: Eternamente jovem", 38.61F, "9786555612752", 
				"Na esperança de obter algumas fotos de seu alter "
				+ "ego aracnídeo em ação, Peter Parker "
				+ "sai em busca de problemas – e os encontra na forma de uma placa de pedra misteriosa e "
				+ "mítica cobiçada pelo Rei do Crime e pelos facínoras da Maggia, o maior sindicato criminal "
				+ "da cidade.");
		
		comic.getAuthors().add(author);
		comic.getCharacters().add(character);
		
		return comic;
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