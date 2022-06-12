package com.gustavo.comicreviewapi.repositories;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.gustavo.comicreviewapi.entities.Comic;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
public class ComicRepositoryTest {
	
	@Autowired
	ComicRepository comicRepository;
	
	@Test
	@DisplayName("Must save a comic")
	public void saveComicTest() {
		// Scenario
		Comic newComic = new Comic(1l, "Homem-Aranha: Eternamente jovem", 38.61F, "9786555612752", 
				"Na esperança de obter algumas fotos de seu alter ego aracnídeo em ação, Peter Parker "
				+ "sai em busca de problemas – e os encontra na forma de uma placa de pedra misteriosa e "
				+ "mítica cobiçada pelo Rei do Crime e pelos facínoras da Maggia, o maior sindicato criminal "
				+ "da cidade.");
		
		// Execution
		Comic savedComic = comicRepository.save(newComic);
		
		// Verification
		Assertions.assertThat(savedComic.getId()).isEqualTo(1l);
	}

}
