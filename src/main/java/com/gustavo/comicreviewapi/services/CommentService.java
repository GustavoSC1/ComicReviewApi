package com.gustavo.comicreviewapi.services;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.gustavo.comicreviewapi.dtos.CommentDTO;
import com.gustavo.comicreviewapi.dtos.CommentNewDTO;
import com.gustavo.comicreviewapi.entities.Comment;
import com.gustavo.comicreviewapi.entities.Review;
import com.gustavo.comicreviewapi.entities.User;
import com.gustavo.comicreviewapi.repositories.CommentRepository;
import com.gustavo.comicreviewapi.services.exceptions.ObjectNotFoundException;

@Service
public class CommentService {
	
	private CommentRepository commentRepository;
	
	private UserService userService;
	
	private ReviewService reviewService;

	public CommentService(CommentRepository commentRepository, UserService userService, ReviewService reviewService) {
		this.commentRepository = commentRepository;
		this.userService = userService;
		this.reviewService = reviewService;
	}
	
	public CommentDTO save(CommentNewDTO commentDto) {
		User user = userService.findById(commentDto.getUserId());
		Review review = reviewService.findById(commentDto.getReviewId());
	
		Comment comment = new Comment(null, commentDto.getTitle(), getDateTime(), commentDto.getContent(), review, user);
		comment = commentRepository.save(comment);
		
		return new CommentDTO(comment);	
	}
	
	public Comment findById(Long id) {
		Optional<Comment> commentOptional = commentRepository.findById(id);
		Comment comment = commentOptional.orElseThrow(() -> new ObjectNotFoundException("Object not found! Id: " + id + ", Type: " + Comment.class.getName()));
		
		return comment;
	}
	
	public LocalDateTime getDateTime() {
		return LocalDateTime.now();
	}
	
}
