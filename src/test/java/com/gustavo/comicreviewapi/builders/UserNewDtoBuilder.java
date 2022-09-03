package com.gustavo.comicreviewapi.builders;

import java.time.LocalDate;

import com.gustavo.comicreviewapi.dtos.UserNewDTO;

public class UserNewDtoBuilder {
	
	private UserNewDTO userNewDto;
	
	private UserNewDtoBuilder() {}
	
	public static UserNewDtoBuilder aUserNewDTO() {
		UserNewDtoBuilder builder = new UserNewDtoBuilder();
		builder.userNewDto = new UserNewDTO();
		builder.userNewDto.setName("Gustavo Silva Cruz");
		builder.userNewDto.setEmail("gu.cruz17@hotmail.com");
		builder.userNewDto.setBirthDate(LocalDate.of(1996, 10, 17));
		builder.userNewDto.setPhone("998123456");
		builder.userNewDto.setPassword("Password1.");

		return builder;
	}
	
	public UserNewDTO now() {
		return userNewDto;
	}	

}
