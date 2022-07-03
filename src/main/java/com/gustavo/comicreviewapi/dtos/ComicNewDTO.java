package com.gustavo.comicreviewapi.dtos;

import java.io.Serializable;

public class ComicNewDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Integer idComicMarvel;
	
	public ComicNewDTO() {
		
	}

	public Integer getIdComicMarvel() {
		return idComicMarvel;
	}

	public void setIdComicMarvel(Integer idComicMarvel) {
		this.idComicMarvel = idComicMarvel;
	}
	
}
