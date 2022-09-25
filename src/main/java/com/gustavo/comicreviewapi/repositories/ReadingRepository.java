package com.gustavo.comicreviewapi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gustavo.comicreviewapi.entities.Reading;
import com.gustavo.comicreviewapi.entities.ReadingPK;

@Repository
public interface ReadingRepository extends JpaRepository<Reading, ReadingPK> {

}
