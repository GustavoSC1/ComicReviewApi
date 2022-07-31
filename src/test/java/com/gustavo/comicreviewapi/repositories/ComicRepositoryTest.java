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
				.withCharactersList(new Character(null, "Homem Aranha")).withId(1l).now();
		
		entityManager.persist(comic);
		
		// Execution
		Optional<Comic> foundComic = comicRepository.findById(id);
		
		// Verification
		Assertions.assertThat(foundComic.isPresent()).isTrue();
	}	

}
