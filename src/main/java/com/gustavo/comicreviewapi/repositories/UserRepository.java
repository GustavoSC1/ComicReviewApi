package com.gustavo.comicreviewapi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gustavo.comicreviewapi.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

}
