package com.gustavo.comicreviewapi.entities;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	private LocalDate birthDate;
	private String phone;
	private String email;
	
	private String password;
	
	@OneToMany(mappedBy="user")
	private Set<Review> reviews = new HashSet<>();
	
	@OneToMany(mappedBy = "user")
	private Set<Comment> comments = new HashSet<>();
	
	@OneToMany(mappedBy = "id.user")
	private Set<Rate> ratings = new HashSet<>();
		
	public User() {
		
	}
	
	public User(Long id, String name, LocalDate birthDate, String phone, String email, String password) {
		super();
		this.id = id;
		this.name = name;
		this.birthDate = birthDate;
		this.phone = phone;
		this.email = email;
		this.password = password;
	}
	
	public Long getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public LocalDate getBirthDate() {
		return birthDate;
	}
	
	public String getPhone() {
		return phone;
	}
	
	public String getEmail() {
		return email;
	}
	
	public String getPassword() {
		return password;
	}

	public Set<Review> getReviews() {
		return reviews;
	}
	
	public Set<Comment> getComments() {
		return comments;
	}

	public Set<Rate> getRatings() {
		return ratings;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setBirthDate(LocalDate birthDate) {
		this.birthDate = birthDate;
	}
	
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}

	public void setReviews(Set<Review> reviews) {
		this.reviews = reviews;
	}

	public void setComments(Set<Comment> comments) {
		this.comments = comments;
	}	

	public void setRatings(Set<Rate> ratings) {
		this.ratings = ratings;
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
		User other = (User) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
}
