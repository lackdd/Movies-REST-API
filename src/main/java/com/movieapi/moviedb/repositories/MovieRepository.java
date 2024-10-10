package com.movieapi.moviedb.repositories;

import com.movieapi.moviedb.entities.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Integer> {

    @Query("SELECT m FROM Movie m JOIN m.genres g WHERE g.id = :genreId")
    Page<Movie> findByGenreId(@Param("genreId") Integer genreId, Pageable pageable);

    @Query("SELECT m FROM Movie m JOIN m.actors a WHERE a.id = :actorId")
    Page<Movie> findByActorId(@Param("actorId") Integer actorId, Pageable pageable);

    @Query("SELECT m FROM Movie m WHERE m.releaseYear = :year")
    Page<Movie> findByReleaseYear(@Param("year") String year, Pageable pageable);

    Page<Movie> findAll(Pageable pageable);

    Page<Movie> findByTitleContainingIgnoreCase(String title, Pageable pageable);
}