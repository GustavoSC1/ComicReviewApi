package com.gustavo.comicreviewapi.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.gustavo.comicreviewapi.entities.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
	
	@Query("SELECT obj FROM Comment obj WHERE obj.review.id = :reviewId")
	Page<Comment> findByReview(@Param("reviewId") Long reviewId, Pageable pageable);
	
}
