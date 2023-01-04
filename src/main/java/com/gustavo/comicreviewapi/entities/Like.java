package com.gustavo.comicreviewapi.entities;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="TB_LIKE") 
public class Like implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@EmbeddedId
	private LikePK id = new LikePK();
	
	private Boolean liked;
	
	public Like() {
		
	}

	public Like(User user, Review review, Boolean liked) {
		super();
		this.id.setUser(user);
		this.id.setReview(review);;
		this.liked = liked;
	}

	public LikePK getId() {
		return id;
	}

	public Boolean getLiked() {
		return liked;
	}

	public void setId(LikePK id) {
		this.id = id;
	}

	public void setLiked(Boolean liked) {
		this.liked = liked;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Like other = (Like) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
}
