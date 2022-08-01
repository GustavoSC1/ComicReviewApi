package com.gustavo.comicreviewapi.builders;

import java.time.LocalDate;

import com.gustavo.comicreviewapi.dtos.UserDTO;

public class UserDtoBuilder {
	
	private UserDTO userDto;
	
	private UserDtoBuilder() {}
	
	public static UserDtoBuilder aUserDTO() {
		UserDtoBuilder builder = new UserDtoBuilder();
		builder.userDto = new UserDTO();
		builder.userDto.setName("Gustavo Silva Cruz");
		builder.userDto.setBirthDate(LocalDate.of(1996, 10, 17));
		builder.userDto.setPhone("998123899");
		builder.userDto.setEmail("gu.cruz17@hotmail.com");
		
		return builder;
	}
	
	public UserDtoBuilder withId(Long id) {
		userDto.setId(id);
		return this;
	}
	
	public UserDtoBuilder withBirthDate(LocalDate birthDate) {
		userDto.setBirthDate(birthDate);
		return this;
	}
	
	public UserDTO now() {
		return userDto;
	}

}
