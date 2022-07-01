package com.gustavo.comicreviewapi.dtos.feignDtos;

public class ComicPriceDTO {
	
	private Float price;
	
	public ComicPriceDTO() {
	
	}

	public ComicPriceDTO(Float price) {
		super();
		this.price = price;
	}

	public Float getPrice() {
		return price;
	}

	public void setPrice(Float price) {
		this.price = price;
	}
	
}
