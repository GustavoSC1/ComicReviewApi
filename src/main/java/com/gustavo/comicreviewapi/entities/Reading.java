package com.gustavo.comicreviewapi.entities;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Entity
public class Reading implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@EmbeddedId
	private ReadingPK id = new ReadingPK();
	
	private Boolean reading;
	
	private Boolean favourit;

	public Reading() {
		
	}

	public Reading(User user, Comic comic, Boolean reading, Boolean favourit) {
		this.id.setUser(user);
		this.id.setComic(comic);
		this.reading = reading;
		this.favourit = favourit;
	}

	public ReadingPK getId() {
		return id;
	}

	public Boolean getReading() {
		return reading;
	}

	public Boolean getFavourit() {
		return favourit;
	}

	public void setId(ReadingPK id) {
		this.id = id;
	}

	public void setReading(Boolean reading) {
		this.reading = reading;
	}

	public void setFavourit(Boolean favourit) {
		this.favourit = favourit;
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
		Reading other = (Reading) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}	
	
}
