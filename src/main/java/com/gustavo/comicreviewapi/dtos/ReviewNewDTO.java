package com.gustavo.comicreviewapi.dtos;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

public class ReviewNewDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@NotEmpty(message="The title field is required")
	@Length(min=8, max=120, message="The length must be between 8 and 120 characters")
	private String title;
	
	@NotNull(message="The date field is required")
	private LocalDateTime date;
	
	@NotEmpty(message="The content field is required")
	@Length(min=40, message="Content field cannot be less than 40 characters")
	private String content;
	
	@NotNull(message="The user id field is required")
	@Min(value=1, message="User id cannot be less than 1")
	private Long userId;
	
	@NotNull(message="The comic id field is required")
	@Min(value=1, message="Comic id cannot be less than 1")
	private Long comicId;
		
	public ReviewNewDTO() {

	}	

	public ReviewNewDTO(String title, LocalDateTime date, String content, Long userId, Long comicId) {		
		this.title = title;
		this.date = date;
		this.content = content;
		this.userId = userId;
		this.comicId = comicId;
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
	
	public Long getUserId() {
		return userId;
	}
	
	public Long getComicId() {
		return comicId;
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
	
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	public void setComicId(Long comicId) {
		this.comicId = comicId;
	}

}
