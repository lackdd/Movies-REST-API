package com.movieapi.moviedb.controller;

import com.movieapi.moviedb.entities.Movie;
import com.movieapi.moviedb.services.MovieService;
import com.movieapi.moviedb.dto.MovieDTO;
import com.movieapi.moviedb.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movies")
public class MovieController {

    @Autowired
    private MovieService movieService;

    @PostMapping
    public ResponseEntity<MovieDTO> createMovie(@RequestBody MovieDTO movieDTO) {
        MovieDTO createdMovie = movieService.createMovie(movieDTO);
        return new ResponseEntity<>(createdMovie, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<MovieDTO>> getAllMovies() {
        List<MovieDTO> movies = movieService.getAllMovies();
        return new ResponseEntity<>(movies, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovieDTO> getMovieById(@PathVariable Integer id) {
        MovieDTO movieDTO = movieService.getMovieById(id);
        return new ResponseEntity<>(movieDTO, HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<MovieDTO> updateMovie(@PathVariable Integer id, @RequestBody MovieDTO updatedMovieDTO) {
        MovieDTO updatedMovie = movieService.updateMovie(id, updatedMovieDTO);
        return new ResponseEntity<>(updatedMovie, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMovie(@PathVariable Integer id) {
        movieService.deleteMovie(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/{movieId}/add-actor/{actorId}")
    public ResponseEntity<MovieDTO> addActorToMovie(@PathVariable Integer movieId, @PathVariable Integer actorId) {
        MovieDTO updatedMovie = movieService.addActorToMovie(movieId, actorId);
        return new ResponseEntity<>(updatedMovie, HttpStatus.OK);
    }
}