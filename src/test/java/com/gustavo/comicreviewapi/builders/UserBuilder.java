package com.gustavo.comicreviewapi.builders;

import java.time.LocalDate;

import com.gustavo.comicreviewapi.entities.User;
import com.gustavo.comicreviewapi.entities.enums.Profile;

public class UserBuilder {
	
	private User user;
	
	//Está privado para que ninguem possa criar instâncias do builder externamente ao próprio builder
	private UserBuilder() {}
	
	//Ficou public e static para que ele possa ser acessado externamente sem a necessidade de uma instância
	public static UserBuilder aUser() {
		UserBuilder builder = new UserBuilder();
		builder.user = new User();
		builder.user.setName("Gustavo Silva Cruz");
		builder.user.setBirthDate(LocalDate.of(1996, 10, 17));
		builder.user.setPhone("998123899");
		builder.user.setEmail("gu.cruz17@hotmail.com");
		builder.user.setPassword("Password1.");
		builder.user.addProfile(Profile.USER);
		
		return builder;
	}
	
	public UserBuilder withId(Long id) {
		user.setId(id);
		return this;
	}
	
	public User now() {
		return user;
	}

}
