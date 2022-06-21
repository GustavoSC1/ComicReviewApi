package com.gustavo.comicreviewapi.dtos.feignDtos;

import java.util.ArrayList;
import java.util.List;

public class ComicDataContainerDTO {
	
	private List<ComicDTO> results = new ArrayList<>();

	public List<ComicDTO> getResults() {
		return results;
	}

	public void setResults(List<ComicDTO> results) {
		this.results = results;
	}
	
}