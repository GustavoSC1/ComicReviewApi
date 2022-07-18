package com.gustavo.comicreviewapi.dtos;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.gustavo.comicreviewapi.entities.Comment;

public class CommentDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String title;
	private LocalDateTime date;
	private String content;
	
	public CommentDTO() {
		
	}
	
	public CommentDTO(Long id, String title, LocalDateTime date, String content) {
		this.id = id;
		this.title = title;
		this.date = date;
		this.content = content;
	}
	
	public CommentDTO(Comment comment) {
		this.id = comment.getId();
		this.title = comment.getTitle();
		this.date = comment.getDate();
		this.content = comment.getContent();
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
	
}
