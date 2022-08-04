package com.gustavo.comicreviewapi.repositories;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.gustavo.comicreviewapi.builders.ComicBuilder;
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
		Long id = 1l;
		
		Comic newComic = ComicBuilder.aComic().withAuthorsList(new Author(null, "Stefan Petrucha"))
				.withCharactersList(new Character(null, "Homem Aranha")).withId(id).now();
		
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
		
		Comic comic = ComicBuilder.aComic().withAuthorsList(new Author(null, "Stefan Petrucha"))
				.withCharactersList(new Character(null, "Homem Aranha")).withId(id).now();
		
		entityManager.persist(comic);
		
		// Execution
		Optional<Comic> foundComic = comicRepository.findById(id);
		
		// Verification
		Assertions.assertThat(foundComic.isPresent()).isTrue();
	}	
	
	@Test
	@DisplayName("Must filter comics")
	public void findByTitleTest() {
		// Scenario
		Long id = 1l;
		
		PageRequest pageRequest = PageRequest.of(0, 24, Direction.valueOf("ASC"), "title");
		
		Comic comic = ComicBuilder.aComic().withAuthorsList(new Author(null, "Stefan Petrucha"))
				.withCharactersList(new Character(null, "Homem Aranha")).withId(id).now();
		
		entityManager.persist(comic);
		
		// Execution
		Page<Comic> foundComics = comicRepository.findByTitle("Eternamente", pageRequest);
		
		// Verification
		Assertions.assertThat(foundComics.getNumberOfElements()).isEqualTo(1);
		Assertions.assertThat(foundComics.getTotalElements()).isEqualTo(1);
		Assertions.assertThat(foundComics.getTotalPages()).isEqualTo(1);
	}

}
