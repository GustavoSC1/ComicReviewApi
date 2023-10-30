package com.gustavo.comicreviewapi.entities;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name="TB_REVIEW")
public class Review implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String title;
	private LocalDateTime date;
	
	@Column(length=3000)
	private String content;
	
	@ManyToOne
	@JoinColumn(name = "user_id")	
	private User user;
	
	@ManyToOne
	@JoinColumn(name = "comic_id")
	private Comic comic;
	
	@OneToMany(mappedBy = "review")
	private Set<Comment> comments = new HashSet<>();
	
	@OneToMany(mappedBy = "id.review")
	private Set<Like> likes = new HashSet<>();
	
	public Review() {

	}

	public Review(Long id, String title, LocalDateTime date, String content, User user, Comic comic) {
		this.id = id;
		this.title = title;
		this.date = date;
		this.content = content;
		this.user = user;
		this.comic = comic;
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

	public User getUser() {
		return user;
	}

	public Comic getComic() {
		return comic;
	}
	
	public Set<Comment> getComments() {
		return comments;
	}
		
	public Set<Like> getLikes() {
		return likes;
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

	public void setUser(User user) {
		this.user = user;
	}

	public void setComic(Comic comic) {
		this.comic = comic;
	}

	public void setComments(Set<Comment> comments) {
		this.comments = comments;
	}

	public void setLikes(Set<Like> likes) {
		this.likes = likes;
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
		Review other = (Review) obj;
		return Objects.equals(id, other.id);
	}
	
}
