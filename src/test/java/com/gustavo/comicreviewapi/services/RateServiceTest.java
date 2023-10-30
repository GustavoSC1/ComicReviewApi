package com.gustavo.comicreviewapi.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.gustavo.comicreviewapi.builders.ComicBuilder;
import com.gustavo.comicreviewapi.builders.UserBuilder;
import com.gustavo.comicreviewapi.dtos.RateNewDTO;
import com.gustavo.comicreviewapi.entities.Comic;
import com.gustavo.comicreviewapi.entities.Rate;
import com.gustavo.comicreviewapi.entities.User;
import com.gustavo.comicreviewapi.repositories.RateRepository;
import com.gustavo.comicreviewapi.utils.UserSS;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class RateServiceTest {
	
	RateService rateService;
	
	@MockBean
	RateRepository rateRepository;
		
	@MockBean
	ComicService comicService;
	
	@BeforeEach
	public void setUp() {
		this.rateService = new RateService(rateRepository, comicService);
	}
	
	@Test
	@DisplayName("Must save a rate")
	public void saveRateTest() {
		try(MockedStatic<UserService> mockedStatic = Mockito.mockStatic(UserService.class)) {
			// Scenario
			Long id = 2l;
			
			User user = UserBuilder.aUser().withId(id).now();
			Comic comic = ComicBuilder.aComic().withId(id).now();
			UserSS userSS = new UserSS(id, user.getEmail(), user.getPassword(), user.getProfiles());
			
			RateNewDTO newRate = new RateNewDTO(4);
						
			mockedStatic.when(UserService::authenticated).thenReturn(userSS);
						
			Mockito.when(comicService.findById(id)).thenReturn(comic);
						
			// Execution
			rateService.save(id, newRate);
			
			// Verification
			Mockito.verify(rateRepository, Mockito.times(1)).save(Mockito.any(Rate.class));
		}
	}

}
