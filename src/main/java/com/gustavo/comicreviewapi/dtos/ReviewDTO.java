package com.gustavo.comicreviewapi.dtos;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.gustavo.comicreviewapi.entities.Review;

public class ReviewDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String title;
	private LocalDateTime date;
	private String content;
	private Long likes;
	
	public ReviewDTO() {
		
	}
	
	public ReviewDTO(Long id, String title, LocalDateTime date, String content, Long likes) {
		this.id = id;
		this.title = title;
		this.date = date;
		this.content = content;
		this.likes = likes;
	}
	
	public ReviewDTO(Review review) {
		this.id = review.getId();
		this.title = review.getTitle();
		this.date = review.getDate();
		this.content = review.getContent();	
		this.likes = review.getLikes().stream().filter(like -> like.getLiked().equals(true)).count();		
	}

	public Long getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public String getContent() {
		return content;
	}

	public Long getLikes() {
		return likes;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setLikes(Long likes) {
		this.likes = likes;
	}
	
}
