package com.gustavo.comicreviewapi.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gustavo.comicreviewapi.entities.Like;
import com.gustavo.comicreviewapi.entities.LikePK;
import com.gustavo.comicreviewapi.entities.Review;
import com.gustavo.comicreviewapi.entities.User;
import com.gustavo.comicreviewapi.repositories.LikeRepository;

@Service
public class LikeService {
	
	@Autowired
	private LikeRepository likeRepository;
	
	public LikeService(LikeRepository likeRepository) {
		this.likeRepository = likeRepository;
	}
	
	public Like findById(User user, Review review) {
		Optional<Like> likeOptional = likeRepository.findById(new LikePK(user, review));
		
		return likeOptional.orElse(null);
	}

}
