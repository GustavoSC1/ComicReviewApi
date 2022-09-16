package com.gustavo.comicreviewapi.dtos;

import java.io.Serializable;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

public class CommentNewDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@NotEmpty(message="The title field is required")
	@Length(min=8, max=120, message="The length must be between 8 and 120 characters")
	private String title;
	
	@NotEmpty(message="The content field is required")
	@Length(min=40, message="Content field cannot be less than 40 characters")
	private String content;
	
	@NotNull(message="The review id field is required")
	@Min(value=1, message="Review id cannot be less than 1")
	private Long reviewId;
	
	public CommentNewDTO() {
		
	}
	
	public CommentNewDTO(String title, String content, Long reviewId) {
		this.title = title;
		this.content = content;
		this.reviewId = reviewId;
	}

	public String getTitle() {
		return title;
	}

	public String getContent() {
		return content;
	}

	public Long getReviewId() {
		return reviewId;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setReviewId(Long reviewId) {
		this.reviewId = reviewId;
	}
	
}
