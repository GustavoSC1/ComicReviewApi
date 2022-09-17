package com.gustavo.comicreviewapi.services;

import java.time.Clock;
import java.util.Calendar;
import java.util.Date;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.gustavo.comicreviewapi.builders.ComicBuilder;
import com.gustavo.comicreviewapi.builders.ComicDtoBuilder;
import com.gustavo.comicreviewapi.builders.FeignComicDtoBuilder;
import com.gustavo.comicreviewapi.builders.FeignMarvelAPIModelDtoBuilder;
import com.gustavo.comicreviewapi.dtos.AuthorDTO;
import com.gustavo.comicreviewapi.dtos.CharacterDTO;
import com.gustavo.comicreviewapi.dtos.ComicDTO;
import com.gustavo.comicreviewapi.dtos.ComicNewDTO;
import com.gustavo.comicreviewapi.dtos.feignDtos.CharacterSummaryDTO;
import com.gustavo.comicreviewapi.dtos.feignDtos.ComicPriceDTO;
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
		com.gustavo.comicreviewapi.dtos.feignDtos.ComicDTO comicDTO = FeignComicDtoBuilder.aFeignComicDTO().withCharactersList(new CharacterSummaryDTO("Homem Aranha"))
				.withCreatorsList(new CreatorSummaryDTO("Stefan Petrucha")).withPrice(new ComicPriceDTO(38.61F)).withId(1).now();
			
		MarvelAPIModelDTO foundMarvelAPIModelDTO = FeignMarvelAPIModelDtoBuilder.aMarvelAPIModelDTO().withComicDtoList(comicDTO).now();
				
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
		com.gustavo.comicreviewapi.dtos.feignDtos.ComicDTO comicDTO = FeignComicDtoBuilder.aFeignComicDTO().withCharactersList(new CharacterSummaryDTO("Homem Aranha"))
			.withCreatorsList(new CreatorSummaryDTO("Stefan Petrucha")).withPrice(new ComicPriceDTO(38.61F)).withId(1).now();
		
		MarvelAPIModelDTO foundMarvelAPIModelDTO = FeignMarvelAPIModelDtoBuilder.aMarvelAPIModelDTO().withComicDtoList(comicDTO).now();
						
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
		
		Comic comic = ComicBuilder.aComic().withAuthorsList(new Author(null, "Stefan Petrucha"))
				.withCharactersList(new Character(null, "Homem Aranha")).withId(Long.valueOf(id)).now();
		
		Mockito.doReturn(comic).when(comicService).fromDTO(comicNewDto);
		Mockito.when(comicRepository.save(Mockito.any(Comic.class))).thenReturn(comic);
		
		// Execution
		ComicDTO savedComic = comicService.save(comicNewDto);
		
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
		
		Comic comic = ComicBuilder.aComic().withAuthorsList(new Author(null, "Stefan Petrucha"))
				.withCharactersList(new Character(null, "Homem Aranha")).withId(id).now();
		
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
		
		Comic comic = ComicBuilder.aComic().withAuthorsList(new Author(null, "Stefan Petrucha"))
				.withCharactersList(new Character(null, "Homem Aranha")).withId(id).now();
		
		Mockito.doReturn(obterData(16,11,2022)).when(comicService).getDate();
		
		Mockito.doReturn(comic).when(comicService).findById(id);
		
		// Execution
		ComicDTO foundComic = comicService.find(id);
		
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
		Assertions.assertThat(foundComic.getActiveDiscount()).isEqualTo(false);
	}
	
	@Test
	@DisplayName("Must calculate a discount on the Comic price based on the last ISBN number and the day of the week the "
			+ "user is making the request")
	public void checkDiscountTest() {
		// Scenario
		ComicDTO comicDto = ComicDtoBuilder.aComicDTO().withCharactersDtoList(new CharacterDTO(null, "Homem Aranha"))
				.withAuthorsList(new AuthorDTO(null, "Stefan Petrucha")).withId(Long.valueOf(2l)).now();
						
		Mockito.doReturn(obterData(15,11,2022)).when(comicService).getDate();
		
		// Execution
		comicService.checkDiscount(comicDto);
		
		// Verification
		Assertions.assertThat(comicDto.getId()).isEqualTo(2);
		Assertions.assertThat(comicDto.getTitle()).isEqualTo("Homem-Aranha: Eternamente jovem");
		Assertions.assertThat(comicDto.getIsbn()).isEqualTo("9786555612752");
		Assertions.assertThat(comicDto.getDescription()).isEqualTo("Na esperança de obter algumas fotos de seu alter "
				+ "ego aracnídeo em ação, Peter Parker "
				+ "sai em busca de problemas – e os encontra na forma de uma placa de pedra misteriosa e "
				+ "mítica cobiçada pelo Rei do Crime e pelos facínoras da Maggia, o maior sindicato criminal "
				+ "da cidade.");
		Assertions.assertThat(comicDto.getCharacters().stream().findFirst().get().getName()).isEqualTo("Homem Aranha");
		Assertions.assertThat(comicDto.getAuthors().stream().findFirst().get().getName()).isEqualTo("Stefan Petrucha");
		Assertions.assertThat(comicDto.getPrice()).isEqualTo(34.75F);
		Assertions.assertThat(comicDto.getActiveDiscount()).isEqualTo(true);
	}
	
	@Test
	@DisplayName("Must filter comics")
	public void findByTitleTest() {
		// Scenario
		Long id = 2l;
		
		Comic comic = ComicBuilder.aComic().withAuthorsList(new Author(null, "Stefan Petrucha"))
				.withCharactersList(new Character(null, "Homem Aranha")).withId(id).now();
		
		List<Comic> list = Arrays.asList(comic);
		
			// Informações de paginação
		PageRequest pageRequest = PageRequest.of(0, 24);//(pagina, quantidade maxima de elementos retornados na pagina)
		
			// PageImpl: Cria uma página utilziando uma lista 
		Page<Comic> page = new PageImpl<Comic>(list, pageRequest, list.size()); //(conteúdo desta página, informações de paginação, quantidade total de resultados encontrados)
		
		Mockito.when(comicRepository.findByTitle(Mockito.anyString(), Mockito.any(PageRequest.class))).thenReturn(page);
		
		// Execution
		Page<ComicDTO> foundComics = comicService.findByTitle("Eternamente", 0, 24, "title", "ASC");
		
		// Verification
		Assertions.assertThat(foundComics.getTotalElements()).isEqualTo(1);		
		Assertions.assertThat(foundComics.getPageable().getPageNumber()).isEqualTo(0);
		Assertions.assertThat(foundComics.getPageable().getPageSize()).isEqualTo(24);		
		Assertions.assertThat(foundComics.getContent().get(0).getId()).isEqualTo(id);
		Assertions.assertThat(foundComics.getContent().get(0).getTitle()).isEqualTo(comic.getTitle());
		Assertions.assertThat(foundComics.getContent().get(0).getPrice()).isEqualTo(comic.getPrice());
		Assertions.assertThat(foundComics.getContent().get(0).getDescription()).isEqualTo(comic.getDescription());
	}
	
	@Test
	@DisplayName("Must delete a comic")
	public void deleteComicTest() {
		// Scenario
		Long id = 2l;
		
		Comic foundComic = ComicBuilder.aComic().withId(id).now();
		
		Mockito.doReturn(foundComic).when(comicService).findById(id);
		
		// Execution
		comicService.delete(id);
		
		// Verification
		Mockito.verify(comicRepository, Mockito.times(1)).delete(foundComic);
	}
	
	public static Date obterData(int day, int mounth, int year){
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, day);
		calendar.set(Calendar.MONTH, mounth - 1);
		calendar.set(Calendar.YEAR, year);
		return calendar.getTime();
	}

}
