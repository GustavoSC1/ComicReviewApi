package com.gustavo.comicreviewapi.dtos;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import com.gustavo.comicreviewapi.entities.Comic;

public class ComicDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String title;
	private Float price;
	private String isbn;
	private String description;
	
	private Set<AuthorDTO> authors = new HashSet<>();
	private Set<CharacterDTO> characters = new HashSet<>();
	
	public ComicDTO() {

	}

	public ComicDTO(Long id, String title, Float price, String isbn, String description) {
		this.id = id;
		this.title = title;
		this.price = price;
		this.isbn = isbn;
		this.description = description;
	}

	public ComicDTO(Comic comic) {
		this.id = comic.getId();
		this.title = comic.getTitle();
		this.price = comic.getPrice();
		this.isbn = comic.getIsbn();
		this.description = comic.getDescription();
		this.authors = comic.getAuthors().stream().map(x -> new AuthorDTO(x)).collect(Collectors.toSet());
		this.characters = comic.getCharacters().stream().map(x -> new CharacterDTO(x)).collect(Collectors.toSet());
	}

	public Long getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public Float getPrice() {
		return price;
	}

	public String getIsbn() {
		return isbn;
	}

	public String getDescription() {
		return description;
	}

	public Set<AuthorDTO> getAuthors() {
		return authors;
	}

	public Set<CharacterDTO> getCharacters() {
		return characters;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setPrice(Float price) {
		this.price = price;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setAuthors(Set<AuthorDTO> authors) {
		this.authors = authors;
	}

	public void setCharacters(Set<CharacterDTO> characters) {
		this.characters = characters;
	}

}
