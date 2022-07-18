package com.gustavo.comicreviewapi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gustavo.comicreviewapi.entities.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

}
