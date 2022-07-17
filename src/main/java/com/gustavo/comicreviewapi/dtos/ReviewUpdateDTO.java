package com.gustavo.comicreviewapi.dtos;

import java.io.Serializable;

public class ReviewUpdateDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String title;
	private String content;
	
	public ReviewUpdateDTO() {
	}
	
	public ReviewUpdateDTO(String title, String content) {
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
