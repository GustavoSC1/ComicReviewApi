package com.gustavo.comicreviewapi.dtos;

import java.io.Serializable;

import com.gustavo.comicreviewapi.entities.Character;

public class CharacterDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String name;
	
	public CharacterDTO() {
		
	}
	
	public CharacterDTO(Character character) {
		this.id = character.getId();
		this.name = character.getName();
	}
		
	public CharacterDTO(Long id, String name) {
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
