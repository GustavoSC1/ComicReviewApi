package com.gustavo.comicreviewapi.dtos;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import com.gustavo.comicreviewapi.entities.User;
import com.gustavo.comicreviewapi.entities.enums.Profile;

public class UserDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long id;	
	private String name;
	private LocalDate birthDate;
	private String phone;	
	private String email;	
	private Set<Profile> profiles = new HashSet<>();
		
	public UserDTO() {
		
	}

	public UserDTO(Long id, String name, LocalDate birthDate, String phone, String email) {
		this.id = id;
		this.name = name;
		this.birthDate = birthDate;
		this.phone = phone;
		this.email = email;
	}

	public UserDTO(User user) {
		this.id = user.getId();
		this.name = user.getName();
		this.birthDate = user.getBirthDate();
		this.phone = user.getPhone();
		this.email = user.getEmail();
		this.profiles = user.getProfiles();
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

	public Set<Profile> getProfiles() {
		return profiles;
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

	public void addProfile(Profile profile) {
		profiles.add(profile);
	}
	
}
