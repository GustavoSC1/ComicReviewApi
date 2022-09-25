package com.gustavo.comicreviewapi.entities;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

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
		final int prime = 31;
		int result = 1;
		result = prime * result + ((comic == null) ? 0 : comic.hashCode());
		result = prime * result + ((user == null) ? 0 : user.hashCode());
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
		ReadingPK other = (ReadingPK) obj;
		if (comic == null) {
			if (other.comic != null)
				return false;
		} else if (!comic.equals(other.comic))
			return false;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		return true;
	}
	
}
