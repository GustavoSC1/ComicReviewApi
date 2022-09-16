package com.gustavo.comicreviewapi.services;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.gustavo.comicreviewapi.dtos.CommentDTO;
import com.gustavo.comicreviewapi.dtos.CommentNewDTO;
import com.gustavo.comicreviewapi.dtos.CommentUpdateDTO;
import com.gustavo.comicreviewapi.entities.Comment;
import com.gustavo.comicreviewapi.entities.Review;
import com.gustavo.comicreviewapi.entities.User;
import com.gustavo.comicreviewapi.entities.enums.Profile;
import com.gustavo.comicreviewapi.repositories.CommentRepository;
import com.gustavo.comicreviewapi.security.UserSS;
import com.gustavo.comicreviewapi.services.exceptions.AuthorizationException;
import com.gustavo.comicreviewapi.services.exceptions.ObjectNotFoundException;

@Service
public class CommentService {
	
	private CommentRepository commentRepository;
		
	private ReviewService reviewService;

	public CommentService(CommentRepository commentRepository, ReviewService reviewService) {
		this.commentRepository = commentRepository;
		this.reviewService = reviewService;
	}
	
	public CommentDTO save(CommentNewDTO commentDto) {
		
		UserSS userAuthenticated = UserService.authenticated();		
		if(userAuthenticated==null) {
			throw new AuthorizationException("Access denied");
		}
		
		User user = new User();
		user.setId(userAuthenticated.getId());
		Review review = reviewService.findById(commentDto.getReviewId());
	
		Comment comment = new Comment(null, commentDto.getTitle(), getDateTime(), commentDto.getContent(), review, user);
		comment = commentRepository.save(comment);
		
		return new CommentDTO(comment);	
	}
	
	public CommentDTO find(Long id) {
		Comment coment = findById(id);
		
		return new CommentDTO(coment);
	}
	
	public Comment findById(Long id) {
		Optional<Comment> commentOptional = commentRepository.findById(id);
		Comment comment = commentOptional.orElseThrow(() -> new ObjectNotFoundException("Object not found! Id: " + id + ", Type: " + Comment.class.getName()));
		
		return comment;
	}
	
	public CommentDTO update(Long id, CommentUpdateDTO commentDto) {
		Comment comment = findById(id);
		
		UserSS userAuthenticated = UserService.authenticated();		
		if(userAuthenticated==null || !comment.getUser().getId().equals(userAuthenticated.getId())) {
			throw new AuthorizationException("Access denied");
		}
		
		comment.setTitle(commentDto.getTitle());
		comment.setContent(commentDto.getContent());
		comment.setDate(getDateTime());
		
		comment = commentRepository.save(comment);
		
		return new CommentDTO(comment);
	}
	
	public Page<CommentDTO> findCommentsByReview(Long reviewId,Integer page, Integer linesPerPage, String orderBy, String direction) {
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		
		return commentRepository.findCommentsByReview(reviewId, pageRequest).map(obj -> new CommentDTO(obj));		
	}
	
	public void delete(Long id) {
		Comment foundComment = findById(id);
		
		UserSS userAuthenticated = UserService.authenticated();		
		if(userAuthenticated==null || !userAuthenticated.hasRole(Profile.ADMIN) && !foundComment.getUser().getId().equals(userAuthenticated.getId())) {
			throw new AuthorizationException("Access denied");
		}
		
		commentRepository.delete(foundComment);
	}
	
	public LocalDateTime getDateTime() {
		return LocalDateTime.now();
	}
	
}
