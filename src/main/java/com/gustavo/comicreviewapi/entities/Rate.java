package com.gustavo.comicreviewapi.entities;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name="TB_RATE")
public class Rate implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@EmbeddedId
	private RatePK id = new RatePK();
	
	private Integer rate;
	
	public Rate() {
		
	}

	public Rate(User user, Comic comic, Integer rate) {
		super();
		this.id.setUser(user);
		this.id.setComic(comic);
		this.rate = rate;
	}

	public RatePK getId() {
		return id;
	}

	public Integer getRate() {
		return rate;
	}

	public void setId(RatePK id) {
		this.id = id;
	}

	public void setRate(Integer rate) {
		this.rate = rate;
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
		Rate other = (Rate) obj;
		return Objects.equals(id, other.id);
	}
	
}
