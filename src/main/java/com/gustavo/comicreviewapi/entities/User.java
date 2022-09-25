package com.gustavo.comicreviewapi.entities;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.gustavo.comicreviewapi.entities.enums.Profile;

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
	
	@ElementCollection(fetch=FetchType.EAGER)
	@CollectionTable(name="PROFILES")
	private Set<Integer> profiles = new HashSet<>();
	
	@OneToMany(mappedBy="user")
	private Set<Review> reviews = new HashSet<>();
	
	@OneToMany(mappedBy = "user")
	private Set<Comment> comments = new HashSet<>();
	
	@OneToMany(mappedBy = "id.user")
	private Set<Rate> ratings = new HashSet<>();
	
	@OneToMany(mappedBy = "id.user")
	private Set<Like> likes = new HashSet<>();
	
	@OneToMany(mappedBy = "id.user")
	private Set<Reading> readings = new HashSet<>();
		
	public User() {
		addProfile(Profile.USER);
	}
	
	public User(Long id, String name, LocalDate birthDate, String phone, String email, String password) {
		super();
		this.id = id;
		this.name = name;
		this.birthDate = birthDate;
		this.phone = phone;
		this.email = email;
		this.password = password;
		addProfile(Profile.USER);
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
	
	public Set<Profile> getProfiles() {
		return profiles.stream().map(x -> Profile.toEnum(x)).collect(Collectors.toSet());
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

	public Set<Like> getLikes() {
		return likes;
	}
	
	public Set<Reading> getReadings() {
		return readings;
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
	
	public void addProfile(Profile profile) {
		profiles.add(profile.getCod());
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

	public void setLikes(Set<Like> likes) {
		this.likes = likes;
	}

	public void setReadings(Set<Reading> readings) {
		this.readings = readings;
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
