package com.gustavo.comicreviewapi.repositories;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.gustavo.comicreviewapi.entities.Like;
import com.gustavo.comicreviewapi.entities.Review;
import com.gustavo.comicreviewapi.entities.User;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
public class LikeRepositoryTest {
	
	@Autowired
	LikeRepository likeRepository;
	
	@Test
	@DisplayName("Must save a like")
	public void saveLikeTest() {
		// Scenario
		User user = new User();
		user.setId(1l);
		Review review = new Review();
		review.setId(1l);
		Like like = new Like(user, review, true);
		
		// Execution
		Like savedLike = likeRepository.save(like);
		
		// Verification
		Assertions.assertThat(savedLike.getId().getUser().getId()).isNotNull();
		Assertions.assertThat(savedLike.getId().getReview().getId()).isNotNull();
		Assertions.assertThat(savedLike.getLiked()).isEqualTo(true);
	}

}
