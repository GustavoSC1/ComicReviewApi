package com.gustavo.comicreviewapi.services;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import org.assertj.core.api.Assertions;
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
import com.gustavo.comicreviewapi.entities.RatePK;
import com.gustavo.comicreviewapi.entities.User;
import com.gustavo.comicreviewapi.repositories.RateRepository;
import com.gustavo.comicreviewapi.security.UserSS;
import com.gustavo.comicreviewapi.services.exceptions.BusinessException;

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
		this.rateService = Mockito.spy(new RateService(rateRepository, userService, comicService));
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
			
			RateNewDTO newRate = new RateNewDTO(id, 4);
						
			mockedStatic.when(UserService::authenticated).thenReturn(userSS);
			
			Mockito.when(userService.findById(id)).thenReturn(user);
			
			Mockito.when(comicService.findById(id)).thenReturn(comic);
			
			Mockito.doReturn(null).when(rateService).findById(user, comic);
			
			// Execution
			rateService.save(id, newRate);
			
			// Verification
			Mockito.verify(rateRepository, Mockito.times(1)).save(Mockito.any(Rate.class));
		}
	}
	
	@Test
	@DisplayName("Should throw business error when trying to rate a comic more than once")
	public void shouldNotRateAComicMoreThanOnce() {
		// Scenario
		Long id = 2l;
		
		User user = UserBuilder.aUser().withId(id).now();
		Comic comic = ComicBuilder.aComic().withId(id).now();
		
		Rate rate = new Rate(user, comic, 4);
		
		RateNewDTO newRate = new RateNewDTO(id, 4);
		
		Mockito.when(userService.findById(id)).thenReturn(user);
		
		Mockito.when(comicService.findById(id)).thenReturn(comic);
		
		Mockito.doReturn(rate).when(rateService).findById(user, comic);
		
		// Execution and Verification
		Exception exception = assertThrows(BusinessException.class, () -> {rateService.save(id, newRate);});
		
		String expectedMessage = "User has already rated this comic!";
		String actualMessage = exception.getMessage();
		
		Assertions.assertThat(actualMessage).isEqualTo(expectedMessage);
		Mockito.verify(rateRepository, Mockito.never()).save(Mockito.any(Rate.class));
	}
	
	@Test
	@DisplayName("Must get one rate per composite id")
	public void findByIdTest() {
		// Scenario
		Long id = 2l;
		
		User user = UserBuilder.aUser().withId(id).now();
		Comic comic = ComicBuilder.aComic().withId(id).now();
		
		Rate rate = new Rate(user, comic, 4);
		
		Mockito.when(rateRepository.findById(Mockito.any(RatePK.class))).thenReturn(Optional.of(rate));
		
		// Execution
		Rate foundRate = rateService.findById(user, comic);
		
		// Verification		
		Assertions.assertThat(foundRate.getId().getUser().getId()).isEqualTo(id);
		Assertions.assertThat(foundRate.getId().getComic().getId()).isEqualTo(id);
		Assertions.assertThat(foundRate.getRate()).isEqualTo(4);
	}

}
