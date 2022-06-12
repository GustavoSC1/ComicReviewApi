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

import com.gustavo.comicreviewapi.entities.Character;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
public class CharacterRepositoryTest {
	
	@Autowired
	CharacterRepository characterRepository;
	
	@Autowired
	TestEntityManager entityManager;
	
	@Test
	@DisplayName("Must get one character per name")
	public void findByNameTest() {
		// Scenario
		Character character = new Character(null, "Homem Aranha");
		entityManager.persist(character);
		
		// Execution
		Character foundCharacter = characterRepository.findByName(character.getName());
		
		// Verification
		Assertions.assertThat(foundCharacter.getId()).isNotNull();
		Assertions.assertThat(foundCharacter.getName()).isEqualTo("Homem Aranha");		
	}

}
