package com.movieapi.moviedb.repositories;

import com.movieapi.moviedb.entities.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Integer> {
    @Query("SELECT m FROM Movie m JOIN m.genres g WHERE g.id = :genreId")
    List<Movie> findByGenreId(@Param("genreId") Integer genreId);

    @Query("SELECT m FROM Movie m JOIN m.actors a WHERE a.id = :actorId")
    List<Movie> findByActorId(@Param("actorId") Integer actorId);

    @Query("SELECT m FROM Movie m WHERE m.releaseYear = :year")
    List<Movie> findByReleaseYear(@Param("year") String year);
}