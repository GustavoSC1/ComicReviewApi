package com.gustavo.comicreviewapi.builders;

import java.time.LocalDateTime;

import com.gustavo.comicreviewapi.entities.Comment;
import com.gustavo.comicreviewapi.entities.Review;
import com.gustavo.comicreviewapi.entities.User;

public class CommentBuilder {
	
	private Comment comment;
	
	private CommentBuilder() {}
	
	public static CommentBuilder aComment() {
		CommentBuilder builder = new CommentBuilder();
		builder.comment = new Comment();
		builder.comment.setTitle("Ótimo review");
		builder.comment.setDate(LocalDateTime.of(2022, 11, 20, 22, 10));
		builder.comment.setContent("Parabéns pelo review, com certeza irei adquirir essa HQ!");
		
		return builder;
	}
	
	public CommentBuilder withUser(User user) {
		comment.setUser(user);
		return this;
	}
	
	public CommentBuilder withReview(Review review) {
		comment.setReview(review);
		return this;
	}
	
	public CommentBuilder withId(Long id) {
		comment.setId(id);
		return this;
	}
	
	public Comment now() {
		return comment;
	}
	
}
