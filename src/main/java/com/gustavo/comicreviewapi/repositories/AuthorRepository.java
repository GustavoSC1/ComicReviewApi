package com.gustavo.comicreviewapi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.gustavo.comicreviewapi.entities.Author;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
	
	@Transactional(readOnly=true)
	Author findByName(String name);

}
