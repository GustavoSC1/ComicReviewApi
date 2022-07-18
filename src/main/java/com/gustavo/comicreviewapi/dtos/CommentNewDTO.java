package com.gustavo.comicreviewapi.dtos;

import java.io.Serializable;

public class CommentNewDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String title;
	private String content;
	
	private Long userId;
	private Long reviewId;
	
	public CommentNewDTO() {
		
	}
	
	public CommentNewDTO(String title, String content, Long userId, Long reviewId) {
		this.title = title;
		this.content = content;
		this.userId = userId;
		this.reviewId = reviewId;
	}

	public String getTitle() {
		return title;
	}

	public String getContent() {
		return content;
	}

	public Long getUserId() {
		return userId;
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

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public void setReviewId(Long reviewId) {
		this.reviewId = reviewId;
	}
	
}
