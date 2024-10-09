package com.movieapi.moviedb.controller;

import com.movieapi.moviedb.entities.Movie;
import com.movieapi.moviedb.services.MovieService;
import com.movieapi.moviedb.dto.MovieDTO;
import com.movieapi.moviedb.dto.ActorDTO;
import jakarta.validation.Valid;
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
    public ResponseEntity<MovieDTO> createMovie(@Valid @RequestBody MovieDTO movieDTO) {
        MovieDTO createdMovie = movieService.createMovie(movieDTO);
        return new ResponseEntity<>(createdMovie, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<MovieDTO>> getMovies(
            @RequestParam(value = "actor", required = false) Integer actorId,
            @RequestParam(value = "year", required = false) String year) {
        // Check if year is provided
        if (year != null) {
            List<MovieDTO> movies = movieService.getMoviesByYear(year);
            return new ResponseEntity<>(movies, HttpStatus.OK);
        }
        // Check if actorId is provided
        if (actorId != null) {
            List<MovieDTO> movies = movieService.getMoviesByActorId(actorId);
            return new ResponseEntity<>(movies, HttpStatus.OK);
        }
        // If no filters are provided, return all movies
        List<MovieDTO> movies = movieService.getAllMovies();
        return new ResponseEntity<>(movies, HttpStatus.OK);
    }

    @GetMapping(params = "genre")
    public ResponseEntity<List<MovieDTO>> getMoviesByGenre(@RequestParam("genre") Integer genreId) {
        List<MovieDTO> movies = movieService.getMoviesByGenre(genreId);
        return new ResponseEntity<>(movies, HttpStatus.OK);
    }

    @GetMapping("/{movieId}/actors")
    public ResponseEntity<List<ActorDTO>> getActorsByMovieId(@PathVariable Integer movieId) {
        List<ActorDTO> actors = movieService.getActorsByMovieId(movieId);
        return new ResponseEntity<>(actors, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovieDTO> getMovieById(@PathVariable Integer id) {
        MovieDTO movieDTO = movieService.getMovieById(id);
        return new ResponseEntity<>(movieDTO, HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<MovieDTO> updateMovie(@PathVariable Integer id, @Valid @RequestBody MovieDTO updatedMovieDTO) {
        MovieDTO updatedMovie = movieService.updateMovie(id, updatedMovieDTO);
        return new ResponseEntity<>(updatedMovie, HttpStatus.OK);
    }

    // Updated method to handle force delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMovie(@PathVariable Integer id, @RequestParam(defaultValue = "false") boolean force) {
        movieService.deleteMovie(id, force);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/{movieId}/add-actor/{actorId}")
    public ResponseEntity<MovieDTO> addActorToMovie(@PathVariable Integer movieId, @PathVariable Integer actorId) {
        MovieDTO updatedMovie = movieService.addActorToMovie(movieId, actorId);
        return new ResponseEntity<>(updatedMovie, HttpStatus.OK);
    }

    @DeleteMapping("/{movieId}/remove-actor/{actorId}")
    public ResponseEntity<MovieDTO> removeActorFromMovie(@PathVariable Integer movieId, @PathVariable Integer actorId) {
        MovieDTO updatedMovie = movieService.removeActorFromMovie(movieId, actorId);
        return new ResponseEntity<>(updatedMovie, HttpStatus.NO_CONTENT);
    }
}