package com.gustavo.comicreviewapi.services;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.gustavo.comicreviewapi.entities.Author;
import com.gustavo.comicreviewapi.repositories.AuthorRepository;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class AuthorServiceTest {
	
	AuthorService authorService;
	
	@MockBean
	AuthorRepository authorRepository;
	
	@BeforeEach
	public void setUp() {
		this.authorService = new AuthorService(authorRepository);
	}
	
	@Test
	@DisplayName("Must get one author per name")
	public void findByNameTest() {
		// Scenario
		String name = "Stefan Petrucha";
		
		Author author = new Author(1l, "Stefan Petrucha");
		
		Mockito.when(authorRepository.findByName(name)).thenReturn(author);
		
		// Execution
		Author foundAuthor = authorService.findByName(name);
		
		// Verification
		Assertions.assertThat(foundAuthor.getId()).isEqualTo(1l);
		Assertions.assertThat(foundAuthor.getName()).isEqualTo(name);
	}

}
