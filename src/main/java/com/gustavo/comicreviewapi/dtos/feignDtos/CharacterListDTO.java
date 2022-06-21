package com.gustavo.comicreviewapi.dtos.feignDtos;

import java.util.ArrayList;
import java.util.List;

public class CharacterListDTO {
		
	private List<CharacterSummaryDTO> items = new ArrayList<>();

	public List<CharacterSummaryDTO> getItems() {
		return items;
	}

	public void setItems(List<CharacterSummaryDTO> items) {
		this.items = items;
	}

}
