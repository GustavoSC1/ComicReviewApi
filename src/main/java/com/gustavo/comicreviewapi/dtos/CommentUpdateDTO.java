package com.gustavo.comicreviewapi.dtos;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;

public class CommentUpdateDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@NotEmpty(message="The title field is required")
	@Length(min=8, max=120, message="The length must be between 8 and 120 characters")
	private String title;
	
	@NotEmpty(message="The content field is required")
	@Length(min=40, message="Content field cannot be less than 40 characters")	
	private String content;
	
	public CommentUpdateDTO() {
	}
	
	public CommentUpdateDTO(String title, String content) {
		this.title = title;
		this.content = content;
	}

	public String getTitle() {
		return title;
	}

	public String getContent() {
		return content;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
