package com.movieapi.moviedb.repositories;

import com.movieapi.moviedb.entities.Actor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ActorRepository extends JpaRepository<Actor, Integer> {
    List<Actor> findByNameContainingIgnoreCase(String name);
}