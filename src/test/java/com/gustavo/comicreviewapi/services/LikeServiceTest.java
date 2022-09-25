package com.gustavo.comicreviewapi.services;

import java.util.Optional;

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

import com.gustavo.comicreviewapi.builders.ReviewBuilder;
import com.gustavo.comicreviewapi.builders.UserBuilder;
import com.gustavo.comicreviewapi.dtos.LikeNewDTO;
import com.gustavo.comicreviewapi.entities.Like;
import com.gustavo.comicreviewapi.entities.LikePK;
import com.gustavo.comicreviewapi.entities.Review;
import com.gustavo.comicreviewapi.entities.User;
import com.gustavo.comicreviewapi.repositories.LikeRepository;
import com.gustavo.comicreviewapi.security.UserSS;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class LikeServiceTest {
	
	LikeService likeService;
	
	@MockBean
	LikeRepository likeRepository;
	
	@MockBean
	UserService userService;
	
	@MockBean
	ReviewService reviewService;
	
	@BeforeEach
	public void setUp() {
		this.likeService = Mockito.spy(new LikeService(likeRepository, userService, reviewService));
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
			
			Mockito.when(userService.findById(id)).thenReturn(user);
			
			Mockito.when(reviewService.findById(id)).thenReturn(review);
			
			Mockito.doReturn(null).when(likeService).findById(user, review);
			
			// Execution
			likeService.save(id, newLike);
			
			// Verification
			Mockito.verify(likeRepository, Mockito.times(1)).save(Mockito.any(Like.class));
		}
	}
	
	@Test
	@DisplayName("Must get one like per composite id")
	public void findByIdTest() {
		// Scenario
		Long id = 2l;
		
		User user = UserBuilder.aUser().withId(id).now();
		Review review = ReviewBuilder.aReview().withId(id).now();
		
		Like like = new Like(user, review, true);
		
		Mockito.when(likeRepository.findById(Mockito.any(LikePK.class))).thenReturn(Optional.of(like));
		
		// Execution
		Like foundLike = likeService.findById(user, review);
		
		// Verification
		Assertions.assertThat(foundLike.getId().getUser().getId()).isEqualTo(id);
		Assertions.assertThat(foundLike.getId().getReview().getId()).isEqualTo(id);
		Assertions.assertThat(foundLike.getLiked()).isEqualTo(true);
	}

}