package com.gustavo.comicreviewapi.dtos;

import java.io.Serializable;
import java.time.LocalDate;

public class UserNewDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String name;
	private LocalDate birthDate;
	private String phone;
	private String email;
		
	public UserNewDTO() {
		super();
	}

	public UserNewDTO(String name, LocalDate birthDate, String phone, String email) {
		super();
		this.name = name;
		this.birthDate = birthDate;
		this.phone = phone;
		this.email = email;
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
	
}
