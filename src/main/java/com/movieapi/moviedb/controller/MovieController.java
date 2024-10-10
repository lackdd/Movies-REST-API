package com.movieapi.moviedb.controller;

import com.movieapi.moviedb.services.MovieService;
import com.movieapi.moviedb.dto.MovieDTO;
import com.movieapi.moviedb.dto.ActorDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

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
    public ResponseEntity<Page<MovieDTO>> getMovies(
            @RequestParam(value = "actor", required = false) Integer actorId,
            @RequestParam(value = "year", required = false) String year,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);

        if (year != null) {
            Page<MovieDTO> movies = movieService.getMoviesByYear(year, pageable);
            return new ResponseEntity<>(movies, HttpStatus.OK);
        }

        if (actorId != null) {
            Page<MovieDTO> movies = movieService.getMoviesByActorId(actorId, pageable);
            return new ResponseEntity<>(movies, HttpStatus.OK);
        }

        Page<MovieDTO> movies = movieService.getAllMovies(pageable);
        return new ResponseEntity<>(movies, HttpStatus.OK);
    }

    @GetMapping(params = "genre")
    public ResponseEntity<Page<MovieDTO>> getMoviesByGenre(
            @RequestParam("genre") Integer genreId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<MovieDTO> movies = movieService.getMoviesByGenre(genreId, pageable);
        return new ResponseEntity<>(movies, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<MovieDTO>> searchMoviesByTitle(
            @RequestParam("title") String title,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<MovieDTO> movies = movieService.searchMoviesByTitle(title, pageable);
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