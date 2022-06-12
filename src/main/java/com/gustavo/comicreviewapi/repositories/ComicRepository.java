package com.gustavo.comicreviewapi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gustavo.comicreviewapi.entities.Comic;

@Repository
public interface ComicRepository extends JpaRepository<Comic, Long> {

}
