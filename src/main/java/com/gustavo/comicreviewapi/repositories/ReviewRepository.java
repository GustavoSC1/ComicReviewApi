package com.gustavo.comicreviewapi.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.gustavo.comicreviewapi.entities.Review;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
	
	@Query("SELECT obj FROM Review obj WHERE obj.comic.id = :comicId")
	Page<Review> findReviewsByComic(@Param("comicId") Long comicId, Pageable pageable);
	
	@Query("SELECT obj FROM Review obj WHERE upper(obj.title) LIKE '%' || upper(:title) || '%'")
	Page<Review> findByTitle(@Param("title") String title, Pageable pageable);

}
