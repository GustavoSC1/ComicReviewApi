package com.gustavo.comicreviewapi.dtos;

import java.io.Serializable;

public class LikeNewDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Boolean liked;
	
	public LikeNewDTO() {
		
	}

	public LikeNewDTO(Boolean liked) {
		this.liked = liked;
	}

	public Boolean getLiked() {
		return liked;
	}

	public void setLiked(Boolean liked) {
		this.liked = liked;
	}

}
