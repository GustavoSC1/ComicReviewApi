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

import com.gustavo.comicreviewapi.entities.Character;
import com.gustavo.comicreviewapi.repositories.CharacterRepository;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class CharacterServiceTest {
	
	CharacterService characterService;
	
	@MockBean
	CharacterRepository characterRepository;
	
	@BeforeEach
	public void setUp() {
		this.characterService = new CharacterService(characterRepository);
	}
	
	@Test
	@DisplayName("Must get one character per name")
	public void findByNameTest() {
		// Scenario
		String name = "Homem Aranha";
		
		Character character = new Character(1l, name);
		
		Mockito.when(characterRepository.findByName(name)).thenReturn(character);
		
		// Execution
		Character foundCharacter = characterService.findByName(name);
		
		// Verification
		Assertions.assertThat(foundCharacter.getId()).isEqualTo(1l);
		Assertions.assertThat(foundCharacter.getName()).isEqualTo("Homem Aranha");
	}
	
}
