package com.gustavo.comicreviewapi.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.gustavo.comicreviewapi.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	
	@Transactional(readOnly=true)
	boolean existsByEmail(String email);
	
	@Transactional(readOnly=true)
	User findByEmail(String email);
	
	@Query("select obj from User obj where day(obj.birthDate) = :day and month(obj.birthDate) = :month")
	List<User> findByDayAndMonthOfBirth(@Param("day") int day,@Param("month") int month);
	
}
