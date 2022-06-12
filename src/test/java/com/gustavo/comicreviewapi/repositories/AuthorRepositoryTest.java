package com.gustavo.comicreviewapi.repositories;

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

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
public class AuthorRepositoryTest {
	
	@Autowired
	AuthorRepository authorRepository;
	
	@Autowired
	TestEntityManager entityManager;
	
	@Test
	@DisplayName("Must get author user per name")
	public void findByNameTest() {
		// Scenario
		Author author = new Author(null, "Stefan Petrucha");
		entityManager.persist(author);
		
		// Execution
		Author foundAuthor = authorRepository.findByName(author.getName());
		
		// Verification
		Assertions.assertThat(foundAuthor.getId()).isNotNull();
		Assertions.assertThat(foundAuthor.getName()).isEqualTo("Stefan Petrucha");
	}

}
