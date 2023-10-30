package com.gustavo.comicreviewapi.entities;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name="TB_COMIC")
public class Comic implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	private Long id;
	private String title;
	private Float price;
	private String isbn;
	
	@Column(length=3000)
	private String description;
	
	@ManyToMany(cascade = CascadeType.PERSIST)
	@JoinTable(name = "TB_COMIC_AUTHOR",
			joinColumns = @JoinColumn(name = "comic_id",  referencedColumnName="id"),
			inverseJoinColumns = @JoinColumn(name = "author_id", referencedColumnName="id"))
	private Set<Author> authors = new HashSet<>();
	
	@ManyToMany(cascade = CascadeType.PERSIST)
	@JoinTable(name = "TB_COMIC_CHARACTER",
			joinColumns = @JoinColumn(name = "comic_id", referencedColumnName="id"),
			inverseJoinColumns = @JoinColumn(name = "character_id", referencedColumnName="id"))
	private Set<Character> characters = new HashSet<>();
	
	@OneToMany(mappedBy = "comic")
	private Set<Review> reviews = new HashSet<>();
	
	@OneToMany(mappedBy = "id.comic")
	private Set<Rate> ratings = new HashSet<>();
	
	@OneToMany(mappedBy = "id.comic")
	private Set<Reading> readings = new HashSet<>();
	
	public Comic() {
		
	}

	public Comic(Long id, String title, Float price, String isbn, String description) {
		super();
		this.id = id;
		this.title = title;
		this.price = price;
		this.isbn = isbn;
		this.description = description;
	}

	public Long getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public Float getPrice() {
		return price;
	}

	public String getIsbn() {
		return isbn;
	}

	public String getDescription() {
		return description;
	}

	public Set<Author> getAuthors() {
		return authors;
	}

	public Set<Character> getCharacters() {
		return characters;
	}
	
	public Set<Review> getReviews() {
		return reviews;
	}

	public Set<Rate> getRatings() {
		return ratings;
	}

	public Set<Reading> getReadings() {
		return readings;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setPrice(Float price) {
		this.price = price;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setAuthors(Set<Author> authors) {
		this.authors = authors;
	}

	public void setCharacters(Set<Character> characters) {
		this.characters = characters;
	}
	
	public void setReviews(Set<Review> reviews) {
		this.reviews = reviews;
	}

	public void setRatings(Set<Rate> ratings) {
		this.ratings = ratings;
	}

	public void setReadings(Set<Reading> readings) {
		this.readings = readings;
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
		Comic other = (Comic) obj;
		return Objects.equals(id, other.id);
	}
	
}
