package com.gustavo.comicreviewapi.dtos;

import java.io.Serializable;
import java.time.LocalDate;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

public class UserNewDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@NotEmpty(message="Mandatory completion")
	@Length(min=8, max=120, message="The length must be between 8 and 120 characters")
	private String name;
	
	@NotNull(message="Mandatory completion")
	@Past(message="Invalid date")
	private LocalDate birthDate;
	
	@NotEmpty(message="Mandatory completion")
	private String phone;
	
	@NotEmpty(message="Mandatory completion")
	@Email(message="Invalid email")
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
