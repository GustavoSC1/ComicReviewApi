package com.gustavo.comicreviewapi.dtos.feignDtos;

import java.util.ArrayList;
import java.util.List;

public class CreatorListDTO {
	
	private List<CreatorSummaryDTO> items = new ArrayList<>();

	public List<CreatorSummaryDTO> getItems() {
		return items;
	}

	public void setItems(List<CreatorSummaryDTO> items) {
		this.items = items;
	}

}
