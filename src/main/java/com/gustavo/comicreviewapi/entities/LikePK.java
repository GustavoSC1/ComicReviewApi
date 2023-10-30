package com.gustavo.comicreviewapi.entities;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Embeddable
public class LikePK implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@ManyToOne
	@JoinColumn(name="user_id")
	private User user;
	
	@ManyToOne
	@JoinColumn(name="review_id")
	private Review review;
	
	public LikePK() {
		
	}

	public LikePK(User user, Review review) {
		this.user = user;
		this.review = review;
	}

	public User getUser() {
		return user;
	}

	public Review getReview() {
		return review;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public void setReview(Review review) {
		this.review = review;
	}

	@Override
	public int hashCode() {
		return Objects.hash(review, user);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LikePK other = (LikePK) obj;
		return Objects.equals(review, other.review) && Objects.equals(user, other.user);
	}
	
}
