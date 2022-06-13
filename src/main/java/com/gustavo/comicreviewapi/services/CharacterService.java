package com.gustavo.comicreviewapi.services;

import org.springframework.stereotype.Service;

import com.gustavo.comicreviewapi.entities.Character;
import com.gustavo.comicreviewapi.repositories.CharacterRepository;

@Service
public class CharacterService {
	
	private CharacterRepository characterRepository;
	
	public CharacterService(CharacterRepository characterRepository) {
		this.characterRepository = characterRepository;
	}
	
	public Character findByName(String name) {
		return characterRepository.findByName(name);
	}

}
