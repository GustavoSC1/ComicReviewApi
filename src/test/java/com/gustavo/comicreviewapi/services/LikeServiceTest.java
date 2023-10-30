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

import com.gustavo.comicreviewapi.builders.ReviewBuilder;
import com.gustavo.comicreviewapi.builders.UserBuilder;
import com.gustavo.comicreviewapi.dtos.LikeNewDTO;
import com.gustavo.comicreviewapi.entities.Like;
import com.gustavo.comicreviewapi.entities.Review;
import com.gustavo.comicreviewapi.entities.User;
import com.gustavo.comicreviewapi.repositories.LikeRepository;
import com.gustavo.comicreviewapi.utils.UserSS;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class LikeServiceTest {
	
	LikeService likeService;
	
	@MockBean
	LikeRepository likeRepository;
		
	@MockBean
	ReviewService reviewService;
	
	@BeforeEach
	public void setUp() {
		this.likeService = new LikeService(likeRepository, reviewService);
	}
	
	@Test
	@DisplayName("Must save a like")
	public void saveLikeTest() {
		try(MockedStatic<UserService> mockedStatic = Mockito.mockStatic(UserService.class)) {
			// Scenario
			Long id = 2l;
			
			User user = UserBuilder.aUser().withId(id).now();
			Review review = ReviewBuilder.aReview().withId(id).now();
			UserSS userSS = new UserSS(id, user.getEmail(), user.getPassword(), user.getProfiles());
			
			LikeNewDTO newLike = new LikeNewDTO(true);
			
			mockedStatic.when(UserService::authenticated).thenReturn(userSS);
						
			Mockito.when(reviewService.findById(id)).thenReturn(review);
			
			// Execution
			likeService.save(id, newLike);
			
			// Verification
			Mockito.verify(likeRepository, Mockito.times(1)).save(Mockito.any(Like.class));
		}
	}

}
