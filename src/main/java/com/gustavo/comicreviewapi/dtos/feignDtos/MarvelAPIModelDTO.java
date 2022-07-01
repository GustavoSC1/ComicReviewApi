package com.gustavo.comicreviewapi.dtos.feignDtos;

public class MarvelAPIModelDTO {
	
	private ComicDataContainerDTO data;

	public MarvelAPIModelDTO() {
	
	}

	public MarvelAPIModelDTO(ComicDataContainerDTO data) {
		super();
		this.data = data;
	}

	public ComicDataContainerDTO getData() {
		return data;
	}

	public void setData(ComicDataContainerDTO data) {
		this.data = data;
	}
	
}