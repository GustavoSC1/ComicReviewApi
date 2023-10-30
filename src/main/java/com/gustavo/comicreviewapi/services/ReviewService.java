package com.gustavo.comicreviewapi.services;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gustavo.comicreviewapi.dtos.ReviewDTO;
import com.gustavo.comicreviewapi.dtos.ReviewNewDTO;
import com.gustavo.comicreviewapi.dtos.ReviewUpdateDTO;
import com.gustavo.comicreviewapi.entities.Comic;
import com.gustavo.comicreviewapi.entities.Review;
import com.gustavo.comicreviewapi.entities.User;
import com.gustavo.comicreviewapi.entities.enums.Profile;
import com.gustavo.comicreviewapi.repositories.ReviewRepository;
import com.gustavo.comicreviewapi.services.exceptions.AuthorizationException;
import com.gustavo.comicreviewapi.services.exceptions.ObjectNotFoundException;
import com.gustavo.comicreviewapi.utils.UserSS;

@Service
public class ReviewService {
	
	private ReviewRepository reviewRepository;
		
	private ComicService comicService;

	public ReviewService(ReviewRepository reviewRepository, ComicService comicService) {		
		this.reviewRepository = reviewRepository;
		this.comicService = comicService;
	}

	public ReviewDTO save(ReviewNewDTO reviewDto) {

		UserSS userAuthenticated = UserService.authenticated();	
				
		User user = new User();
		
		user.setId(userAuthenticated.getId());
		
		Comic comic = comicService.findById(reviewDto.getComicId());
		
		Review review = new Review(null, reviewDto.getTitle(), getDateTime(), reviewDto.getContent(), user, comic);
		review = reviewRepository.save(review);
		
		return new ReviewDTO(review);
	}
	
	@Transactional(readOnly = true)
	public ReviewDTO find(Long id) {
		Review review = findById(id);
		
		return new ReviewDTO(review);
	}
		
	public Review findById(Long id) {
		Optional<Review> reviewOptional = reviewRepository.findById(id);
		Review review = reviewOptional.orElseThrow(() -> new ObjectNotFoundException("Review not found! Id: " + id));
	
		return review;
	}
	
	public ReviewDTO update(Long id, ReviewUpdateDTO reviewDto) {
		Review review = findById(id);
		
		UserSS userAuthenticated = UserService.authenticated();	
		
		if(!review.getUser().getId().equals(userAuthenticated.getId())) {
			throw new AuthorizationException("Access denied");
		}
		
		review.setTitle(reviewDto.getTitle());
		review.setContent(reviewDto.getContent());
		review.setDate(getDateTime());
		
		review = reviewRepository.save(review);
		
		return new ReviewDTO(review);		
	}
	
	public Page<ReviewDTO> findReviewsByComic(Long comicId, Integer page, Integer linesPerPage, String orderBy, String direction) {
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		
		return reviewRepository.findReviewsByComic(comicId, pageRequest).map(obj -> new ReviewDTO(obj));
	}
	
	public Page<ReviewDTO> findReviewsByUser(Long userId, Integer page, Integer linesPerPage, String orderBy, String direction) {
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		
		return reviewRepository.findReviewsByUser(userId, pageRequest).map(obj -> new ReviewDTO(obj));
	}
	
	public Page<ReviewDTO> findByTitle(String title, Integer page, Integer linesPerPage, String orderBy, String direction) {
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		
		return reviewRepository.findByTitle(title, pageRequest).map(obj -> new ReviewDTO(obj));
	}
	
	public void delete(Long id) {
		Review foundReview = findById(id);
		
		UserSS userAuthenticated = UserService.authenticated();	
		
		if(!userAuthenticated.hasRole(Profile.ADMIN) && !foundReview.getUser().getId().equals(userAuthenticated.getId())) {
			throw new AuthorizationException("Access denied");
		}
		
		reviewRepository.delete(foundReview);
	}
	
	public LocalDateTime getDateTime() {
		return LocalDateTime.now();
	}

}
