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

import com.gustavo.comicreviewapi.entities.Comic;
import com.gustavo.comicreviewapi.entities.Rate;
import com.gustavo.comicreviewapi.entities.RatePK;
import com.gustavo.comicreviewapi.entities.User;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
public class RateRepositoryTest {
	
	@Autowired
	RateRepository rateRepository;
	
	@Autowired
	TestEntityManager entityManager;
	
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
	}
	
	@Test
	@DisplayName("Must get one rate per composite id")
	public void findByIdTest() {
		// Scenario
		Comic comic = new Comic();
		comic.setId(1l);
		User user = new User();
		user.setId(1l);
		Rate rate = new Rate(user, comic, 4);
		entityManager.persist(rate);
		
		// Execution
		Optional<Rate> foundRate = rateRepository.findById(new RatePK(user, comic));
		
		// Verification
		Assertions.assertThat(foundRate.isPresent()).isTrue();
	}
		
}
