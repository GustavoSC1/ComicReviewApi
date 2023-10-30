package com.gustavo.comicreviewapi.entities;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

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
		return Objects.hash(id);
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
		return Objects.equals(id, other.id);
	}
	
}
