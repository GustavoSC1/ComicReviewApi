package com.gustavo.comicreviewapi.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.gustavo.comicreviewapi.entities.Comic;

@Repository
public interface ComicRepository extends JpaRepository<Comic, Long> {
	
	@Query("select obj from Comic obj where upper(obj.title) LIKE '%' || upper(:title) || '%'")
	Page<Comic>	findByTitle(@Param("title") String title, Pageable pageable);

}
