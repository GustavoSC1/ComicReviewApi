package com.gustavo.comicreviewapi.entities;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Embeddable
public class ReadingPK implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@ManyToOne
	@JoinColumn(name="user_id")
	private User user;
	
	@ManyToOne
	@JoinColumn(name="comic_id")
	private Comic comic;

	public ReadingPK() {
		
	}

	public ReadingPK(User user, Comic comic) {
		this.user = user;
		this.comic = comic;
	}

	public User getUser() {
		return user;
	}

	public Comic getComic() {
		return comic;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public void setComic(Comic comic) {
		this.comic = comic;
	}

	@Override
	public int hashCode() {
		return Objects.hash(comic, user);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ReadingPK other = (ReadingPK) obj;
		return Objects.equals(comic, other.comic) && Objects.equals(user, other.user);
	}
	
}
