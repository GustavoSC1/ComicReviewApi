package com.gustavo.comicreviewapi.entities;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name="TB_READING")
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
		Reading other = (Reading) obj;
		return Objects.equals(id, other.id);
	}
	
}
