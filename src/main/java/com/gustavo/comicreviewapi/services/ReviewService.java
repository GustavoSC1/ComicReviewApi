package com.gustavo.comicreviewapi.services;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.gustavo.comicreviewapi.dtos.ReviewDTO;
import com.gustavo.comicreviewapi.dtos.ReviewNewDTO;
import com.gustavo.comicreviewapi.dtos.ReviewUpdateDTO;
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
		
		Review review = new Review(null, reviewDto.getTitle(), getDateTime(), reviewDto.getContent(), user, comic);
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
	
	public ReviewDTO update(Long id, ReviewUpdateDTO reviewDto) {
		Review review = findById(id);
		
		review.setTitle(reviewDto.getTitle());
		review.setContent(reviewDto.getContent());
		review.setDate(getDateTime());
		
		review = reviewRepository.save(review);
		
		return new ReviewDTO(review);		
	}
	
	public LocalDateTime getDateTime() {
		return LocalDateTime.now();
	}

}
