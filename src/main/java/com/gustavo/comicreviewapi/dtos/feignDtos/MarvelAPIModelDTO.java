package com.gustavo.comicreviewapi.dtos.feignDtos;

public class MarvelAPIModelDTO {
	
	private ComicDataContainerDTO data;

	public ComicDataContainerDTO getData() {
		return data;
	}

	public void setData(ComicDataContainerDTO data) {
		this.data = data;
	}
	
}