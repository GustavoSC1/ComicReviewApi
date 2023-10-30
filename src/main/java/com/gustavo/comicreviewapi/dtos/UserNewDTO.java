package com.gustavo.comicreviewapi.dtos;

import java.io.Serializable;
import java.time.LocalDate;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

public class UserNewDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@NotEmpty(message="The name field is required")
	@Length(min=8, max=120, message="The length must be between 8 and 120 characters")
	private String name;
	
	@NotNull(message="The birth date field is required")
	@Past(message="Invalid date")
	private LocalDate birthDate;
	
	@NotEmpty(message="The phone field is required")
	private String phone;
	
	@NotEmpty(message="The email field is required")
	@Email(message="Invalid email")
	private String email;
	
	@NotEmpty(message="The password field is required")
	@Length(min=8, max=18, message="The length must be between 8 and 18 characters")
	private String password;
		
	public UserNewDTO() {
		super();
	}

	public UserNewDTO(String name, LocalDate birthDate, String phone, String email, String password) {
		super();
		this.name = name;
		this.birthDate = birthDate;
		this.phone = phone;
		this.email = email;
		this.password = password;
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
	
}
