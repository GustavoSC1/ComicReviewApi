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
import com.gustavo.comicreviewapi.dtos.ReadingNewDTO;
import com.gustavo.comicreviewapi.entities.Comic;
import com.gustavo.comicreviewapi.entities.Reading;
import com.gustavo.comicreviewapi.entities.User;
import com.gustavo.comicreviewapi.repositories.ReadingRepository;
import com.gustavo.comicreviewapi.security.UserSS;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class ReadingServiceTest {
	
	ReadingService readingService;
	
	@MockBean
	ReadingRepository readingRepository;
	
	@MockBean
	ComicService comicService;
	
	@BeforeEach
	public void setUp() {
		this.readingService = new ReadingService(readingRepository, comicService);
	}
	
	@Test
	@DisplayName("Must save a reading")
	public void saveReadingTest() {
		try(MockedStatic<UserService> mockedStatic = Mockito.mockStatic(UserService.class)) {
			// Scenario
			Long id = 2l;
			
			User user = UserBuilder.aUser().withId(id).now();
			Comic comic = ComicBuilder.aComic().withId(id).now();
			UserSS userSS = new UserSS(id, user.getEmail(), user.getPassword(), user.getProfiles());
			
			ReadingNewDTO newReading = new ReadingNewDTO(true, true);
			
			mockedStatic.when(UserService::authenticated).thenReturn(userSS);
			
			Mockito.when(comicService.findById(id)).thenReturn(comic);
						
			// Execution
			readingService.save(id, newReading);
			
			// Verification
			Mockito.verify(readingRepository, Mockito.times(1)).save(Mockito.any(Reading.class));
		}
	}
	
}
