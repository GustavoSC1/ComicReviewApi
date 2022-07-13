package com.gustavo.comicreviewapi.dtos;

import java.io.Serializable;
import java.time.LocalDateTime;

public class ReviewNewDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String title;
	private LocalDateTime date;
	private String content;
	
	private Long userId;
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
