package com.movieapi.moviedb.controller;

import com.movieapi.moviedb.dto.GenreDTO;
import com.movieapi.moviedb.services.GenreService;
import com.movieapi.moviedb.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/genres")
public class GenreController {

    @Autowired
    private GenreService genreService;

    @PostMapping
    public ResponseEntity<GenreDTO> createGenre(@RequestBody GenreDTO genreDTO) {
        GenreDTO createdGenre = genreService.createGenre(genreDTO);
        return new ResponseEntity<>(createdGenre, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<GenreDTO>> getAllGenres() {
        List<GenreDTO> genres = genreService.getAllGenres();
        return new ResponseEntity<>(genres, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GenreDTO> getGenreById(@PathVariable Integer id) {
        return genreService.getGenreById(id)
                .map(genre -> new ResponseEntity<>(genre, HttpStatus.OK))
                .orElseThrow(() -> new ResourceNotFoundException("Genre not found with id: " + id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<GenreDTO> updateGenre(@PathVariable Integer id, @RequestBody GenreDTO updatedGenreDTO) {
        GenreDTO updatedGenre = genreService.updateGenre(id, updatedGenreDTO);
        return new ResponseEntity<>(updatedGenre, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGenre(@PathVariable Integer id) {
        genreService.deleteGenre(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}