package com.gustavo.comicreviewapi.dtos.feignDtos;

import java.util.ArrayList;
import java.util.List;

public class ComicDTO {
	
	private Integer id;
	private String title;
	private String isbn;
	private String description;
	private List<ComicPriceDTO> prices = new ArrayList<>();
	private CreatorListDTO creators;
	private CharacterListDTO characters;
	
	public Integer getId() {
		return id;
	}
	
	public String getTitle() {
		return title;
	}
	
	public String getIsbn() {
		return isbn;
	}
	
	public String getDescription() {
		return description;
	}
	
	public List<ComicPriceDTO> getPrices() {
		return prices;
	}
	
	public CreatorListDTO getCreators() {
		return creators;
	}
	
	public CharacterListDTO getCharacters() {
		return characters;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public void setPrices(List<ComicPriceDTO> prices) {
		this.prices = prices;
	}
	
	public void setCreators(CreatorListDTO creators) {
		this.creators = creators;
	}

	public void setCharacters(CharacterListDTO characters) {
		this.characters = characters;
	}
	
}
