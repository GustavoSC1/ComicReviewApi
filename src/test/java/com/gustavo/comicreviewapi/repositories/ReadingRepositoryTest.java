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
import com.gustavo.comicreviewapi.entities.Reading;
import com.gustavo.comicreviewapi.entities.User;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
public class ReadingRepositoryTest {
	
	@Autowired
	ReadingRepository readingRepository;
	
	@Test
	@DisplayName("Must save a reading")
	public void saveReadingTest() {
		// Scenario
		User user = new User();
		user.setId(1l);		
		Comic comic = new Comic();
		comic.setId(1l);
		Reading reading = new Reading(user, comic, true, true);
		
		// Execution
		Reading savedReading = readingRepository.save(reading);
		
		// Verification
		Assertions.assertThat(savedReading.getId().getUser().getId()).isNotNull();
		Assertions.assertThat(savedReading.getId().getComic().getId()).isNotNull();
		Assertions.assertThat(savedReading.getReading()).isEqualTo(true);
		Assertions.assertThat(savedReading.getFavourit()).isEqualTo(true);
	}

}
