package com.gustavo.comicreviewapi.dtos.feignDtos;

public class CreatorSummaryDTO {
	
	private String name;
	
	public CreatorSummaryDTO() {
		
	}

	public CreatorSummaryDTO(String name) {
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
