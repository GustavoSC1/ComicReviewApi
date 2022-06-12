package com.gustavo.comicreviewapi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.gustavo.comicreviewapi.entities.Character;

@Repository
public interface CharacterRepository extends JpaRepository<Character, Long> {
	
	@Transactional(readOnly=true)
	Character findByName(String name);
	
}
