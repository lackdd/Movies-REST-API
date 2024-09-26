package com.movieapi.moviedb.services;

import com.movieapi.moviedb.entities.Genre;
import com.movieapi.moviedb.repositories.GenreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class GenreService {

    @Autowired
    private GenreRepository genreRepository;

    public Genre createGenre(Genre genre) {
        return genreRepository.save(genre);
    }

    public List<Genre> getAllGenres() {
        return genreRepository.findAll();
    }

    public Optional<Genre> getGenreById(Long id) {
        return genreRepository.findById(id);
    }

    public Genre updateGenre(Long id, Genre updatedGenre) {
        Optional<Genre> genreOptional = genreRepository.findById(id);
        if (genreOptional.isPresent()) {
            Genre genre = genreOptional.get();
            genre.setName(updatedGenre.getName());
            return genreRepository.save(genre);
        }
        return null;
    }

    public void deleteGenre(Long id) {
        genreRepository.deleteById(id);
    }
}
