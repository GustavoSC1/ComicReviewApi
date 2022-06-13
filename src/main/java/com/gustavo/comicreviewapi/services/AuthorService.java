package com.gustavo.comicreviewapi.services;

import org.springframework.stereotype.Service;

import com.gustavo.comicreviewapi.entities.Author;
import com.gustavo.comicreviewapi.repositories.AuthorRepository;

@Service
public class AuthorService {
	
	private AuthorRepository authorRepository;
	
	public AuthorService(AuthorRepository authorRepository) {
		this.authorRepository = authorRepository;
	}
	
	public Author findByName(String name) {
		return authorRepository.findByName(name);
	}

}
