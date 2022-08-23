package com.gustavo.comicreviewapi.services;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.gustavo.comicreviewapi.builders.ComicBuilder;
import com.gustavo.comicreviewapi.builders.UserBuilder;
import com.gustavo.comicreviewapi.entities.Comic;
import com.gustavo.comicreviewapi.entities.Rate;
import com.gustavo.comicreviewapi.entities.RatePK;
import com.gustavo.comicreviewapi.entities.User;
import com.gustavo.comicreviewapi.repositories.RateRepository;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class RateServiceTest {
	
	RateService rateService;
	
	@MockBean
	RateRepository rateRepository;
	
	@MockBean
	UserService userService;
	
	@MockBean
	ComicService comicService;
	
	@BeforeEach
	public void setUp() {
		this.rateService = new RateService(rateRepository, userService, comicService);
	}
	
	@Test
	@DisplayName("Must get one rate per composite id")
	public void findByIdTest() {
		// Scenario
		Long id = 2l;
		
		User user = UserBuilder.aUser().withId(id).now();
		Comic comic = ComicBuilder.aComic().withId(id).now();
		
		Rate rate = new Rate(user, comic, 4);
		
		Mockito.when(userService.findById(id)).thenReturn(user);
		Mockito.when(comicService.findById(id)).thenReturn(comic);
		Mockito.when(rateRepository.findById(Mockito.any(RatePK.class))).thenReturn(Optional.of(rate));
		
		// Execution
		Rate foundRate = rateService.findById(id, id);
		
		// Verification		
		Assertions.assertThat(foundRate.getId().getUser().getId()).isEqualTo(id);
		Assertions.assertThat(foundRate.getId().getComic().getId()).isEqualTo(id);
		Assertions.assertThat(foundRate.getRate()).isEqualTo(4);
	}

}
