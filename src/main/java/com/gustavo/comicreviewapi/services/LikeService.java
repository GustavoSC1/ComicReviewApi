package com.gustavo.comicreviewapi.services;

import org.springframework.stereotype.Service;

import com.gustavo.comicreviewapi.dtos.LikeNewDTO;
import com.gustavo.comicreviewapi.entities.Like;
import com.gustavo.comicreviewapi.entities.Review;
import com.gustavo.comicreviewapi.entities.User;
import com.gustavo.comicreviewapi.repositories.LikeRepository;
import com.gustavo.comicreviewapi.security.UserSS;
import com.gustavo.comicreviewapi.services.exceptions.AuthorizationException;

@Service
public class LikeService {
	
	private LikeRepository likeRepository;
	private ReviewService reviewService;
	
	public LikeService(LikeRepository likeRepository, ReviewService reviewService) {
		this.likeRepository = likeRepository;
		this.reviewService = reviewService;
	}
	
	public void save(Long reviewId, LikeNewDTO likeDto) {
		
		UserSS userAuthenticated = UserService.authenticated();
		
		if(userAuthenticated==null) {
			throw new AuthorizationException("Access denied");
		}
		
		User user = new User();
		user.setId(userAuthenticated.getId());
		
		Review review = reviewService.findById(reviewId);
		
		Like like = new Like(user, review, likeDto.getLiked());
				
		likeRepository.save(like);	
	}

}
