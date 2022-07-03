package com.gustavo.comicreviewapi.dtos;

import java.io.Serializable;

import com.gustavo.comicreviewapi.entities.Author;

public class AuthorDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String name;
	
	public AuthorDTO() {
		
	}
	
	public AuthorDTO(Author author) {
		super();
		this.id = author.getId();
		this.name = author.getName();
	}
		
	public AuthorDTO(Long id, String name) {
		this.id = id;
		this.name = name;
	}

	public Long getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
}
