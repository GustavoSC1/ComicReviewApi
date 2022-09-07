package com.gustavo.comicreviewapi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.gustavo.comicreviewapi.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	
	@Transactional(readOnly=true)
	boolean existsByEmail(String email);
	
	@Transactional(readOnly=true)
	User findByEmail(String email);
	
}
