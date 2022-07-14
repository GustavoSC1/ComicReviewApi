package com.gustavo.comicreviewapi.dtos;

import java.io.Serializable;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class ComicNewDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@NotNull(message="The comic id field is required")
	@Min(value=1, message="idComicMarvel cannot be less than 1")
	private Integer idComicMarvel;
	
	public ComicNewDTO() {
		
	}

	public ComicNewDTO(Integer idComicMarvel) {
		this.idComicMarvel = idComicMarvel;
	}

	public Integer getIdComicMarvel() {
		return idComicMarvel;
	}

	public void setIdComicMarvel(Integer idComicMarvel) {
		this.idComicMarvel = idComicMarvel;
	}
	
}
