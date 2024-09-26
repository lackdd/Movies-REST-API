package com.movieapi.moviedb.controller;

import com.movieapi.moviedb.entities.Genre;
import com.movieapi.moviedb.services.GenreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/genres")
public class GenreController {

    @Autowired
    private GenreService genreService;

    @PostMapping
    public Genre createGenre(@RequestBody Genre genre) {
        return genreService.createGenre(genre);
    }

    @GetMapping
    public List<Genre> getAllGenres() {
        return genreService.getAllGenres();
    }

    @GetMapping("/{id}")
    public Genre getGenreById(@PathVariable Long id) {
        return genreService.getGenreById(id)
                .orElseThrow(() -> new RuntimeException("Genre not found"));
    }
    @PatchMapping("/{id}")
    public Genre updateGenre(@PathVariable Long id, @RequestBody Genre updatedGenre) {
        return genreService.updateGenre(id, updatedGenre);
    }
    @DeleteMapping("/{id}")
    public void deleteGenre(@PathVariable Long id) {
        genreService.deleteGenre(id);
    }
}
