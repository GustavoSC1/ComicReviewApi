package com.gustavo.comicreviewapi.dtos.feignDtos;

public class CharacterSummaryDTO {
	
	private String name;
	
	public CharacterSummaryDTO() {
		
	}

	public CharacterSummaryDTO(String name) {
		super();
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
