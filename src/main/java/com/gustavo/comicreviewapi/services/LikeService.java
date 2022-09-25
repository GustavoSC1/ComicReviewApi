package com.gustavo.comicreviewapi.services;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.gustavo.comicreviewapi.dtos.LikeNewDTO;
import com.gustavo.comicreviewapi.entities.Like;
import com.gustavo.comicreviewapi.entities.LikePK;
import com.gustavo.comicreviewapi.entities.Review;
import com.gustavo.comicreviewapi.entities.User;
import com.gustavo.comicreviewapi.repositories.LikeRepository;
import com.gustavo.comicreviewapi.security.UserSS;
import com.gustavo.comicreviewapi.services.exceptions.AuthorizationException;

@Service
public class LikeService {
	
	private LikeRepository likeRepository;
	private UserService userService;
	private ReviewService reviewService;
	
	public LikeService(LikeRepository likeRepository, UserService userService, ReviewService reviewService) {
		this.likeRepository = likeRepository;
		this.userService = userService;
		this.reviewService = reviewService;
	}
	
	public void save(Long reviewId, LikeNewDTO likeDto) {
		
		UserSS userAuthenticated = UserService.authenticated();
		
		if(userAuthenticated==null) {
			throw new AuthorizationException("Access denied");
		}
		
		User user = userService.findById(userAuthenticated.getId());
		
		Review review = reviewService.findById(reviewId);
		
		Like like = findById(user, review);
		
		if(like == null) {
			like = new Like(user, review, likeDto.getLiked());
		} else {
			like.setLiked(likeDto.getLiked());
		}
		
		likeRepository.save(like);	
	}
	
	public Like findById(User user, Review review) {
		Optional<Like> likeOptional = likeRepository.findById(new LikePK(user, review));
		
		return likeOptional.orElse(null);
	}

}
