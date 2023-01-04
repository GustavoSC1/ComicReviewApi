package com.gustavo.comicreviewapi.entities;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="TB_COMMENT")
public class Comment implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String title;
	private LocalDateTime date;
	
	@Column(length=3000)
	private String content;
	
	@ManyToOne
	@JoinColumn(name = "review_id")
	private Review review;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	public Comment() {	
		
	}
	
	public Comment(Long id, String title, LocalDateTime date, String content, Review review, User user) {		
		this.id = id;
		this.title = title;
		this.date = date;
		this.content = content;
		this.review = review;
		this.user = user;
	}

	public Long getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public String getContent() {
		return content;
	}

	public Review getReview() {
		return review;
	}

	public User getUser() {
		return user;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setReview(Review review) {
		this.review = review;
	}

	public void setUser(User user) {
		this.user = user;
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
		Comment other = (Comment) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
}
