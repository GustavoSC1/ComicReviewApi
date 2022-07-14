package com.gustavo.comicreviewapi.services;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.gustavo.comicreviewapi.dtos.ReviewDTO;
import com.gustavo.comicreviewapi.dtos.ReviewNewDTO;
import com.gustavo.comicreviewapi.entities.Comic;
import com.gustavo.comicreviewapi.entities.Review;
import com.gustavo.comicreviewapi.entities.User;
import com.gustavo.comicreviewapi.repositories.ReviewRepository;
import com.gustavo.comicreviewapi.services.exceptions.ObjectNotFoundException;

@Service
public class ReviewService {
	
	private ReviewRepository reviewRepository;
	
	private UserService userService;
	
	private ComicService comicService;

	public ReviewService(ReviewRepository reviewRepository, UserService userService, ComicService comicService) {		
		this.reviewRepository = reviewRepository;
		this.userService = userService;
		this.comicService = comicService;
	}

	public ReviewDTO save(ReviewNewDTO reviewDto) {
		User user = userService.findById(reviewDto.getUserId());
		Comic comic = comicService.findById(reviewDto.getComicId());
		
		Review review = new Review(null, reviewDto.getTitle(), reviewDto.getDate(), reviewDto.getContent(), user, comic);
		review = reviewRepository.save(review);
		
		return new ReviewDTO(review);
	}
	
	public ReviewDTO find(Long id) {
		Review review = findById(id);
		
		return new ReviewDTO(review);
	}
		
	public Review findById(Long id) {
		Optional<Review> reviewOptional = reviewRepository.findById(id);
		Review review = reviewOptional.orElseThrow(() -> new ObjectNotFoundException("Object not found! Id: " + id + ", Type: " + Review.class.getName()));
	
		return review;
	}

}
