package com.gustavo.comicreviewapi.dtos;

import java.io.Serializable;

public class ReadingNewDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Boolean reading;
	
	private Boolean favourit;
	
	public ReadingNewDTO() {
		
	}

	public ReadingNewDTO(Boolean reading, Boolean favourit) {
		this.reading = reading;
		this.favourit = favourit;
	}

	public Boolean getReading() {
		return reading;
	}

	public Boolean getFavourit() {
		return favourit;
	}

	public void setReading(Boolean reading) {
		this.reading = reading;
	}

	public void setFavourit(Boolean favourit) {
		this.favourit = favourit;
	}

}
