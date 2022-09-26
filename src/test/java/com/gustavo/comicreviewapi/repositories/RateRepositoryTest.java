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
import com.gustavo.comicreviewapi.entities.Rate;
import com.gustavo.comicreviewapi.entities.User;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
public class RateRepositoryTest {
	
	@Autowired
	RateRepository rateRepository;
		
	@Test
	@DisplayName("Must save a rate")
	public void saveRateTest() {
		// Scenario
		Comic comic = new Comic();
		comic.setId(1l);
		User user = new User();
		user.setId(1l);
		Rate rate = new Rate(user, comic, 4);
		
		// Execution
		Rate savedRate = rateRepository.save(rate);
		
		// Verification
		Assertions.assertThat(savedRate.getId().getUser().getId()).isNotNull();
		Assertions.assertThat(savedRate.getId().getComic().getId()).isNotNull();
		Assertions.assertThat(savedRate.getRate()).isEqualTo(4);
	}
		
}
