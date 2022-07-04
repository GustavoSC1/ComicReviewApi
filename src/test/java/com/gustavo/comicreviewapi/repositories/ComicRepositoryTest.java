package com.gustavo.comicreviewapi.repositories;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.gustavo.comicreviewapi.entities.Author;
import com.gustavo.comicreviewapi.entities.Character;
import com.gustavo.comicreviewapi.entities.Comic;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
public class ComicRepositoryTest {
	
	@Autowired
	ComicRepository comicRepository;
	
	@Autowired
	TestEntityManager entityManager;
	
	@Test
	@DisplayName("Must save a comic")
	public void saveComicTest() {
		// Scenario
		Comic newComic = createComic();
		newComic.setId(1l);
		
		// Execution
		Comic savedComic = comicRepository.save(newComic);
		
		// Verification
		Assertions.assertThat(savedComic.getId()).isEqualTo(1l);
	}
	
	@Test
	@DisplayName("Must get one comic per id")
	public void findByIdTest() {
		// Scenario
		Long id = 1l;
		Comic comic = createComic();
		comic.setId(id);
		entityManager.persist(comic);
		
		// Execution
		Optional<Comic> foundComic = comicRepository.findById(id);
		
		// Verification
		Assertions.assertThat(foundComic.isPresent()).isTrue();
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

}
