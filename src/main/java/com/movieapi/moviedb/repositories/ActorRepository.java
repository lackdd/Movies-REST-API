package com.movieapi.moviedb.repositories;

import com.movieapi.moviedb.entities.Actor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

@Repository
public interface ActorRepository extends JpaRepository<Actor, Integer> {
    Page<Actor> findByNameContainingIgnoreCase(String name, Pageable pageable);
}